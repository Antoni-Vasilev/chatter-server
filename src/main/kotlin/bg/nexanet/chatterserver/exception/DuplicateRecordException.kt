package bg.nexanet.chatterserver.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.CONFLICT)
class DuplicateRecordException(
    message: String
) : RuntimeException(message)