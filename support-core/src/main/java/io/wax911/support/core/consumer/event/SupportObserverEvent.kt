package io.wax911.support.core.consumer.event

import io.wax911.support.core.consumer.SupportObserver

interface SupportObserverEvent {
    fun onModelChanged(consumer: SupportObserver)
}