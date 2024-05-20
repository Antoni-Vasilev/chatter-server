package bg.nexanet.chatterserver.dto

import lombok.*

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Data
class UserLoginRequest(
    val email: String,
    val password: String,

    val deviceId: String,
    val deviceName: String,
    val notificationToken: String
)