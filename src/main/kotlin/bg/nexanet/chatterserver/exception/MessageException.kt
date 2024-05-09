package bg.nexanet.chatterserver.exception

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.Getter
import lombok.NoArgsConstructor

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Data
data class MessageException(
    var message: String
)
