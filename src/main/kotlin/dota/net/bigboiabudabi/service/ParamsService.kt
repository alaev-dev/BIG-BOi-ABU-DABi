package dota.net.bigboiabudabi.service

import dota.net.bigboiabudabi.dto.Params
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ParamsService(
    private val steamIntegration: SteamIntegration,
    private val sendService: SendService
) {

    private val log = LoggerFactory.getLogger(SendService::class.java)

    fun setParams(params: Params) {
        log.info("New update params")
        params.accessTokenVK?.let {
            sendService.accessTokenVk = it
            log.info("New accessTokenVk" + sendService.accessTokenVk)
        }
        params.chatIdVk?.let {
            sendService.chatIdVk = it
            log.info("New chatIdVk" + sendService.chatIdVk)
        }
        params.gamerId?.let {
            steamIntegration.gamerId = it
            log.info("New gamerId" + steamIntegration.gamerId)
        }
        params.developerKey?.let {
            steamIntegration.developerKey = it
            log.info("New developerKey" + steamIntegration.developerKey)
        }
    }
}
