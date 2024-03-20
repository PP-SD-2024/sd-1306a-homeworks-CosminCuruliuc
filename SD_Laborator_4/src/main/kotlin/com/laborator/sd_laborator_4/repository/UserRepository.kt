package com.laborator.sd_laborator_4.repository

import com.laborator.sd_laborator_4.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<User, Long> {
    fun findByUsername(username: String): User?
}
