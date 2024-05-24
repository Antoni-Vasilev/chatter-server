package bg.nexanet.chatterserver.service.impl

import bg.nexanet.chatterserver.exception.NotFoundException
import bg.nexanet.chatterserver.exception.UnauthorizedException
import bg.nexanet.chatterserver.model.ForgotPassword
import bg.nexanet.chatterserver.model.User
import bg.nexanet.chatterserver.repository.ForgotPasswordRepository
import bg.nexanet.chatterserver.service.ForgotPasswordService
import bg.nexanet.chatterserver.toSHA256
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class ForgotPasswordServiceImpl(
    @Autowired
    val forgotPasswordRepository: ForgotPasswordRepository
) : ForgotPasswordService {

    override fun generateKey(): String {
        val chars = "123456789ABCDEF"
        var key = ""

        while (key.isEmpty() || forgotPasswordRepository.findByCode(key.toSHA256()) != null) {
            val random = Random()
            for (i in 1..6) {
                key += chars[random.nextInt(chars.length)]
            }
        }

        return key
    }

    override fun create(code: String, user: User) {
        forgotPasswordRepository.save(ForgotPassword(null, code.toSHA256(), user))
    }

    override fun checkCode(code: String, email: String) {
        val findCode = forgotPasswordRepository.findByCode(code.toSHA256()) ?: throw NotFoundException("Code is wrong or invalid")
        if (findCode.user.email != email) throw UnauthorizedException("Code is wrong")
    }

    override fun findByCode(code: String): ForgotPassword {
        return forgotPasswordRepository.findByCode(code.toSHA256()) ?: throw NotFoundException("Code is wrong or invalid")
    }

    override fun deleteByCode(code: String) {
        forgotPasswordRepository.delete(findByCode(code))
    }
}