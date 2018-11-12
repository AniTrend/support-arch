package io.wax911.support.base.event

import io.wax911.support.custom.consumer.SupportObserver

interface SupportEvent {
    fun onModelChanged(consumer: SupportObserver)
}