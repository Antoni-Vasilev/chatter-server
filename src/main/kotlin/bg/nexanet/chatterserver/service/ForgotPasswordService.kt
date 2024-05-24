package bg.nexanet.chatterserver.service

import bg.nexanet.chatterserver.model.ForgotPassword
import bg.nexanet.chatterserver.model.User

interface ForgotPasswordService {

    fun generateKey(): String

    fun create(code: String, user: User)

    fun checkCode(code: String, email: String)

    fun findByCode(code: String): ForgotPassword

    fun deleteByCode(code: String)
}