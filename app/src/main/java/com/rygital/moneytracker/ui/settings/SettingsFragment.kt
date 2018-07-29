package com.rygital.moneytracker.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rygital.moneytracker.App
import com.rygital.moneytracker.R
import com.rygital.moneytracker.ui.base.BaseFragment
import javax.inject.Inject

class SettingsFragment: BaseFragment(), Settings.View {
    companion object {
        const val TAG: String = "SettingsFragment"
    }


    @Inject @JvmSuppressWildcards lateinit var presenter: Settings.Presenter<Settings.View>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View = inflater.inflate(R.layout.fragment_settings, container, false)

        App.instance?.componentsHolder
                ?.getSettingsFragmentComponent()?.inject(this)

        presenter.attachView(this)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setActionBarTitle(R.string.settings)
    }

    override fun onDestroyView() {
        presenter.detachView()

        super.onDestroyView()
        if (isRemoving) App.instance?.componentsHolder?.releaseSettingsFragmentComponent()
    }
}