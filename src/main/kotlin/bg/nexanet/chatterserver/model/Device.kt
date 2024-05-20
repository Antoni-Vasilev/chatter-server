package bg.nexanet.chatterserver.model

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Getter
import lombok.NoArgsConstructor
import org.jetbrains.annotations.NotNull

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Table(name = "devices")
@Entity
class Device(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String?,

    @NotNull
    @Column(nullable = false, unique = true)
    val deviceId: String,
    val name: String,
    val notificationToken: String
)