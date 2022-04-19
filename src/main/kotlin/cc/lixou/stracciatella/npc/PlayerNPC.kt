package cc.lixou.stracciatella.npc

import net.minestom.server.entity.fakeplayer.FakePlayer
import net.minestom.server.entity.fakeplayer.FakePlayerOption
import java.util.*

class PlayerNPC(uuid: UUID, username: String, options: FakePlayerOption, spawnCallback: (FakePlayer) -> Unit) : NPC {

    init {
        FakePlayer.initPlayer(uuid, username, options, spawnCallback)
    }

}