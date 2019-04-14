package io.wax911.support.event

import io.wax911.support.consumer.SupportObserver

interface SupportObserverEvent {
    fun onModelChanged(consumer: SupportObserver)
}