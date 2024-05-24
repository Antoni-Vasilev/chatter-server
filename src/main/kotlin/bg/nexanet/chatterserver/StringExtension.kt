package bg.nexanet.chatterserver

import java.security.MessageDigest
import java.util.regex.Pattern

fun String.checkEmailFormat(): Boolean {
    return Pattern.compile("^(?=.{1,64}@)[\\p{L}0-9_-]+(\\.[\\p{L}0-9_-]+)*@[^-][\\p{L}0-9-]+(\\.[\\p{L}0-9-]+)*(\\.[\\p{L}]{2,})$")
        .matcher(this)
        .matches()
}

fun String.toSHA256(): String {
    val HEX_CHARS = "0123456789ABCDEF"
    val digest = MessageDigest.getInstance("SHA-256").digest(this.toByteArray())
    return digest.joinToString(
        separator = "",
        transform = { a ->
            String(
                charArrayOf(
                    HEX_CHARS[a.toInt() shr 4 and 0x0f],
                    HEX_CHARS[a.toInt() and 0x0F]
                )
            )
        }
    )
}