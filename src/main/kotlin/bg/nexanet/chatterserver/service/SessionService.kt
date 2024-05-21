package bg.nexanet.chatterserver.service

import bg.nexanet.chatterserver.model.Session
import bg.nexanet.chatterserver.model.User

interface SessionService {

    fun create(session: Session): Session
    fun refresh(sessionId: String)
    fun renew(sessionId: String, deviceId: String): String
    fun findSessionsByUser(user: User): List<Session>
    fun logout(sessionId: String)
}