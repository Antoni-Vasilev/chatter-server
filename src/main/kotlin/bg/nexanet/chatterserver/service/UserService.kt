package bg.nexanet.chatterserver.service

import bg.nexanet.chatterserver.dto.UserLoginRequest
import bg.nexanet.chatterserver.dto.UserRegisterRequest

interface UserService {

    fun register(userRegisterRequest: UserRegisterRequest)

    fun login(userLoginRequest: UserLoginRequest): String
}