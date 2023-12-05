/**
 * Copyright 2021 AniTrend
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.anitrend.arch.ui.fragment.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntegerRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.extension.ext.attachComponent
import co.anitrend.arch.extension.ext.detachComponent
import co.anitrend.arch.recycler.SupportRecyclerView
import co.anitrend.arch.recycler.adapter.SupportListAdapter
import co.anitrend.arch.recycler.adapter.contract.ISupportAdapter
import co.anitrend.arch.recycler.common.ClickableItem
import co.anitrend.arch.recycler.extensions.isEmpty
import co.anitrend.arch.recycler.paging.legacy.adapter.SupportPagedListAdapter
import co.anitrend.arch.recycler.shared.adapter.SupportLoadStateAdapter
import co.anitrend.arch.ui.fragment.SupportFragment
import co.anitrend.arch.ui.fragment.list.contract.ISupportFragmentList
import co.anitrend.arch.ui.fragment.list.presenter.SupportListPresenter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Core implementation for fragments that rely on paginated/non-paginated data sets
 * using standard [List] collections
 *
 * @since v1.2.0
 * @see SupportFragment
 * @see ISupportFragmentList
 */
abstract class SupportFragmentList<M> : SupportFragment(), ISupportFragmentList<M> {
    protected abstract val listPresenter: SupportListPresenter<M>

    /**
     * Default span size of [IntegerRes] the layout manager will use.
     *
     * @see setRecyclerLayoutManager
     */
    @get:IntegerRes
    protected abstract val defaultSpanSize: Int

    /**
     * Stub to trigger the loading of data, by default this is only called
     * when [supportViewAdapter] has no data in its underlying source.
     *
     * This is called when the fragment reaches it's [onResume] state
     *
     * @see initializeComponents
     */
    abstract fun onFetchDataInitialize()

    override val onRefreshObserver =
        Observer<LoadState> { loadState ->
            listPresenter.onRefreshObserverChanged(loadState)
        }

    override val onNetworkObserver =
        Observer<LoadState> { loadState ->
            listPresenter.onNetworkObserverChanged(this, loadState)
        }

    protected open fun ISupportAdapter<*>.registerFlowListener() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                clickableFlow
                    .debounce(16)
                    .filterIsInstance<ClickableItem.State>()
                    .onEach {
                        if (it.state !is LoadState.Loading) {
                            viewModelState()?.retry()
                        } else {
                            Timber.d("retry -> state is loading? current state: ${it.state}")
                        }
                    }.collect()
            }
        }
    }

    /**
     * Provides a layout manager that should be used by [setRecyclerLayoutManager]
     */
    override fun provideLayoutManager(): RecyclerView.LayoutManager {
        return StaggeredGridLayoutManager(
            resources.getInteger(defaultSpanSize),
            StaggeredGridLayoutManager.VERTICAL,
        )
    }

    /**
     * Sets a layout manager to the recycler view
     */
    override fun setRecyclerLayoutManager(recyclerView: SupportRecyclerView) {
        if (recyclerView.layoutManager == null) {
            recyclerView.layoutManager = provideLayoutManager()
        }
    }

    /**
     * Sets the adapter for the recycler view
     */
    override fun setRecyclerAdapter(recyclerView: SupportRecyclerView) {
        if (recyclerView.adapter == null) {
            val header =
                SupportLoadStateAdapter(resources, stateConfig).apply {
                    registerFlowListener()
                }
            val footer =
                SupportLoadStateAdapter(resources, stateConfig).apply {
                    registerFlowListener()
                }

            if (supportViewAdapter is RecyclerView.Adapter<*>) {
                (supportViewAdapter as RecyclerView.Adapter<*>)
                    .stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }

            recyclerView.adapter =
                supportViewAdapter.withLoadStateHeaderAndFooter(
                    header = header,
                    footer = footer,
                )
        }
    }

    /**
     * Additional initialization to be done in this method, this method will be called in
     * [androidx.fragment.app.FragmentActivity.onCreate].
     *
     * @param savedInstanceState
     */
    override fun initializeComponents(savedInstanceState: Bundle?) {
        attachComponent(supportViewAdapter)
        attachComponent(listPresenter)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                listPresenter.stateLayout.interactionFlow
                    .debounce(16)
                    .filterIsInstance<ClickableItem.State>()
                    .onEach {
                        if (it.state !is LoadState.Loading) {
                            viewModelState()?.retry()
                        } else {
                            Timber.d("state is loading? current state: ${it.state}")
                        }
                    }.collect()
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                if (supportViewAdapter.isEmpty()) {
                    onFetchDataInitialize()
                }
            }
        }
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation). This will be called between
     * [onCreate] and [onActivityCreated].
     *
     * If you return a View from here, you will later be called in
     * [onDestroyView] when the view is being released.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Return the [View] for the fragment's UI, or null.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        listPresenter.onCreateView(this, view)
        return view
    }

    /**
     * Called immediately after [onCreateView] has returned, but before any saved state has been
     * restored in to the view. This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created. The fragment's view hierarchy
     * is not however attached to its parent at this point.
     *
     * @param view The View returned by [.onCreateView].
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModelObserver()
        viewModelState()?.loadState?.observe(viewLifecycleOwner, onNetworkObserver)
        viewModelState()?.refreshState?.observe(viewLifecycleOwner, onRefreshObserver)
    }

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    override fun onRefresh() {
        lifecycleScope.launch {
            viewModelState()?.refresh()
        }
    }

    /**
     * Called when the fragment is no longer attached to its activity.
     * This is called after [onDestroy].
     */
    override fun onDetach() {
        super.onDetach()
        detachComponent(supportViewAdapter)
        detachComponent(listPresenter)
    }

    protected open fun afterPostModelChange(data: Collection<*>?) {
        /*if (data.isNullOrEmpty())
            supportViewAdapter.networkState = NetworkState.Loading
        else
            supportStateLayout?.networkMutableStateFlow?.value = NetworkState.Idle*/

        listPresenter.resetWidgetStates()
    }

    /**
     * Handles post view model result after extraction or processing
     *
     * @param model list holding data
     */
    protected open fun onPostModelChange(model: Collection<M>?) {
        /** Since pagedList is a type of list we check it first */
        when (model) {
            is PagedList -> {
                with(supportViewAdapter as SupportPagedListAdapter) {
                    submitList(model)
                }
            }
            is List -> {
                with(supportViewAdapter as SupportListAdapter) {
                    submitList(model)
                }
            }
            else -> {}
        }

        afterPostModelChange(model)
    }
}
