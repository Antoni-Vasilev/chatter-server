package bg.nexanet.chatterserver.service.impl

import bg.nexanet.chatterserver.checkEmailFormat
import bg.nexanet.chatterserver.converter.UserConverter
import bg.nexanet.chatterserver.dto.UserLoginRequest
import bg.nexanet.chatterserver.dto.UserRegisterRequest
import bg.nexanet.chatterserver.exception.BadRequestException
import bg.nexanet.chatterserver.exception.DuplicateRecordException
import bg.nexanet.chatterserver.model.Session
import bg.nexanet.chatterserver.model.User
import bg.nexanet.chatterserver.repository.UserRepository
import bg.nexanet.chatterserver.service.DeviceService
import bg.nexanet.chatterserver.service.SessionService
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

    @Autowired
    var deviceService: DeviceService,

    @Autowired
    var sessionService: SessionService
) : UserService {

    override fun register(userRegisterRequest: UserRegisterRequest) {
        if (!userRegisterRequest.email.checkEmailFormat()) throw BadRequestException("Invalid email format")

        val findUser: User? = userRepository.findUserByEmail(userRegisterRequest.email)
        if (findUser != null) throw DuplicateRecordException("This email is not available")

        val convertedUser =
            userConverter.UserRegisterRequest_User(userRegisterRequest, generateUsername(userRegisterRequest.fullName))
        userRepository.save(convertedUser)
    }

    override fun login(userLoginRequest: UserLoginRequest): String {
        val findUser = userRepository.findUserByEmail(userLoginRequest.email)
            ?: throw BadRequestException("This email is not available")
        if (findUser.password != userLoginRequest.password) throw BadRequestException("This password is not valid")

        val findDevice = deviceService.findById(userLoginRequest.deviceId)
            ?: deviceService.register(
                userLoginRequest.deviceId,
                userLoginRequest.deviceName,
                userLoginRequest.notificationToken
            )

        var isContain = false
        for (device in findUser.devices) if (device.id == findDevice.id) {
            isContain = true
            break
        }
        if (!isContain) findUser.devices += findDevice
        findUser.lastOnline = Date()
        userRepository.save(findUser)

        val today = Date()
        val tomorrow = Date(today.time + 3_600_000)
        val session = sessionService.create(Session(null, today, tomorrow, true, findUser, findDevice))

        return session.id ?: throw RuntimeException("Internal server error")
    }

    fun generateUsername(fullName: String): String {
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
}