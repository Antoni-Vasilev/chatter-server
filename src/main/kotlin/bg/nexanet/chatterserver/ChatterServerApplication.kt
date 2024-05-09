package bg.nexanet.chatterserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ChatterServerApplication

fun main(args: Array<String>) {
    runApplication<ChatterServerApplication>(*args)
}
