package io.wax911.support.core.consumer.event

import io.wax911.support.core.consumer.SupportObserver

/**
 * An event bus representation of published events
 *
 * @see [SupportObserver]
 */
interface ISupportObserveEvent {
    fun onEventRecieved(consumer: SupportObserver)
}