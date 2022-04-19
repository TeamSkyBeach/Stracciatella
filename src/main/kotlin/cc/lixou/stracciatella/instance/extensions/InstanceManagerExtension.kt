package cc.lixou.stracciatella.instance.extensions

import cc.lixou.stracciatella.instance.IcedInstance
import net.minestom.server.instance.InstanceManager
import net.minestom.server.world.DimensionType
import java.util.*

fun InstanceManager.createIcedInstance(worldFilePath: String, dimensionType: DimensionType = DimensionType.OVERWORLD): IcedInstance {
    val icedInstance = IcedInstance(UUID.randomUUID(), dimensionType, worldFilePath)
    registerInstance(icedInstance)
    return icedInstance
}