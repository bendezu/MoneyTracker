package com.rygital.moneytracker.ui.about

import android.os.Bundle
import android.view.*
import com.rygital.moneytracker.App
import com.rygital.moneytracker.R
import com.rygital.moneytracker.injection.components.fragment.AboutFragmentComponent
import com.rygital.moneytracker.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_about.*
import javax.inject.Inject

class AboutFragment: BaseFragment(), About.View {
    companion object {
        const val TAG: String = "AboutFragment"
    }

    @Inject @JvmSuppressWildcards lateinit var presenter: About.Presenter<About.View>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View = inflater.inflate(R.layout.fragment_about, container, false)

        (App.getApp(context!!).componentsHolder?.getComponent(javaClass.kotlin) as AboutFragmentComponent)
                .inject(this)

        presenter.attachView(this)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tvVersion?.text = String.format("ver. %s",
                context?.packageManager?.getPackageInfo(context?.packageName, 0)?.versionName)
    }

    override fun onDestroyView() {
        presenter.detachView()

        super.onDestroyView()
        if (isRemoving) App.getApp(context!!).componentsHolder?.releaseComponent(javaClass.kotlin)
    }
}