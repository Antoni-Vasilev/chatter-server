package bg.nexanet.chatterserver.model

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Getter
import lombok.NoArgsConstructor

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Table(name = "forgot_password")
@Entity
data class ForgotPassword(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String?,
    val code: String,

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    val user: User
)