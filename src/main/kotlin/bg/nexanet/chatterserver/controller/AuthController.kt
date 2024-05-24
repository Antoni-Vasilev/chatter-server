package bg.nexanet.chatterserver.controller

import bg.nexanet.chatterserver.checkEmailFormat
import bg.nexanet.chatterserver.converter.UserConverter
import bg.nexanet.chatterserver.dto.MessageResponse
import bg.nexanet.chatterserver.dto.UserLoginRequest
import bg.nexanet.chatterserver.dto.UserRegisterRequest
import bg.nexanet.chatterserver.dto.UserSessionData
import bg.nexanet.chatterserver.exception.BadRequestException
import bg.nexanet.chatterserver.exception.DuplicateRecordException
import bg.nexanet.chatterserver.exception.NotFoundException
import bg.nexanet.chatterserver.exception.UnauthorizedException
import bg.nexanet.chatterserver.model.Session
import bg.nexanet.chatterserver.model.User
import bg.nexanet.chatterserver.service.DeviceService
import bg.nexanet.chatterserver.service.ForgotPasswordService
import bg.nexanet.chatterserver.service.SessionService
import bg.nexanet.chatterserver.service.UserService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ResourceLoader
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.web.bind.annotation.*
import java.nio.file.Files
import java.util.*

