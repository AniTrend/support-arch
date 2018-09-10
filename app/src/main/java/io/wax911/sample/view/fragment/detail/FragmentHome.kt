package io.wax911.sample.view.fragment.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.wax911.sample.R
import io.wax911.sample.presenter.BasePresenter
import io.wax911.support.custom.fragment.SupportFragment
import io.wax911.support.util.InstanceUtilNoArg

class FragmentHome : SupportFragment<Nothing, BasePresenter, Nothing>()  {

    companion object : InstanceUtilNoArg<SupportFragment<*,*,*>>({ FragmentHome() })

    /**
     * Mandatory presenter initialization
     */
    override fun initPresenter() {
        presenter = BasePresenter.newInstance(requireContext())
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * [.onCreate] and [.onActivityCreated].
     *
     *
     * If you return a View from here, you will later be called in
     * [.onDestroyView] when the view is being released.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Return the View for the fragment's UI, or null.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.custom_recycler_state, container, false)

    /**
     * Update views or bind a mutableLiveData to them
     */
    override fun updateUI() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun makeRequest() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Called when the data is changed.
     * @param model The new data
     */
    override fun onChanged(model: Nothing) {

    }
}