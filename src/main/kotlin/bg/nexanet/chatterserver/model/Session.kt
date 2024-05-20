package bg.nexanet.chatterserver.model

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Getter
import lombok.NoArgsConstructor
import java.util.*

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Table(name = "sessions")
@Entity
class Session(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String?,

    val startDate: Date,
    val endDate: Date,
    val isValid: Boolean,

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    val user: User,

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    val device: Device
)