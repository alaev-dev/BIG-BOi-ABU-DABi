package dota.net.bigboiabudabi.dto

data class Game(
    val appid: Int,
    val img_icon_url: String,
    val name: String,
    val playtime_2weeks: Int,
    val playtime_forever: Int,
    val playtime_linux_forever: Int,
    val playtime_mac_forever: Int,
    val playtime_windows_forever: Int
)
