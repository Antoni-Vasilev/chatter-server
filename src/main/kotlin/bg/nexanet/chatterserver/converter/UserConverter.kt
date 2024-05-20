package bg.nexanet.chatterserver.converter

import bg.nexanet.chatterserver.dto.UserRegisterRequest
import bg.nexanet.chatterserver.model.User
import bg.nexanet.chatterserver.service.RoleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserConverter(
    @Autowired
    val roleService: RoleService,
) {

    fun UserRegisterRequest_User(userRegisterRequest: UserRegisterRequest, username: String): User {
        val nameParts = userRegisterRequest.fullName.trim().split(" ")
        val firstName = nameParts[0]
        val lastName = nameParts[nameParts.size - 1]

        return User(
            null,
            username,
            firstName,
            lastName,
            Date(),
            null,
            listOf(roleService.client()),
            ArrayList(),
            false,
            userRegisterRequest.email,
            userRegisterRequest.password,
        )
    }
}