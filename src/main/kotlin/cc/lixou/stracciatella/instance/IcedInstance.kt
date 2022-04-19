package cc.lixou.stracciatella.instance

import net.minestom.server.instance.InstanceContainer
import net.minestom.server.world.DimensionType
import java.util.*

/**
 * Instance Container for our ICED Instance Format
 */
class IcedInstance(uuid: UUID, dimensionType: DimensionType = DimensionType.OVERWORLD) :
    InstanceContainer(uuid, dimensionType, IcedChunkLoader()) {

    init {
        
    }


}