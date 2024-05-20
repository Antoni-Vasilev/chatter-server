package bg.nexanet.chatterserver.service.impl

import bg.nexanet.chatterserver.model.Device
import bg.nexanet.chatterserver.repository.DeviceRepository
import bg.nexanet.chatterserver.service.DeviceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DeviceServiceImpl(
    @Autowired
    val deviceRepository: DeviceRepository
) : DeviceService {

    override fun findById(deviceId: String): Device? {
        return deviceRepository.findDeviceByDeviceId(deviceId)
    }

    override fun register(id: String, name: String, notificationToken: String): Device {
        val device = Device(null, id, name, notificationToken)
        return deviceRepository.save(device)
    }
}