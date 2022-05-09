package cc.lixou.stracciatella

import kotlinx.serialization.Serializable

@Serializable
data class ServerConfig(
    @Serializable
    var port: Int = 25565
)