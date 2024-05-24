package bg.nexanet.chatterserver.service.impl

import bg.nexanet.chatterserver.exception.NotFoundException
import bg.nexanet.chatterserver.model.Session
import bg.nexanet.chatterserver.model.User
import bg.nexanet.chatterserver.repository.SessionRepository
import bg.nexanet.chatterserver.service.SessionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SessionServiceImpl(
    @Autowired
    val sessionRepository: SessionRepository,
) : SessionService {

    override fun create(session: Session): Session {
        return sessionRepository.save(session)
    }

    override fun findSessionsByUser(user: User): List<Session> {
        return sessionRepository.findSessionByUser(user)
    }

    override fun findSessionById(sessionId: String): Session {
        return sessionRepository.findSessionById(sessionId) ?: throw NotFoundException("Session not valid")
    }

    override fun save(session: Session): Session {
        return sessionRepository.save(session)
    }

    override fun delete(session: Session) {
        sessionRepository.delete(session)
    }
}