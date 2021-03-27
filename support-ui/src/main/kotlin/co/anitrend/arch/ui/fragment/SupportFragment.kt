package co.anitrend.arch.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment
import co.anitrend.arch.ui.common.ILifecycleController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

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
abstract class SupportFragment(
    @MenuRes protected open val inflateMenu: Int = NO_MENU_ITEM,
    @LayoutRes protected open val inflateLayout: Int = NO_LAYOUT_ITEM
) : Fragment(), ILifecycleController, CoroutineScope by MainScope() {

    /**
     * Invoke view model observer to watch for changes, this will be called
     * called in [onViewCreated]
     */
    protected abstract fun setUpViewModelObserver()

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
        setHasOptionsMenu(inflateMenu != NO_MENU_ITEM)
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
        return if (inflateLayout != NO_LAYOUT_ITEM) {
            inflater.inflate(inflateLayout, container, false)
        } else super.onCreateView(inflater, container, savedInstanceState)
    }

    /**
     * Called immediately after [onCreateView] has returned, but before any saved state has been
     * restored in to the view. This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.
     *
     * The fragment's view hierarchy is not however attached to its parent at this point.
     *
     * @param view The View returned by [onCreateView].
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     * saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModelObserver()
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
        if (inflateMenu != NO_MENU_ITEM)
            inflater.inflate(inflateMenu, menu)
    }

    companion object {

        /**
         * Constant value that indicates that no dynamic menu will be inflated for a [CustomView]
         *
         * [NO_MENU_ITEM] has the default value of 0
         */
        const val NO_MENU_ITEM = 0

        /**
         * Constant value that indicates that no dynamic layout will be inflated for a
         * [ISupportFragment] derivative
         *
         * [NO_LAYOUT_ITEM] has the default value of 0
         */
        const val NO_LAYOUT_ITEM = 0
    }
}
