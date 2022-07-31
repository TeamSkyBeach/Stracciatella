package cc.lixou.stracciatella.instance.util

import net.minestom.server.instance.block.Block

object FacingUtils {
    fun fromBlock(block: Block): Faces = Faces(
        block.getProperty("east").equals("true"),
        block.getProperty("north").equals("true"),
        block.getProperty("south").equals("true"),
        block.getProperty("west").equals("true")
    )
}

class Faces(var east: Boolean, var north: Boolean, var south: Boolean, var west: Boolean) {

    fun rotateClockwise(): Faces {
        val oldEast = east
        east = north
        north = west
        west = south
        south = oldEast
        return this
    }

    fun applyTo(block: Block): Block = block.withProperties(
        mapOf(
            Pair("east", east.toString()),
            Pair("north", north.toString()),
            Pair("south", south.toString()),
            Pair("west", west.toString())
        )
    )
}

fun Block.hasDirectionFacing() = properties().containsKey("east")

fun Block.getDirectionFacing() = FacingUtils.fromBlock(this)