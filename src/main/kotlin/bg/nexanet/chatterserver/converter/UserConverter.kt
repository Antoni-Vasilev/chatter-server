package bg.nexanet.chatterserver.converter

import bg.nexanet.chatterserver.dto.UserRegisterRequest
import bg.nexanet.chatterserver.model.User
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserConverter {

    fun UserRegisterRequest_User(userRegisterRequest: UserRegisterRequest, username: String): User {
        val nameParts = userRegisterRequest.fullName.split(" ")
        val firstName = nameParts[0]
        val lastName = nameParts[nameParts.size - 1]

        return User(
            null,
            username,
            firstName,
            lastName,
            Date(),
            null,
            userRegisterRequest.email,
            userRegisterRequest.password,
        )
    }
}