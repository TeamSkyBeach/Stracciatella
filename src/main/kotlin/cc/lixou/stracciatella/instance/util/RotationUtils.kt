package cc.lixou.stracciatella.instance.util

import net.minestom.server.instance.block.Block

fun Block.rotateClockwise(): Block {
    var result = this
    if (hasFacing()) {
        result = getFacing().next().applyFacing(result)
    }
    if (hasAxis()) {
        result = getAxis().switchHorizontal().applyAxis(result)
    }
    if (hasDirectionFacing()) {
        result = getDirectionFacing().rotateClockwise().applyTo(result)
    }
    return result
}