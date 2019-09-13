package io.wax911.sample.view.fragment.list

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import co.anitrend.arch.core.viewmodel.SupportViewModel
import co.anitrend.arch.extension.argument
import co.anitrend.arch.ui.fragment.SupportFragmentList
import co.anitrend.arch.ui.recycler.holder.event.ItemClickListener
import co.anitrend.arch.ui.util.SupportStateLayoutConfiguration
import io.wax911.sample.R
import io.wax911.sample.adapter.recycler.ShowAdapter
import io.wax911.sample.core.presenter.CorePresenter
import io.wax911.sample.core.viewmodel.show.ShowViewModel
import io.wax911.sample.data.entitiy.show.ShowEntity
import io.wax911.sample.domain.usecases.show.TraktShowUseCase
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentShowList : SupportFragmentList<ShowEntity, CorePresenter, PagedList<ShowEntity>>() {

    private val pagingMediaPayload
            by argument<TraktShowUseCase.Payload>(PARAM_SHOW_TYPE)

    // we could inject this as a singleton through DI
    override val supportStateConfiguration = SupportStateLayoutConfiguration(
        R.drawable.ic_support_empty_state,
        R.drawable.ic_support_empty_state,
        R.string.supportTextLoading, R.string.action_retry
    )

    override val supportPresenter by inject<CorePresenter>()
    override val supportViewModel by viewModel<ShowViewModel>()

    override val supportViewAdapter =
        ShowAdapter(
            supportPresenter,
            object : ItemClickListener<ShowEntity> {
                /**
                 * When the target view from [View.OnClickListener]
                 * is clicked from a view holder this method will be called
                 *
                 * @param target view that has been clicked
                 * @param data the liveData that at the click index
                 */
                /**
                 * When the target view from [View.OnClickListener]
                 * is clicked from a view holder this method will be called
                 *
                 * @param target view that has been clicked
                 * @param data the liveData that at the click index
                 */
                override fun onItemClick(target: View, data: Pair<Int, ShowEntity?>) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                /**
                 * When the target view from [View.OnLongClickListener]
                 * is clicked from a view holder this method will be called
                 *
                 * @param target view that has been long clicked
                 * @param data the liveData that at the long click index
                 */
                /**
                 * When the target view from [View.OnLongClickListener]
                 * is clicked from a view holder this method will be called
                 *
                 * @param target view that has been long clicked
                 * @param data the liveData that at the long click index
                 */
                override fun onItemLongClick(target: View, data: Pair<Int, ShowEntity?>) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            }
        )

    override val columnSize: Int = R.integer.single_list_size

    /**
     * Invoke view model observer to watch for changes
     */
    override fun setUpViewModelObserver() {
        supportViewModel.model.observe(this, Observer {
            onPostModelChange(it)
        })
    }

    /**
     * Additional initialization to be done in this method, if the overriding class is type of [SupportFragmentList]
     * then this method will be called in [SupportFragmentList.onCreate].
     * invokes this function
     *
     * @see [SupportFragmentList.onCreate]
     * @param
     */
    override fun initializeComponents(savedInstanceState: Bundle?) {

    }

    /**
     * Handles the updating of views, binding, creation or state change, depending on the context
     * [androidx.lifecycle.LiveData] for a given [CompatView] will be available by this point.
     *
     * Check implementation for more details
     */
    override fun onUpdateUserInterface() {

    }

    /**
     * Handles the complex logic required to dispatch network request to [SupportViewModel]
     * which uses [SupportRepository] to either request from the network or database cache.
     *
     * The results of the dispatched network or cache call will be published by the
     * [androidx.lifecycle.LiveData] inside of your [SupportRepository]
     *
     * @see [SupportRepository.publishResult]
     */
    override fun onFetchDataInitialize() {
        pagingMediaPayload?.also {
            supportViewModel(
                parameter = it
            )
        }
    }

    companion object {

        const val PARAM_SHOW_TYPE = "FragmentShowList:PARAM_SHOW_TYPE"

        fun newInstance(bundle: Bundle?): FragmentShowList {
            val fragment = FragmentShowList()
            fragment.arguments = bundle
            return fragment
        }
    }
}