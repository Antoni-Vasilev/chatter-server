package bg.nexanet.chatterserver.repository

import bg.nexanet.chatterserver.model.Device
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DeviceRepository : JpaRepository<Device, String> {

    fun findDeviceByDeviceId(deviceId: String): Device?
}