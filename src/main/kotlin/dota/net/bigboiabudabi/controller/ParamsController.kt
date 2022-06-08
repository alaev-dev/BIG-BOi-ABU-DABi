package dota.net.bigboiabudabi.controller

import dota.net.bigboiabudabi.dto.Params
import dota.net.bigboiabudabi.service.ParamsService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("v0.1")
class ParamsController(
    private val paramsService: ParamsService
) {
    @PostMapping("/setParams")
    fun setParams(
        @RequestBody params: Params
    ) {
        paramsService.setParams(params)
    }
}
