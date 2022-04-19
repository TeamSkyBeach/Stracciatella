package cc.lixou.stracciatella.instance

import net.minestom.server.instance.InstanceContainer
import net.minestom.server.world.DimensionType
import java.util.*

/**
 * Instance Container for our ICED Instance Format
 */
class IcedInstance(uuid: UUID, dimensionType: DimensionType = DimensionType.OVERWORLD, worldFilePath: String) :
    InstanceContainer(uuid, dimensionType, IcedChunkLoader(worldFilePath)) {

    init {

    }


}