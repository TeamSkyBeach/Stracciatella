package cc.lixou.stracciatella.inventory.extensions

import cc.lixou.stracciatella.item.impl.PlaceholderItem
import net.minestom.server.inventory.AbstractInventory
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

/**
 * Styles the inventory
 * @param material the material of the corner frames
 * @param rowAmount amount of rows
 * @param startPos the index from which to start
 * @param rowLength length of a single row
 */
fun AbstractInventory.styleRadialBackground(
    material: Material,
    rowAmount: Byte,
    startPos: Byte = 0,
    rowLength: Byte = 9
) {
    val edgeFrame = PlaceholderItem.createItemStack(material)
    val outerFrame = PlaceholderItem.createItemStack(Material.BLACK_STAINED_GLASS_PANE)
    val innerFrame = PlaceholderItem.createItemStack(Material.GRAY_STAINED_GLASS_PANE)
    for (r in 1..rowAmount) {
        for (c in 1..rowLength) {
            if (r == 1 || r.toByte() == rowAmount) {
                if (c == 1 || c == 2 || c == rowLength - 1 || c.toByte() == rowLength) {
                    setItemStack(r, c, edgeFrame, startPos.toInt())
                } else {
                    setItemStack(r, c, outerFrame, startPos.toInt())
                }
            } else if (c == 1 || c.toByte() == rowLength) {
                if (r == 2 || r == rowAmount - 1) {
                    setItemStack(r, c, edgeFrame, startPos.toInt())
                } else {
                    setItemStack(r, c, outerFrame, startPos.toInt())
                }
            } else {
                setItemStack(r, c, innerFrame, startPos.toInt())
            }
        }
    }
}

fun AbstractInventory.setItemStack(row: Int, column: Int, itemStack: ItemStack, offset: Int = 0) {
    setItemStack(offset + ((row - 1) * 9 + (column - 1)), itemStack)
}