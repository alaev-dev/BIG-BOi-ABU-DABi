package dota.net.bigboiabudabi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class BigBOiAbuDaBiApplication

fun main(args: Array<String>) {
    runApplication<BigBOiAbuDaBiApplication>(*args)
}
