package bg.nexanet.chatterserver.model

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Getter
import lombok.NoArgsConstructor
import org.jetbrains.annotations.NotNull
import java.util.*

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Table(name = "users")
@Entity
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String?,

    @NotNull
    @Column(unique = true, nullable = false)
    var username: String,

    @NotNull
    @Column(nullable = false)
    var firstName: String,
    var lastName: String,

    @NotNull
    @Column(nullable = false)
    var createDate: Date,
    var lastOnline: Date?,

    @Email
    @NotNull
    @Column(nullable = false, unique = true)
    var email: String,

    @NotNull
    @Column(nullable = false)
    var password: String,
)
