package bg.nexanet.chatterserver.service

import bg.nexanet.chatterserver.model.Device

interface DeviceService {

    fun findById(deviceId: String): Device?

    fun register(id: String, name: String, notificationToken: String): Device
}