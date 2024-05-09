package bg.nexanet.chatterserver.dto

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
data class MessageResponse(
    var message: String
)
