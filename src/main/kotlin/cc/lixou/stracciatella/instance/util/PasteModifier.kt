package cc.lixou.stracciatella.instance.util

/**
 * @param rotationY 0 = nothing, 1 = 90°, 2 = 180°...
 * @param flip 0 = nothing, 1 = x, 2 = z
 */
data class PasteModifier(val rotationY: Byte = 0, val flip: Byte = 0)
