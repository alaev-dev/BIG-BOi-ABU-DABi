package dota.net.bigboiabudabi.service

import dota.net.bigboiabudabi.dto.Game
import dota.net.bigboiabudabi.dto.Player
import dota.net.bigboiabudabi.dto.SteamProfiles
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import java.time.Duration
import java.time.LocalDateTime

/**
 * communication service with Steam API
 *
 * @author Alaev Konstantin
 */
@Component
class SteamIntegration(
    // private val sender: ISender,
    @Value("\${app.developerKey}")
    private val developerKey: String,
    @Value("\${app.gamerId}")
    private val gamerId: String
) {
    private var gamesTenSecondsAgo: List<Game>? = null
    private val log = LoggerFactory.getLogger(this::class.java)
    private var startGame: LocalDateTime? = null

    /**
     * Checking the player for what he is playing
     */
    @Scheduled(fixedRate = 10000)
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
                if (startGame == null) startGame = LocalDateTime.now()
                log.warn(
                    "${player.personaname} IN GAME ${player.gameextrainfo} time:" +
                        " ${Duration.between(startGame, LocalDateTime.now()).toMinutes()} minutes"
                )
            } else {
                if (startGame != null) {
                    log.warn(
                        "In game: ${Duration.between(startGame, LocalDateTime.now()).toMinutes()} minutes"
                    )
                    startGame = null
                } else log.info("All right!")
            }
        }
    }

    /** collect hours
     * uri: "https://api.steampowered.com/IPlayerService/GetRecentlyPlayedGames/v0001/?key=$developerKey&steamid=$gamerId&format=json"
     */
    private fun analyze(games: List<Game>) {
        // first init check
        if (gamesTenSecondsAgo == null) {
            gamesTenSecondsAgo = games
            return
        }

        // collect past minutes and present
        val pastMinutes = gamesTenSecondsAgo!!.sumOf { it.playtime_forever }
        val presentMinutes = games.sumOf { it.playtime_forever }

        if (presentMinutes > pastMinutes) {
            val diff =
                games.map { it.playtime_forever }.minus(gamesTenSecondsAgo!!.map { it.playtime_forever }.toSet())[0]

            val runningGame = games.filter { it.playtime_forever == diff }[0]

            log.warn("GAMER IN GAME!!! ${runningGame.name} played ${runningGame.playtime_2weeks} minutes on 2 weeks")
        } else log.info("check: ${games.map { "${it.name} minutes: ${it.playtime_2weeks} " }} ")
    }
}
