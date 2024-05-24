package bg.nexanet.chatterserver.service

import bg.nexanet.chatterserver.model.Session
import bg.nexanet.chatterserver.model.User

interface SessionService {

    fun create(session: Session): Session

    fun findSessionsByUser(user: User): List<Session>

    fun findSessionById(sessionId: String): Session

    fun save(session: Session): Session

    fun delete(session: Session)
}