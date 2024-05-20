package bg.nexanet.chatterserver.service

import bg.nexanet.chatterserver.model.Role

interface RoleService {

    fun client(): Role
}