package bg.nexanet.chatterserver.repository

import bg.nexanet.chatterserver.model.ForgotPassword
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ForgotPasswordRepository : JpaRepository<ForgotPassword, String> {
    fun findByCode(code: String): ForgotPassword?
}