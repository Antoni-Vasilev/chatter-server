package bg.nexanet.chatterserver

import java.util.regex.Pattern

fun String.checkEmailFormat(): Boolean {
    return Pattern.compile("^(?=.{1,64}@)[\\p{L}0-9_-]+(\\.[\\p{L}0-9_-]+)*@[^-][\\p{L}0-9-]+(\\.[\\p{L}0-9-]+)*(\\.[\\p{L}]{2,})$")
        .matcher(this)
        .matches();
}