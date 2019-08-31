package co.anitrend.arch.core.consumer.event

import co.anitrend.arch.core.consumer.SupportObserver

/**
 * An event bus representation of published events
 *
 * @see [SupportObserver]
 */
interface ISupportObserveEvent {
    fun onEventRecieved(consumer: SupportObserver)
}