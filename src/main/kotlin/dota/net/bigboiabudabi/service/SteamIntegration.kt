package dota.net.bigboiabudabi.service

import dota.net.bigboiabudabi.dto.Game
import dota.net.bigboiabudabi.dto.Player
import dota.net.bigboiabudabi.dto.SteamProfiles
import dota.net.bigboiabudabi.enum.RealName
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.time.Duration
import java.time.LocalDateTime

/**
 * communication service with Steam API
 *
 * @author Alaev Konstantin
 */
@Service
class SteamIntegration(
    private val sender: ISender,
    @Value("\${app.developerKey}")
    var developerKey: String,
    @Value("\${app.gamerId}")
    var gamerId: String,
) {
    private var gamesTenSecondsAgo: List<Game>? = null
    private val log = LoggerFactory.getLogger(this::class.java)
    private var startGame: LocalDateTime? = null
    private val mapName: Map<RealName, String> = mapOf(
        RealName.Kiruha to "КИРЮХА",
        RealName.Cumstantin to "КОСТЯ",
        RealName.Gelya to "ГЕЛЯ",
        RealName.Miktor to "МИКТОР",
        RealName.Petya to "ПЕТЯ",
        RealName.Leha to "ЛЕХА"
        )
    var timeoutToFuck: Long = 5

    /**
     * Checking the player for what he is playing
     */
    @Scheduled(fixedRate = 60100)
    fun checkOnGaming() {
        WebClient.create().get().uri(
            "https://api.steampowered.com/ISteamUser/GetPlayerSummaries/v2/" +
                "?key=$developerKey&format=json&steamids=$gamerId"
        ).exchangeToMono { response ->
            response.bodyToMono(SteamProfiles::class.java)
        }.subscribe { checkInGame(it.response.players) }
    }

    private fun checkInGame(games: List<Player>) {
        games.forEach { player ->
            if (player.gameextrainfo != null) {
                sendIngameTimeToSenderAndLog(player)
            } else {
                sendResultIngameTimeAndLog()
            }
        }
    }

    private fun sendResultIngameTimeAndLog() {
        if (startGame != null) {
            val message = "ЖЕСТКО ПРОЕБАЛ ${Duration.between(startGame, LocalDateTime.now()).toMinutes()} МИНУТ"

            log.warn(message)
            sender.sendMessage(message)
            startGame = null
        } else log.info("All right!")
    }

    private fun sendIngameTimeToSenderAndLog(player: Player) {
        if (startGame == null) startGame = LocalDateTime.now()
        val message = "${player.personaname} ЖЕСТКО ЗАДРОТИТ В ${player.gameextrainfo} УЖЕ БЛЯТЬ" +
            " ${Duration.between(startGame, LocalDateTime.now()).toMinutes()} МИНУТ. ОСТАНОВИСЬ!!!"

        log.warn(message)

        if (Duration.between(startGame, LocalDateTime.now()).toMinutes() >= timeoutToFuck)
            sender.sendMessage(message)
    }
}
