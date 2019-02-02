package io.wax911.sample.view.fragment.list

import android.os.Bundle
import android.view.View
import com.annimon.stream.IntPair
import io.wax911.sample.R
import io.wax911.sample.adapter.recycler.SimpleModelAdapter
import io.wax911.sample.model.BaseModel
import io.wax911.sample.presenter.BasePresenter
import io.wax911.support.fragment.SupportFragmentList
import io.wax911.support.factory.InstanceCreator

class FragmentHistory : SupportFragmentList<BaseModel, BasePresenter, List<BaseModel>>() {

    companion object : InstanceCreator<FragmentHistory, Bundle?>({
        val fragment = FragmentHistory()
        fragment.arguments = it
        fragment
    })

    override fun initPresenter(): BasePresenter = BasePresenter.newInstance(requireContext())

    /**
     * Called to do extra initialization on behalf of the onCreate method using saved instance
     * @see onCreate
     */
    override fun initializeListComponents(savedInstanceState: Bundle?) {
        mColumnSize = R.integer.single_list_size
        supportViewAdapter = SimpleModelAdapter()
    }

    /**
     * Update views or bind a liveData to them
     */
    override fun updateUI() {
        // injectAdapter(R.string.empty_response)
    }

    override fun makeRequest() {

    }

    /**
     * Must provide a string resource from the running application for a retry action button
     */
    override fun retryButtonText(): Int = R.string.action_retry

    /**
     * Called when the data is changed.
     * @param model The new data
     */
    override fun onChanged(model: List<BaseModel>?) =
            onPostModelChange(model, R.string.empty_response)

    /**
     * When the target view from [View.OnClickListener]
     * is clicked from a view holder this method will be called
     *
     * @param target view that has been clicked
     * @param data   the liveData that at the click index
     */
    override fun onItemClick(target: View, data: IntPair<BaseModel>) {

    }

    /**
     * When the target view from [View.OnLongClickListener]
     * is clicked from a view holder this method will be called
     *
     * @param target view that has been long clicked
     * @param data   the liveData that at the long click index
     */
    override fun onItemLongClick(target: View, data: IntPair<BaseModel>) {

    }
}