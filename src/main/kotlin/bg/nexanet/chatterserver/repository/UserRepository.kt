package bg.nexanet.chatterserver.repository

import bg.nexanet.chatterserver.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, String> {

    fun findUserByEmail(email: String): User?
    fun findUserByUsername(username: String): User?
}