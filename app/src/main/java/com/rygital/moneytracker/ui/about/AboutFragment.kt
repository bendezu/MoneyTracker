package com.rygital.moneytracker.ui.about

import android.os.Bundle
import android.view.*
import android.widget.TextView
import com.rygital.moneytracker.App
import com.rygital.moneytracker.R
import com.rygital.moneytracker.ui.base.BaseFragment
import javax.inject.Inject

class AboutFragment: BaseFragment(), About.View {
    companion object {
        const val TAG: String = "AboutFragment"
    }

    @Inject @JvmSuppressWildcards lateinit var presenter: About.Presenter<About.View>

    var tvVersion: TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View = inflater.inflate(R.layout.fragment_about, container, false)

        App.instance?.applicationComponent?.inject(this)

        tvVersion = v.findViewById(R.id.tvVersion)

        presenter.attachView(this)
        init()

        return v
    }

    private fun init() {
        activity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tvVersion?.text = String.format("ver. %s",
                context?.packageManager?.getPackageInfo(context?.packageName, 0)?.versionName)
    }

    override fun onDestroyView() {
        tvVersion = null
        presenter.detachView()

        super.onDestroyView()
    }
}