@Suppress("NAME_SHADOWING")
@RestController
@RequestMapping("/api/auth")
class AuthController(
    @Autowired
    var userService: UserService,

    @Autowired
    var sessionService: SessionService,

    @Autowired
    var userConverter: UserConverter,

    @Autowired
    var deviceService: DeviceService,

    @Autowired
    var forgotPasswordService: ForgotPasswordService,

    @Autowired
    var javaMailSender: JavaMailSender,

    @Autowired
    var resourceLoader: ResourceLoader,
) {

    @PostMapping("/sign-up")
    fun signUp(@RequestBody @Valid userRegisterRequest: UserRegisterRequest): ResponseEntity<MessageResponse> {
        if (!userRegisterRequest.email.checkEmailFormat()) throw BadRequestException("Invalid email format")

        val findUser: User? = userService.findUserByEmail(userRegisterRequest.email)
        if (findUser != null) throw DuplicateRecordException("This email is not available")

        val convertedUser = userConverter.UserRegisterRequest_User(
            userRegisterRequest,
            userService.generateUsername(userRegisterRequest.fullName)
        )
        userService.save(convertedUser)

        val message = MessageResponse("Registration was successful")
        return ResponseEntity.ok(message)
    }

    @PostMapping("/sign-in")
    fun signIn(@RequestBody @Valid userLoginRequest: UserLoginRequest): ResponseEntity<MessageResponse> {
        val findUser = userService.findUserByEmail(userLoginRequest.email)
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
        userService.save(findUser)

        val start = Date()
        val end = Date(start.time + 3_600_000)
        val session = sessionService.create(Session(null, start, end, true, findUser, findDevice))

        return ResponseEntity.ok(MessageResponse(session.id!!))
    }

    @PostMapping("/refresh")
    fun refresh(@RequestParam sessionId: String): ResponseEntity<MessageResponse> {
        val findSession = sessionService.findSessionById(sessionId)

        val findUser = userService.findUserByEmail(findSession.user.email)
            ?: throw BadRequestException("This user is not available")
        findUser.lastOnline = Date()
        userService.save(findUser)

        val today = Date()
        if (!findSession.startDate.before(today) || !findSession.endDate.after(today)) throw UnauthorizedException("Session has expired")
        if (!findSession.isValid) throw UnauthorizedException("The session was closed")

        return ResponseEntity.ok(MessageResponse("Session is valid"))
    }

    @PostMapping("/renew")
    fun renew(@RequestParam sessionId: String, @RequestParam deviceId: String): ResponseEntity<MessageResponse> {
        val findSession = sessionService.findSessionById(sessionId)
        val findUserSessions = sessionService.findSessionsByUser(findSession.user).sortedBy { it.startDate }

        if (!findSession.isValid) throw UnauthorizedException("The session was closed")

        val sessionId: String
        if (findUserSessions.first().device.deviceId == deviceId)
            sessionId = sessionService.create(
                Session(
                    findSession.id,
                    Date(),
                    Date(Date().time + 3_600_000),
                    true,
                    findSession.user,
                    findSession.device
                )
            ).id!!
        else {
            val findDevice = deviceService.findById(deviceId) ?: throw NotFoundException("Device not found")

            var isContain = false
            for (device in findSession.user.devices) {
                if (device.deviceId == findDevice.deviceId) isContain = true
            }
            if (!isContain) throw UnauthorizedException("No permission please login first")

            sessionId = sessionService.create(
                Session(
                    null,
                    Date(),
                    Date(Date().time + 3_600_000),
                    true,
                    findSession.user,
                    findSession.device
                )
            ).id!!
        }

        return ResponseEntity.ok(MessageResponse(sessionId))
    }

    @PostMapping("/logout")
    fun logout(@RequestParam sessionId: String): ResponseEntity<MessageResponse> {
        val findSession = sessionService.findSessionById(sessionId)
        findSession.isValid = false
        if (findSession.user.devices.contains(findSession.device)) findSession.user.devices -= findSession.device
        userService.save(findSession.user)
        sessionService.save(findSession)
        return ResponseEntity.ok(MessageResponse("Session is logged out"))
    }

    @GetMapping("/account")
    fun account(@RequestParam sessionId: String): ResponseEntity<UserSessionData> {
        val findSession = sessionService.findSessionById(sessionId)
        return ResponseEntity.ok(userConverter.User_UserSessionData(findSession.user))
    }

    @PostMapping("/forgotPassword/generate")
    fun forgotPasswordGenerate(@RequestParam email: String): ResponseEntity<MessageResponse> {
        val generatedKey = forgotPasswordService.generateKey()
        val findUser = userService.findUserByEmail(email) ?: throw NotFoundException("User not found")

        val message = javaMailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true, "UTF-8")

        helper.setTo(email)
        helper.setSubject("Password Reset Code")
        helper.setText(
            "<h1>Confirmation code: <span style='background-color: #087ABA; padding: 4px 8px; border-radius: 6px; color: white; letter-spacing: 2px; text-align: center'>$generatedKey</span></h1>",
            true
        )

        javaMailSender.send(message)

        forgotPasswordService.create(generatedKey, findUser)

        return ResponseEntity.ok(MessageResponse("The email is sent"))
    }

    @PostMapping("/forgotPassword/check")
    fun forgotPasswordCheck(@RequestParam code: String, @RequestParam email: String): ResponseEntity<MessageResponse> {
        forgotPasswordService.checkCode(code, email)
        return ResponseEntity.ok(MessageResponse("Code is valid"))
    }

    @PostMapping("/forgotPassword/change")
    fun forgotPasswordChange(
        @RequestParam code: String,
        @RequestParam email: String,
        @RequestParam password: String
    ): ResponseEntity<MessageResponse> {
        forgotPasswordService.checkCode(code, email)
        val findUser = userService.findUserByEmail(email) ?: throw NotFoundException("User not found")
        findUser.password = password
        userService.save(findUser)
        forgotPasswordService.deleteByCode(code)

        val sessions = sessionService.findSessionsByUser(findUser)
        sessions.forEach { sessionService.delete(it) }

        return ResponseEntity.ok(MessageResponse("Password updated successfully"))
    }

    @GetMapping("/profileImage/{id}", produces = [MediaType.IMAGE_JPEG_VALUE])
    fun profileImage(@PathVariable id: String): ByteArray {
        return Files.readAllBytes(resourceLoader.getResource("classpath:static/def_profile_pic.png").file.toPath())
    }
}