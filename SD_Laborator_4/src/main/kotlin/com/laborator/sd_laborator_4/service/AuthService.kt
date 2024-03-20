package com.laborator.sd_laborator_4.service

import com.laborator.sd_laborator_4.model.User
import com.laborator.sd_laborator_4.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(@Autowired private val userRepository: UserRepository) {

    private val encoder = BCryptPasswordEncoder()

    fun registerUser(username: String, password: String, firstName: String, lastName: String): User? {
        if (userRepository.findByUsername(username) != null) {
            return null // Utilizatorul exista deja
        }
        val hashedPassword = encoder.encode(password)
        val newUser = User(username = username, password = hashedPassword, firstName = firstName, lastName = lastName)
        userRepository.save(newUser)
        return newUser
    }

    fun authenticateUser(username: String, password: String): Boolean {
        val user = userRepository.findByUsername(username) ?: return false
        return encoder.matches(password, user.getPassword())
    }
}
