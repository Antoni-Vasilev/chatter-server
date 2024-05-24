package bg.nexanet.chatterserver.dto

import bg.nexanet.chatterserver.model.Device
import bg.nexanet.chatterserver.model.Role
import java.util.*

data class UserSessionData(
    var id: String,
    var username: String,
    var firstName: String,
    var lastName: String,
    var createDate: Date,
    var lastOnline: Date,
    var lastProfileImageUpdate: Date?,
    var roles: List<Role>,
    var devices: List<Device>,
    var emailValidate: Boolean,
    var email: String
)
