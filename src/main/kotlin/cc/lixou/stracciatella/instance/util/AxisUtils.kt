package cc.lixou.stracciatella.instance.util

import net.minestom.server.instance.block.Block

object AxisUtils {

    fun fromAxis(axis: String) = when (axis) {
        "x" -> BlockAxis.X
        "y" -> BlockAxis.Y
        "z" -> BlockAxis.Z
        else -> throw IllegalArgumentException("Argument '$axis' isn't an axis")
    }

}

enum class BlockAxis(val axis: String) {
    X("x"),
    Y("y"),
    Z("z");

    fun switchHorizontal(): BlockAxis = when (this) {
        X -> Z
        Y -> Y
        Z -> X
    }

    fun applyAxis(block: Block): Block = block.withProperty("axis", axis)
}

fun Block.hasAxis(): Boolean = properties().containsKey("axis")

fun Block.getAxis(): BlockAxis = AxisUtils.fromAxis(getProperty("axis"))