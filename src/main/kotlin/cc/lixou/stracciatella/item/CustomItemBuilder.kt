package cc.lixou.stracciatella.item

import cc.lixou.stracciatella.item.extensions.getCreamID
import net.minestom.server.entity.Player
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.item.ItemMeta
import net.minestom.server.item.ItemMetaView
import net.minestom.server.item.ItemStack

class CustomItemBuilder(
    private val customItem: CustomItem,
    private val eventNode: EventNode<Event>
) {
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
     * Registers an event to the items eventNode
     * @param eventClass The Event Class
     * @param listener the code which gets executed on the event
     */
    fun <T : Event> event(eventClass: Class<T>, listener: (T) -> Unit, vararg checkingItems: (T) -> ItemStack) {
        eventNode.addListener(eventClass) {
            var hasRequiredItem = false
            checkingItems.forEach { item -> hasRequiredItem = hasRequiredItem || item.invoke(it).getCreamID().equals(customItem.getCreamID()) }
            if(!hasRequiredItem) return@addListener
            listener.invoke(it)
        }
    }

    /**
     * INTERNAL METHOD
     */
    @Suppress("UNCHECKED_CAST")
    fun internalApply(builder: ItemStack.Builder): ItemStack.Builder {
        if (passedMetaWriting != null) {
            if (passedMetaClass != null) {
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
}