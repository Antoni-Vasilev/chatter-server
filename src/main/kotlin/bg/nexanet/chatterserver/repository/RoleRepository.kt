package bg.nexanet.chatterserver.repository

import bg.nexanet.chatterserver.model.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : JpaRepository<Role, String> {

    fun findRoleByName(name: String): Role
}