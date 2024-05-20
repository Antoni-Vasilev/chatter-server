package bg.nexanet.chatterserver.service.impl

import bg.nexanet.chatterserver.model.Role
import bg.nexanet.chatterserver.repository.RoleRepository
import bg.nexanet.chatterserver.service.RoleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RoleServiceImpl(
    @Autowired
    val roleRepository: RoleRepository
) : RoleService {

    override fun client(): Role {
        return roleRepository.findRoleByName("client")
    }
}