package cc.lixou.stracciatella.instance.util

import net.minestom.server.instance.block.Block
import org.jglrxavpok.hephaistos.nbt.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

/**
 * @author From https://github.com/CatDevz/SlimeLoader/blob/master/src/main/kotlin/gg/astromc/slimeloader/helpers/NBTHelpers.kt
 */
object NBTUtils {

    inline fun <reified T : NBT> readNBTTag(bytes: ByteArray): T? =
        NBTReader(ByteArrayInputStream(bytes), CompressedProcesser.NONE).read() as? T

    @Suppress("unchecked_cast")
    fun <T : NBT> readNBTTagRaw(bytes: ByteArray, type: NBTType<T>): T =
        NBTReader(ByteArrayInputStream(bytes), CompressedProcesser.NONE).readRaw(type.ordinal) as T

    inline fun <reified T : NBT> writeNBTTag(tag: T): ByteArray =
        ByteArrayOutputStream().also { NBTWriter(it, CompressedProcesser.NONE).writeRaw(tag) }.toByteArray()

    fun getBlockFromCompound(compound: NBTCompound): Block? {
        val name = compound.getString("Name") ?: return null
        if (name == "minecraft:air") return null
        val properties = compound.getCompound("Properties") ?: NBTCompound()

        val newProps = mutableMapOf<String, String>()
        for ((key, rawValue) in properties) {
            newProps[key] = (rawValue as NBTString).value
        }

        return Block.fromNamespaceId(name)?.withProperties(newProps)
    }

}