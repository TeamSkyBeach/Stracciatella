package cc.lixou.stracciatella.npc

import net.minestom.server.entity.fakeplayer.FakePlayer
import net.minestom.server.entity.fakeplayer.FakePlayerOption
import java.util.*

class PlayerNPC(
    username: String,
    options: FakePlayerOption,
    uuid: UUID = UUID.randomUUID(),
    spawnCallback: (FakePlayer) -> Unit
) {

    init {
        FakePlayer.initPlayer(uuid, username, options, spawnCallback)
    }

}