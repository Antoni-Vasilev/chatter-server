package bg.nexanet.chatterserver.dto

import jakarta.validation.constraints.Email
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Getter
import lombok.NoArgsConstructor
import org.jetbrains.annotations.NotNull

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
data class UserRegisterRequest(
    @NotNull
    var fullName: String,

    @Email
    @NotNull
    var email: String,

    @NotNull
    var password: String
)
