package bg.nexanet.chatterserver.controller

import bg.nexanet.chatterserver.dto.MessageResponse
import bg.nexanet.chatterserver.dto.UserRegisterRequest
import bg.nexanet.chatterserver.service.UserService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    @Autowired
    var userService: UserService
) {

    @PostMapping("/sign-up")
    fun signUp(@RequestBody @Valid userRegisterRequest: UserRegisterRequest): ResponseEntity<MessageResponse> {
        userService.register(userRegisterRequest)

        val message = MessageResponse("Registration was successful")
        return ResponseEntity.ok(message)
    }
}