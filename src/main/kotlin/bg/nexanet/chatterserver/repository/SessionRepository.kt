package bg.nexanet.chatterserver.repository

import bg.nexanet.chatterserver.model.Session
import bg.nexanet.chatterserver.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SessionRepository : JpaRepository<Session, String> {

    fun findSessionById(id: String): Session?
    fun findSessionByUser(user: User): List<Session>
}