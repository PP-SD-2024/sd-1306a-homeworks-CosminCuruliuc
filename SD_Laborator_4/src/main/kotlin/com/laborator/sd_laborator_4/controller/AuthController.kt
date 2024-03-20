package com.laborator.sd_laborator_4.controller

import com.laborator.sd_laborator_4.service.AuthService
import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.web.bind.annotation.*
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.time.Instant

@RestController
@RequestMapping("/auth")
class AuthController(@Autowired private val authService: AuthService) {

    @Value("\${jwt.private.key}")
    private lateinit var privateKey: RSAPrivateKey

    @Value("\${jwt.public.key}")
    private lateinit var publicKey: RSAPublicKey

    @PostMapping("/register")
    fun register(@RequestBody registrationData: Map<String, String>): ResponseEntity<Any> {
        val user = authService.registerUser(
            registrationData["username"]!!,
            registrationData["password"]!!,
            registrationData["firstName"]!!,
            registrationData["lastName"]!!
        ) ?: return ResponseEntity.badRequest().body("User already exists")

        return ResponseEntity.ok().body("User ${user.username} registered successfully")
    }

    @PostMapping("/login")
    fun login(@RequestBody loginData: Map<String, String>): ResponseEntity<Any> {
        val authenticated = authService.authenticateUser(
            loginData["username"]!!,
            loginData["password"]!!
        )

        if (!authenticated) {
            return ResponseEntity.status(401).body("Authentication failed")
        }

        val encoder = jwtEncoder()
        val now = Instant.now()
        val expiration = now.plusSeconds(3600000L)
        val token = encoder.encode(
            JwtEncoderParameters.from(
                JwtClaimsSet.builder().issuedAt(now).expiresAt(expiration).issuer("app").notBefore(now).build()
            )
        )

        return ResponseEntity.ok().body(token.tokenValue)
    }

    fun jwtEncoder(): NimbusJwtEncoder {
        val jwk: JWK = RSAKey.Builder(this.publicKey).privateKey(this.privateKey).build()
        val jwks: JWKSource<SecurityContext> = ImmutableJWKSet(JWKSet(jwk))
        return NimbusJwtEncoder(jwks)
    }
}
