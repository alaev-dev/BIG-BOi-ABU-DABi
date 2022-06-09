package dota.net.bigboiabudabi.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.time.LocalDateTime
import kotlin.random.Random

@Service
class SendService(
    @Value("\${app.accessTokenVK}")
    var accessTokenVk: String,
    @Value("\${app.chatIdVk}")
    var chatIdVk: String
) : ISender {

    private val log = LoggerFactory.getLogger(SendService::class.java)

    /**
     * Метод рассылки сообщений
     * @param message сообщение для рассылки кирюхе
     * @author некий Икоркин Алексей
     */
    override fun sendMessage(message: String) {
        WebClient.create("https://api.vk.com/method/messages.send")
            .get()
            .uri {
                it.queryParam("chat_id", chatIdVk)
                    .queryParam("random_id", Random(LocalDateTime.now().nano).nextInt().toString())
                    .queryParam("message", message)
                    .queryParam("access_token", accessTokenVk)
                    .queryParam("v", "5.131")
                    .build()
            }
            .exchangeToMono { it.bodyToMono(String::class.java) }
            .subscribe { log.info(it) }
    }
}
