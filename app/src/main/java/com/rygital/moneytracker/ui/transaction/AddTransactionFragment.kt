package com.rygital.moneytracker.ui.transaction

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import com.rygital.moneytracker.App
import com.rygital.moneytracker.INTERVALS
import com.rygital.moneytracker.R
import com.rygital.moneytracker.injection.components.fragment.AddTransactionFragmentComponent
import com.rygital.moneytracker.ui.base.BaseFragment
import com.rygital.moneytracker.ui.home.OnMenuClickListener
import kotlinx.android.synthetic.main.fragment_add_transaction.*
import javax.inject.Inject

class AddTransactionFragment: BaseFragment(), AddTransaction.View {
    companion object {
        const val TAG: String = "AddTransactionFragment"
        const val ARG_ACCOUNT_ID = "account_id_argument"

        fun newInstance(accountId: Int): AddTransactionFragment {
            return AddTransactionFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_ACCOUNT_ID, accountId)
                }
            }
        }
    }

    @Inject @JvmSuppressWildcards lateinit var presenter: AddTransaction.Presenter<AddTransaction.View>
    private lateinit var onMenuClickListener: OnMenuClickListener
    private var initialAccount: Int? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (activity !is OnMenuClickListener) {
            throw ClassCastException(context.toString() + " must implement OnMenuClickListener")
        }
        onMenuClickListener = activity as OnMenuClickListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View = inflater.inflate(R.layout.fragment_add_transaction, container, false)

        initialAccount = arguments?.getInt(ARG_ACCOUNT_ID)

        (App.instance?.componentsHolder?.getComponent(javaClass) as AddTransactionFragmentComponent)
                .inject(this)

        presenter.attachView(this)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.inflateMenu(R.menu.add)
        toolbar.setOnMenuItemClickListener {
            save()
            onMenuClickListener.navigateBack()
            true
        }
        toolbar.setNavigationOnClickListener { onMenuClickListener.navigateBack() }
        repeatSwitch.setOnCheckedChangeListener{ compoundButton: CompoundButton, isChecked: Boolean ->
            if (isChecked) repeatGroup.visibility = VISIBLE
            else repeatGroup.visibility = GONE
        }
        intervalSpinner.adapter = getAdapter(INTERVALS.map { getString(it) })
        presenter.initNewTransaction()
    }

    override fun setAccountAdapter(list: List<String>) {
        spinnerAccount.adapter = getAdapter(list)
        spinnerAccount.setSelection(initialAccount ?: 0)
    }

    override fun setCategoryAdapter(list: List<String>) {
        spinnerCategory.adapter = getAdapter(list)
    }

    override fun setCurrencyAdapter(list: List<String>, initial: Int) {
        spinnerCurrency.adapter = getAdapter(list)
        spinnerCurrency.setSelection(initial)
    }

    private fun<T> getAdapter(list: List<T>): ArrayAdapter<T> {
        val dataAdapterCategory = ArrayAdapter<T>(context, android.R.layout.simple_spinner_item, list)
        dataAdapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return dataAdapterCategory
    }

    fun save() {
        if (repeatSwitch.isChecked) {
            presenter.addPeriodicTransaction(
                    toggleButton.isChecked,
                    etSum.text.toString(),
                    spinnerCurrency.selectedItemPosition,
                    spinnerCategory.selectedItemPosition,
                    spinnerAccount.selectedItemPosition,
                    intervalSpinner.selectedItemPosition,
                    intervalEditText.text.toString().toInt())
        } else {
            presenter.addNewTransaction(
                    toggleButton.isChecked,
                    etSum.text.toString(),
                    spinnerCurrency.selectedItemPosition,
                    spinnerCategory.selectedItemPosition,
                    spinnerAccount.selectedItemPosition,
                    patternCheckBox.isChecked)
        }
    }

    override fun close() {
        activity?.onBackPressed()
    }

    override fun onDestroyView() {
        presenter.detachView()

        super.onDestroyView()
        if (isRemoving) App.instance?.componentsHolder?.releaseComponent(javaClass)
    }

}