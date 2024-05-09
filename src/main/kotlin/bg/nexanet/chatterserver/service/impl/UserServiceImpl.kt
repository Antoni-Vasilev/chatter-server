package bg.nexanet.chatterserver.service.impl

import bg.nexanet.chatterserver.converter.UserConverter
import bg.nexanet.chatterserver.dto.UserRegisterRequest
import bg.nexanet.chatterserver.exception.BadRequestException
import bg.nexanet.chatterserver.exception.DuplicateRecordException
import bg.nexanet.chatterserver.model.User
import bg.nexanet.chatterserver.repository.UserRepository
import bg.nexanet.chatterserver.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import java.util.regex.Pattern

@Service
class UserServiceImpl(
    @Autowired
    var userRepository: UserRepository,

    @Autowired
    var userConverter: UserConverter,
) : UserService {

    override fun register(userRegisterRequest: UserRegisterRequest) {
        if (!checkEmailFormat(userRegisterRequest.email)) throw BadRequestException("Invalid email format")

        val findUser: User? = userRepository.findUserByEmail(userRegisterRequest.email)
        if (findUser != null) throw DuplicateRecordException("This email is not available")

        val convertedUser =
            userConverter.UserRegisterRequest_User(userRegisterRequest, generateUsername(userRegisterRequest.fullName))
        userRepository.save(convertedUser)
    }

    fun generateUsername(fullName: String): String {
        val username = fullName.replace(" ", ".").lowercase(Locale.getDefault())

        var tries = 0
        if (userRepository.findUserByUsername(username) != null) {
            while (true) {
                tries++
                val subUsername = "$username.$tries"
                if (userRepository.findUserByUsername(subUsername) == null) break
            }
        }

        return if (tries != 0) "$username.$tries" else username
    }

    fun checkEmailFormat(email: String): Boolean {
        return Pattern.compile("^(?=.{1,64}@)[\\p{L}0-9_-]+(\\.[\\p{L}0-9_-]+)*@[^-][\\p{L}0-9-]+(\\.[\\p{L}0-9-]+)*(\\.[\\p{L}]{2,})$")
            .matcher(email)
            .matches();
    }
}