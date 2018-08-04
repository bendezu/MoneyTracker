package com.rygital.moneytracker.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.rygital.moneytracker.App
import com.rygital.moneytracker.R
import com.rygital.moneytracker.data.model.database.Currency
import com.rygital.moneytracker.injection.components.fragment.SettingsFragmentComponent
import com.rygital.moneytracker.ui.base.BaseFragment
import com.rygital.moneytracker.ui.home.OnMenuClickListener
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject

class SettingsFragment: BaseFragment(), Settings.View {
    companion object {
        const val TAG: String = "SettingsFragment"
    }

    @Inject @JvmSuppressWildcards lateinit var presenter: Settings.Presenter<Settings.View>

    private lateinit var onMenuClickListener: OnMenuClickListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (activity !is OnMenuClickListener) {
            throw ClassCastException(context.toString() + " must implement OnMenuClickListener")
        }

        onMenuClickListener = activity as OnMenuClickListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View = inflater.inflate(R.layout.fragment_settings, container, false)

        (App.instance?.componentsHolder?.getComponent(javaClass) as SettingsFragmentComponent)
                .inject(this)

        presenter.attachView(this)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.initSpinners()

        primaryCurrencySpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                presenter.savePrimaryCurrency(position)
            }
        }
        secondaryCurrencySpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                presenter.saveSecondaryCurrency(position)
            }
        }

        toolbar.setNavigationOnClickListener { onMenuClickListener.navigateBack() }
        about.setOnClickListener { onMenuClickListener.openAboutScreen() }
    }

    override fun initPrimaryCurrencySpinner(list: List<Currency>, initial: Int) {
        primaryCurrencySpinner.adapter = getAdapter(list.map { it.label })
        primaryCurrencySpinner.setSelection(initial)
    }

    override fun initSecondaryCurrencySpinner(list: List<Currency>, initial: Int) {
        secondaryCurrencySpinner.adapter = getAdapter(list.map { it.label })
        secondaryCurrencySpinner.setSelection(initial)
    }

    private fun<T> getAdapter(list: List<T>): ArrayAdapter<T> {
        val dataAdapterCategory = ArrayAdapter<T>(context, android.R.layout.simple_spinner_item, list)
        dataAdapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return dataAdapterCategory
    }

    override fun onDestroyView() {
        presenter.detachView()

        super.onDestroyView()
        if (isRemoving) App.instance?.componentsHolder?.releaseComponent(javaClass)
    }
}