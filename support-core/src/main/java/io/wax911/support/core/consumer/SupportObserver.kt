package io.wax911.support.core.consumer

import android.os.Bundle

/**
 * Observer publisher holder emitting events
 *
 * @see [org.greenrobot.eventbus.EventBus.post]
 */
class SupportObserver {
    val bundle by lazy { Bundle() }
}