package cc.lixou.stracciatella.item

import net.minestom.server.entity.Player
import net.minestom.server.item.ItemMeta
import net.minestom.server.item.ItemMetaView
import net.minestom.server.item.ItemStack

class CustomItemBuilder {
    private var passedInteractionMethod: ((Player, InteractReason) -> Boolean)? = null
    private var passedMetaClass: Class<ItemMeta.Builder>? = null
    private var passedMetaWriting: ((ItemMeta.Builder) -> Unit)? = null

    fun meta(writeMeta: (ItemMeta.Builder) -> Unit) {
        this.passedMetaWriting = writeMeta
    }

    @Suppress("UNCHECKED_CAST")
    fun <V : ItemMetaView.Builder, T : ItemMetaView<V>> meta(metaClass: Class<T>, writeMeta: (V) -> Unit) {
        this.passedMetaClass = metaClass as Class<ItemMeta.Builder>
        this.passedMetaWriting = writeMeta as ((ItemMeta.Builder) -> Unit)
    }

    /**
     * Gets called when interacting with the item
     * @param onInteract interaction function with
     * the player which interacts and the action reason
     */
    fun interaction(onInteract: (player: Player, action: InteractReason) -> Boolean) {
        this.passedInteractionMethod = onInteract
    }

    /**
     * INTERNAL METHOD
     */
    @Suppress("UNCHECKED_CAST")
    fun internalApply(builder: ItemStack.Builder): ItemStack.Builder {
        if(passedMetaWriting != null) {
            if(passedMetaClass != null) {
                // SPECIAL META
                builder.meta(passedMetaClass as Class<ItemMetaView<ItemMetaView.Builder>>) {
                    passedMetaWriting!!.invoke(it)
                }
            } else {
                // NORMAL META
                builder.meta {
                    passedMetaWriting!!.invoke(it)
                    return@meta it
                }
            }
        }

        return builder
    }

    /**
     * INTERNAL METHOD
     */
    fun internalInteract(): ((Player, InteractReason) -> Boolean)? = passedInteractionMethod
}