package bg.nexanet.chatterserver.service.impl

import bg.nexanet.chatterserver.converter.UserConverter
import bg.nexanet.chatterserver.model.User
import bg.nexanet.chatterserver.repository.UserRepository
import bg.nexanet.chatterserver.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserServiceImpl(
    @Autowired
    var userRepository: UserRepository,

    @Autowired
    var userConverter: UserConverter,
) : UserService {

    override fun findUserByEmail(email: String): User? {
        return userRepository.findUserByEmail(email)
    }

    override fun generateUsername(fullName: String): String {
        val username = fullName.trim().replace(" ", ".").lowercase(Locale.getDefault())

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

    override fun save(user: User): User {
        return userRepository.save(user)
    }
}