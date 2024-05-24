package bg.nexanet.chatterserver.service

import bg.nexanet.chatterserver.model.User

interface UserService {

    fun findUserByEmail(email: String): User?

    fun generateUsername(fullName: String): String

    fun save(user: User): User
}