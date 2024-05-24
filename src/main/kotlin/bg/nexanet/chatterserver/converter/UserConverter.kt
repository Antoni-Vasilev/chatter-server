package bg.nexanet.chatterserver.converter

import bg.nexanet.chatterserver.dto.UserRegisterRequest
import bg.nexanet.chatterserver.dto.UserSessionData
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
            null,
            listOf(roleService.client()),
            ArrayList(),
            false,
            userRegisterRequest.email,
            userRegisterRequest.password,
        )
    }

    fun User_UserSessionData(user: User): UserSessionData {
        return UserSessionData(
            user.id!!,
            user.username,
            user.firstName,
            user.lastName,
            user.createDate,
            user.lastOnline ?: Date(),
            user.profileImageLastChange,
            user.roles,
            user.devices,
            user.emailValidate,
            user.email
        )
    }
}