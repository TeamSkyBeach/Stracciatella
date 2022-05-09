package cc.lixou.stracciatella.game

import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.trait.InstanceEvent

class GameState<T : Enum<*>>(
    private val game: Game,
    private val defaultState: T,
    val parentEventNode: EventNode<*> = game.eventNode
) {

    private val eventNodes = mutableMapOf<T, EventNode<in InstanceEvent>>()

    var state: T = defaultState
        set(value) {
            eventNodes[field]?.let {
                @Suppress("unchecked_cast")
                parentEventNode.removeChild(it as EventNode<out Nothing>)
            }
            eventNodes[value]?.let {
                @Suppress("unchecked_cast")
                parentEventNode.addChild(it as EventNode<out Nothing>)
            }
            field = value
        }

    fun eventNode(ofState: T): EventNode<in InstanceEvent> =
        eventNodes.getOrPut(ofState) { generateNode(ofState).also { if (state == ofState) register(it) } }

    private fun generateNode(ofState: T): EventNode<in InstanceEvent> =
        EventNode.type("${game.javaClass.simpleName}-${game.uuid}-${state.name}", EventFilter.INSTANCE) { _, _ ->
            this@GameState.state == ofState
        }

    private fun register(node: EventNode<in InstanceEvent>?) {
        node?.let {
            @Suppress("unchecked_cast")
            parentEventNode.addChild(it as EventNode<out Nothing>)
        }
    }


}