package bg.nexanet.chatterserver.service.impl

import bg.nexanet.chatterserver.exception.BadRequestException
import bg.nexanet.chatterserver.exception.NotFoundException
import bg.nexanet.chatterserver.exception.UnauthorizedException
import bg.nexanet.chatterserver.model.Session
import bg.nexanet.chatterserver.model.User
import bg.nexanet.chatterserver.repository.SessionRepository
import bg.nexanet.chatterserver.repository.UserRepository
import bg.nexanet.chatterserver.service.DeviceService
import bg.nexanet.chatterserver.service.SessionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class SessionServiceImpl(
    @Autowired
    val sessionRepository: SessionRepository,

    @Autowired
    val deviceService: DeviceService,

    @Autowired
    val userRepository: UserRepository,
) : SessionService {

    override fun create(session: Session): Session {
        return sessionRepository.save(session)
    }

    override fun refresh(sessionId: String) {
        val findSession = sessionRepository.findSessionById(sessionId) ?: throw NotFoundException("Session not valid")

        val findUser = userRepository.findUserByEmail(findSession.user.email)
            ?: throw BadRequestException("This user is not available")
        findUser.lastOnline = Date()
        userRepository.save(findUser)

        val today = Date()
        if (!findSession.startDate.before(today) || !findSession.endDate.after(today)) throw UnauthorizedException("Session has expired")
        if (!findSession.isValid) throw UnauthorizedException("The session was closed")
    }

    override fun renew(sessionId: String, deviceId: String): String {
        val findSession = sessionRepository.findSessionById(sessionId) ?: throw NotFoundException("Session not valid")
        val findUserSessions = findSessionsByUser(findSession.user).sortedBy { it.startDate }

        if (!findSession.isValid) throw UnauthorizedException("The session was closed")

        if (findUserSessions.first().device.deviceId == deviceId)
            return create(
                Session(
                    findSession.id,
                    Date(),
                    Date(Date().time + 3_600_000),
                    true,
                    findSession.user,
                    findSession.device
                )
            ).id!!
        else {
            val findDevice = deviceService.findById(deviceId) ?: throw NotFoundException("Device not found")

            var isContain = false
            for (device in findSession.user.devices) {
                if (device.deviceId == findDevice.deviceId) isContain = true
            }
            if (!isContain) throw UnauthorizedException("No permission please login first")

            return create(
                Session(
                    null,
                    Date(),
                    Date(Date().time + 3_600_000),
                    true,
                    findSession.user,
                    findSession.device
                )
            ).id!!
        }
    }

    override fun findSessionsByUser(user: User): List<Session> {
        return sessionRepository.findSessionByUser(user)
    }

    override fun logout(sessionId: String) {
        val findSession = sessionRepository.findSessionById(sessionId) ?: throw NotFoundException("Session not valid")

        findSession.isValid = false
        sessionRepository.save(findSession)
    }
}