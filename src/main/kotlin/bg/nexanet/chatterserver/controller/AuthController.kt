package bg.nexanet.chatterserver.controller

import bg.nexanet.chatterserver.dto.MessageResponse
import bg.nexanet.chatterserver.dto.UserLoginRequest
import bg.nexanet.chatterserver.dto.UserRegisterRequest
import bg.nexanet.chatterserver.service.SessionService
import bg.nexanet.chatterserver.service.UserService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    @Autowired
    var userService: UserService,

    @Autowired
    var sessionService: SessionService
) {

    @PostMapping("/sign-up")
    fun signUp(@RequestBody @Valid userRegisterRequest: UserRegisterRequest): ResponseEntity<MessageResponse> {
        userService.register(userRegisterRequest)

        val message = MessageResponse("Registration was successful")
        return ResponseEntity.ok(message)
    }

    @PostMapping("/sign-in")
    fun signIn(@RequestBody @Valid userLoginRequest: UserLoginRequest): ResponseEntity<MessageResponse> {
        val sessionId = userService.login(userLoginRequest)
        return ResponseEntity.ok(MessageResponse(sessionId))
    }

    @PostMapping("/refresh")
    fun refresh(@RequestParam sessionId: String): ResponseEntity<MessageResponse> {
        sessionService.refresh(sessionId)
        return ResponseEntity.ok(MessageResponse("Session is valid"))
    }

    @PostMapping("/renew")
    fun renew(@RequestParam sessionId: String, @RequestParam deviceId: String): ResponseEntity<MessageResponse> {
        return ResponseEntity.ok(MessageResponse(sessionService.renew(sessionId, deviceId)))
    }

    @PostMapping("/logout")
    fun logout(@RequestParam sessionId: String): ResponseEntity<MessageResponse> {
        sessionService.logout(sessionId)
        return ResponseEntity.ok(MessageResponse("Session is logged out"))
    }
}