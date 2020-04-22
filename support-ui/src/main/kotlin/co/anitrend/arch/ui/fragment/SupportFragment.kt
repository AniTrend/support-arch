package co.anitrend.arch.ui.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import co.anitrend.arch.core.presenter.SupportPresenter
import co.anitrend.arch.extension.LAZY_MODE_UNSAFE
import co.anitrend.arch.ui.action.SupportActionMode
import co.anitrend.arch.ui.action.contract.ISupportActionMode
import co.anitrend.arch.ui.action.event.ActionModeListener
import co.anitrend.arch.ui.common.ISupportActionUp
import co.anitrend.arch.ui.fragment.contract.ISupportFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import timber.log.Timber

/**
 * Core implementation contract for fragments, which automatically retains instance,
 * see [setRetainInstance] for behavior changes
 *
 * @param inflateMenu setting this to anything other than [ISupportFragment.NO_MENU_ITEM]
 * will automatically inflate the given menu in [onCreateOptionsMenu] and set [setHasOptionsMenu]
 * to true.
 * @param inflateLayout settings this to anything other than [ISupportFragment.NO_LAYOUT_ITEM]
 * will inflate the given layout automatically in [onCreateView].
 *
 * @since v0.9.X
 *
 * @see ISupportFragment
 */
abstract class SupportFragment<M, P : SupportPresenter<*>>(
    @MenuRes protected open val inflateMenu: Int = ISupportFragment.NO_MENU_ITEM,
    @LayoutRes protected open val inflateLayout: Int = ISupportFragment.NO_LAYOUT_ITEM
) : Fragment(), ActionModeListener, ISupportFragment<P>, CoroutineScope by MainScope(),
    ISupportActionUp {

    protected val moduleTag = javaClass.simpleName

    protected val supportAction: ISupportActionMode<M> by lazy(LAZY_MODE_UNSAFE) {
        SupportActionMode<M>(
            actionModeListener = this,
            presenter = presenter
        )
    }

    /**
     * Called to do initial creation of a fragment. This is called after
     * [SupportFragment.onAttach] and before [SupportFragment.onCreateView].
     *
     * **N.B.** This can be called while the fragment's activity is still in the process of
     * being created.  As such, you can not rely on things like the activity's content view
     * hierarchy being initialized at this point. If you want to do work once the activity itself
     * is created, use [onActivityCreated]
     *
     * Any restored child fragments will be created before the base [SupportFragment.onCreate]
     * method returns.
     *
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        initializeComponents(savedInstanceState)
        setHasOptionsMenu(inflateMenu != ISupportFragment.NO_MENU_ITEM)
    }

    /**
     * Called to have the fragment instantiate its user interface view. This is optional, and
     * non-graphical fragments can return null. This will be called between
     * [onCreate] & [onActivityCreated].
     *
     * A default View can be returned by calling [Fragment] in your
     * constructor. Otherwise, this method returns null.
     *
     * It is recommended to __only__ inflate the layout in this method and move
     * logic that operates on the returned View to [onViewCreated].
     *
     * If you return a View from here, you will later be called in [onDestroyView]
     * when the view is being released.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container If non-null, this is the parent view that the fragment's UI should be
     * attached to. The fragment should not add the view itself, but this can be used to generate
     * the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Return the View for the fragment's UI, or null.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (inflateLayout != ISupportFragment.NO_LAYOUT_ITEM) {
            inflater.inflate(inflateLayout, container, false)
        } else super.onCreateView(inflater, container, savedInstanceState)
    }

    /**
     * Initialize the contents of the Fragment host's standard options menu. You should place
     * your menu items in to [menu]. For this method to be called, you must have first
     * called [setHasOptionsMenu]. See [SupportFragment.onCreateOptionsMenu] for more information.
     *
     * @param menu The options menu in which you place your items.
     * @param inflater menu inflater
     *
     * @see SupportFragment.setHasOptionsMenu
     * @see SupportFragment.onPrepareOptionsMenu
     * @see SupportFragment.onOptionsItemSelected
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (inflateMenu != ISupportFragment.NO_MENU_ITEM)
            inflater.inflate(inflateMenu, menu)
    }

    override fun hasBackPressableAction(): Boolean {
        if (supportAction.getAllSelectedItems().isNotEmpty()) {
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
        Timber.tag(moduleTag).d("onSelectionChanged(mode: ActionMode?, count: Int) -> count = $count")
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
    override fun onCreateActionMode(mode: ActionMode, menu: Menu)= false

    /**
     * Called to refresh an action mode's action menu whenever it is invalidated.
     *
     * @param mode ActionMode being prepared
     * @param menu Menu used to populate action buttons
     * @return true if the menu or action mode was updated, false otherwise.
     */
    override fun onPrepareActionMode(mode: ActionMode, menu: Menu) = false

    /**
     * Called to report a user click on an action button.
     *
     * @param mode The current ActionMode
     * @param item The item that was clicked
     * @return true if this supportActionMode handled the event, false if the standard MenuItem
     * invocation should continue.
     */
    override fun onActionItemClicked(mode: ActionMode, item: MenuItem) = false

    /**
     * Called when an action mode is about to be exited and destroyed.
     *
     * @param mode The current ActionMode being destroyed
     */
    override fun onDestroyActionMode(mode: ActionMode) {
        supportAction.clearSelection()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        Timber.tag(moduleTag).d("onSharedPreferenceChanged -> $key | Changed value")
    }

    /**
     * Invoke view model observer to watch for changes, this is called in [onViewCreated]
     */
    protected abstract fun setUpViewModelObserver()
}
