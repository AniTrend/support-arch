package io.wax911.support.repository.contract

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import io.wax911.support.custom.controller.SupportRequestClient
import kotlinx.coroutines.Deferred

/**
 * Repository contract
 */
interface ISupportRepository<K, V> {

    /**
     * Saves the given model to the database
     * <br/>
     *
     * @param model item which should be saved
     */
    fun save(model : V)

    /**
     * Updates the given model to the database
     * <br/>
     *
     * @param model item which should be updated
     */
    fun update(model : V)

    /**
     * Find any specific items from our database using a key
     */
    fun find(key : K) : V? = null

    /**
     * Find any specific items from our database
     */
    fun find() : V? = null

    /**
     * Deletes the given model from the database
     * <br/>
     *
     * @param model item which should be deleted
     */
    fun delete(model : V)


    /**
     * Sets the given life cycle owner to observe changes in the live data that
     * currently exists in this repository.
     * <br/>
     *
     * @param context any valid life cycle owner such as a FragmentActivity descendant
     * @param observer any observer that shares the same value type as this repository
     */
    fun registerObserver(context: LifecycleOwner, observer: Observer<V?>)


    /**
     * Handles dispatching of network requests to a background thread
     * <br/>
     *
     * @param bundle bundle of parameters for the request
     * @param context any valid context
     */
    fun requestFromNetwork(bundle: Bundle, context: Context?)

    /**
     * Deals with cancellation of any pending or on going operations that the repository is busy with
     */
    fun onCleared()
}