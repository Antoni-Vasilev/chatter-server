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
@Table(name = "roles")
@Entity
class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String,
    val name: String,
    val priority: Byte
)