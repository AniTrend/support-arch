package co.anitrend.arch.core.consumer.event

import co.anitrend.arch.core.consumer.SupportObserver

/**
 * An event bus representation of published events
 *
 * @see [SupportObserver]
 * @since v0.9.X
 */
interface ISupportObserveEvent {
    fun onEventRecieved(consumer: SupportObserver)
}