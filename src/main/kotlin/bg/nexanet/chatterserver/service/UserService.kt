package bg.nexanet.chatterserver.service

import bg.nexanet.chatterserver.dto.UserRegisterRequest

interface UserService {

    fun register(userRegisterRequest: UserRegisterRequest)
}