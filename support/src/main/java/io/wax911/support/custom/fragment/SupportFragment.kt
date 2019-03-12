package io.wax911.support.custom.fragment

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.annimon.stream.IntPair
import com.google.android.material.snackbar.Snackbar
import io.wax911.support.base.event.ActionModeListener
import io.wax911.support.base.event.ItemClickListener
import io.wax911.support.custom.action.contract.ISupportActionMode
import io.wax911.support.view.CompatView
import io.wax911.support.custom.presenter.SupportPresenter
import io.wax911.support.custom.viewmodel.SupportViewModel
import io.wax911.support.custom.action.SupportActionMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.greenrobot.eventbus.EventBus
import kotlin.coroutines.CoroutineContext

abstract class SupportFragment<M, P : SupportPresenter<*>, VM> : Fragment(), CoroutineScope, ActionModeListener, CompatView<VM, P>, ItemClickListener<M> {

    private lateinit var job: Job

    @MenuRes
    protected var inflateMenu: Int = 0
    protected var snackBar: Snackbar? = null

    protected val presenter: P by lazy { initPresenter() }
    protected val viewModel: SupportViewModel<VM?, *>? by lazy { initViewModel() }
    protected val supportAction: ISupportActionMode<M> by lazy {
        SupportActionMode<M>(this, presenter)
    }

    /**
     * Called to do initial creation of a fragment.  This is called after
     * [.onAttach] and before
     * [.onCreateView].
     *
     *
     * Note that this can be called while the fragment's activity is
     * still in the process of being created.  As such, you can not rely
     * on things like the activity's content view hierarchy being initialized
     * at this point.  If you want to do work once the activity itself is
     * created, see [.onActivityCreated].
     *
     *
     * Any restored child fragments will be created before the base
     * `Fragment.onCreate` method returns.
     *
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
        retainInstance = true
    }

    /**
     * Called immediately after [.onCreateView]
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment's
     * view hierarchy is not however attached to its parent at this point.
     * @param view The View returned by [.onCreateView].
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    /**
     * Called when the Fragment is visible to the user.  This is generally
     * tied to [Activity.onStart] of the containing
     * Activity's lifecycle.
     */
    override fun onStart() {
        super.onStart()
        if (shouldSubscribe()) {
            if (!EventBus.getDefault().isRegistered(this))
                EventBus.getDefault().register(this)
        }
        if (!shouldDisableMenu())
            setHasOptionsMenu(true)
    }

    /**
     * Called when the Fragment is no longer started.  This is generally
     * tied to [Activity.onStop] of the containing
     * Activity's lifecycle.
     */
    override fun onStop() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this)
        super.onStop()
    }

    override fun onPause() {
        presenter.onPause(this)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume(this)
    }

    /**
     * Called when the fragment is no longer in use.  This is called
     * after [.onStop] and before [.onDetach].
     */
    override fun onDestroy() {
        job.cancel()
        presenter.onDestroy()
        super.onDestroy()
    }

    /**
     * Initialize the contents of the Fragment host's standard options menu.  You
     * should place your menu items in to <var>menu</var>.  For this method
     * to be called, you must have first called [.setHasOptionsMenu].  See
     * [Activity.onCreateOptionsMenu]
     * for more information.
     *
     * @param menu The options menu in which you place your items.
     * @param inflater menu inflater
     * @see .setHasOptionsMenu
     *
     * @see .onPrepareOptionsMenu
     *
     * @see .onOptionsItemSelected
     */
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        if (inflateMenu != 0)
            inflater?.inflate(inflateMenu, menu)
    }

    override fun hasBackPressableAction(): Boolean {
        if (!supportAction.getAllSelectedItems().isEmpty()) {
            supportAction.clearSelection()
            return true
        }
        return super.hasBackPressableAction()
    }

    /**
     * Called when an item is selected or deselected.
     *
     * @param mode The current ActionMode being used
     */
    override fun onSelectionChanged(mode: ActionMode?, count: Int) {

    }

    /**
     * Called when action mode is first created. The menu supplied will be used to
     * generate action buttons for the action mode.
     *
     * @param mode ActionMode being created
     * @param menu Menu used to populate action buttons
     * @return true if the action mode should be created, false if entering this
     * mode should be aborted.
     */
    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
        return false
    }

    /**
     * Called to refresh an action mode's action menu whenever it is invalidated.
     *
     * @param mode ActionMode being prepared
     * @param menu Menu used to populate action buttons
     * @return true if the menu or action mode was updated, false otherwise.
     */
    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
        return false
    }

    /**
     * Called to report a user click on an action button.
     *
     * @param mode The current ActionMode
     * @param item The item that was clicked
     * @return true if this supportActionMode handled the event, false if the standard MenuItem
     * invocation should continue.
     */
    override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
        return false
    }

    /**
     * Called when an action mode is about to be exited and destroyed.
     *
     * @param mode The current ActionMode being destroyed
     */
    override fun onDestroyActionMode(mode: ActionMode) {
        supportAction.clearSelection()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        Log.d(getViewName(), "onSharedPreferenceChanged -> $key | Changed value")
    }

    /**
     * Context of this scope.
     */
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default

    protected fun isAtLeastState(state: Lifecycle.State): Boolean =
            lifecycle.currentState.isAtLeast(state)

    /**
     * When the target view from [View.OnClickListener]
     * is clicked from a view holder this method will be called
     *
     * @param target view that has been clicked
     * @param data   the liveData that at the click index
     */
    override fun onItemClick(target: View, data: IntPair<M>) {

    }

    /**
     * When the target view from [View.OnLongClickListener]
     * is clicked from a view holder this method will be called
     *
     * @param target view that has been long clicked
     * @param data   the liveData that at the long click index
     */
    override fun onItemLongClick(target: View, data: IntPair<M>) {

    }

    override fun getViewName(): String = this.toString()

    /**
     * Called when the data is changed.
     * @param model The new data
     */
    abstract override fun onChanged(model: VM?)
}
