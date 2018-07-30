package com.rygital.moneytracker.ui.transaction

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import com.rygital.moneytracker.App
import com.rygital.moneytracker.R
import com.rygital.moneytracker.data.model.Currency
import com.rygital.moneytracker.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_add_transaction.*
import javax.inject.Inject

class AddTransactionFragment: BaseFragment(), AddTransaction.View, View.OnClickListener {
    companion object {
        const val TAG: String = "AddTransactionFragment"
    }

    @Inject @JvmSuppressWildcards lateinit var presenter: AddTransaction.Presenter<AddTransaction.View>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View = inflater.inflate(R.layout.fragment_add_transaction, container, false)

        App.instance?.componentsHolder
                ?.getAddTransactionFragmentComponent()?.inject(this)

        presenter.attachView(this)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setActionBarTitle(R.string.add_transaction)
        btnSave.setOnClickListener(this)

        presenter.initNewTransaction()
    }

    override fun setAccountAdapter(list: List<String>) {
        spinnerAccount.adapter = getAdapter(list)
    }

    override fun setCategoryAdapter(list: List<String>) {
        spinnerCategory.adapter = getAdapter(list)
    }

    override fun setCurrencyAdapter(list: List<Currency>) {
        spinnerCurrency.adapter = getAdapter(list)
    }

    private fun<T> getAdapter(list: List<T>): ArrayAdapter<T> {
        val dataAdapterCategory = ArrayAdapter<T>(context, android.R.layout.simple_spinner_item, list)
        dataAdapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return dataAdapterCategory
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSave -> {
                presenter.addNewTransaction(
                        spinnerAccount.selectedItemPosition,
                        spinnerCategory.selectedItemPosition,
                        spinnerCurrency.selectedItem as Currency,
                        etSum.text.toString())
            }
        }
    }

    override fun close() {
        activity?.onBackPressed()
    }

    override fun onDestroyView() {
        presenter.detachView()

        super.onDestroyView()
        if (isRemoving) App.instance?.componentsHolder?.releaseAddTransactionFragmentComponent()
    }
}