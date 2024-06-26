package bg.nexanet.chatterserver.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateRecordException::class)
    fun handleDuplicateRecordException(e: DuplicateRecordException): ResponseEntity<MessageException> {
        val message = MessageException(e.message.toString())
        return ResponseEntity.status(HttpStatus.CONFLICT).body(message)
    }

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(e: BadRequestException): ResponseEntity<MessageException> {
        val message = MessageException(e.message.toString())
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message)
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(e: NotFoundException): ResponseEntity<MessageException> {
        val message = MessageException(e.message.toString())
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message)
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedException(e: UnauthorizedException): ResponseEntity<MessageException> {
        val message = MessageException(e.message.toString())
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message)
    }
}