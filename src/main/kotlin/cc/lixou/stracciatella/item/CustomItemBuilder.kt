package cc.lixou.stracciatella.item

import cc.lixou.stracciatella.item.extensions.getCreamID
import net.minestom.server.entity.Player
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.item.ItemDropEvent
import net.minestom.server.event.trait.ItemEvent
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
    fun <T : Event> event(eventClass: Class<T>, listener: (T) -> Unit) {
        eventNode.addListener(eventClass, listener)
    }

    /**
     * Registers multiple events from same type to the items eventNode
     * @param baseClass the eventClass every subClass extends
     * @param listener the code which gets executed on the event
     * @param subClasses the subclasses of the baseClasses which get called
     */
    fun <T : Event> event(baseClass: Class<T>, listener: (T) -> Unit, vararg subClasses: Class<out T>) {
        subClasses.forEach { eventNode.addListener(it, listener) }
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