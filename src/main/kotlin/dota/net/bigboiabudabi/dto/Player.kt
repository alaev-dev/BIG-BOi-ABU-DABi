package dota.net.bigboiabudabi.dto

data class Player(
    val avatar: String,
    val avatarfull: String,
    val avatarhash: String,
    val avatarmedium: String,
    val commentpermission: Int? = null,
    val communityvisibilitystate: Int,
    val gameextrainfo: String? = null,
    val gameid: String? = null,
    val lastlogoff: Int,
    val loccountrycode: String,
    val personaname: String,
    val personastate: Int,
    val personastateflags: Int,
    val primaryclanid: String,
    val profilestate: Int,
    val profileurl: String,
    val realname: String,
    val steamid: String,
    val timecreated: Int
)
