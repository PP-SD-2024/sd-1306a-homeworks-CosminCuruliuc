package com.laborator.sd_laborator_4.security

import com.nimbusds.jose.crypto.MACVerifier
import org.springframework.web.filter.GenericFilterBean
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import java.security.PublicKey
import java.security.interfaces.RSAPublicKey
import java.time.Instant
import java.util.*

class JwtTokenFilter : GenericFilterBean() {

    @Value("\${jwt.public.key}")
    private lateinit var publicKey: RSAPublicKey

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpServletRequest = request as HttpServletRequest
        val token = httpServletRequest.getHeader("Authorization")?.substring("Bearer ".length)
        if (token != null && validateToken(token)) {
            val claims = getClaims(token)
            val auth = UsernamePasswordAuthenticationToken(
                claims.subject, null, listOf(SimpleGrantedAuthority("ROLE_USER"))
            )
            SecurityContextHolder.getContext().authentication = auth
        }
        chain.doFilter(request, response)
    }

    private fun validateToken(token: String): Boolean {
        val jwtDecoder: JwtDecoder = NimbusJwtDecoder.withPublicKey(this.publicKey).build()
        val decoded = jwtDecoder.decode(token)
        val now = Instant.now()
        return (decoded.expiresAt!! > now) && (decoded.notBefore < now)
    }

    private fun getClaims(token: String): JWTClaimsSet {
        val signedJWT = SignedJWT.parse(token)
        return signedJWT.jwtClaimsSet
    }
}
