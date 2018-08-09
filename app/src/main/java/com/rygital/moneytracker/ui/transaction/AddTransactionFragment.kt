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
import com.rygital.moneytracker.*
import com.rygital.moneytracker.data.model.database.Account
import com.rygital.moneytracker.data.model.database.DetailedTransaction
import com.rygital.moneytracker.data.model.database.Transaction
import com.rygital.moneytracker.injection.components.fragment.AddTransactionFragmentComponent
import com.rygital.moneytracker.ui.base.BaseFragment
import com.rygital.moneytracker.ui.home.OnMenuClickListener
import kotlinx.android.synthetic.main.fragment_add_transaction.*
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject

class AddTransactionFragment: BaseFragment(), AddTransaction.View {
    companion object {
        const val TAG: String = "AddTransactionFragment"
        const val ARG_ACCOUNT_ID = "account_id_argument"
        const val ARG_TRANSACTION = "transaction"

        fun newInstance(accountId: Int? = null, transaction: DetailedTransaction? = null): AddTransactionFragment {
            return AddTransactionFragment().apply {
                arguments = Bundle().apply {
                    if (accountId != null)
                        putInt(ARG_ACCOUNT_ID, accountId)
                    if (transaction != null)
                        putParcelable(ARG_TRANSACTION, transaction)
                }
            }
        }
    }

    @Inject @JvmSuppressWildcards lateinit var presenter: AddTransaction.Presenter<AddTransaction.View>
    private lateinit var onMenuClickListener: OnMenuClickListener
    private var initialAccount: Int? = null
    private var editableTransaction: DetailedTransaction? = null

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
        editableTransaction = arguments?.getParcelable(ARG_TRANSACTION)

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

        if (editableTransaction != null) {
            repeatSwitch.visibility = View.GONE
            patternCheckBox.visibility = View.GONE
            toolbar.setTitle(R.string.edit)
            etSum.setText(editableTransaction?.amount?.toPlainString())
            toggleButton.isChecked = editableTransaction?.type == INCOME
        }
        repeatSwitch.setOnCheckedChangeListener{ compoundButton: CompoundButton, isChecked: Boolean ->
            if (isChecked) repeatGroup.visibility = VISIBLE
            else repeatGroup.visibility = GONE
        }
        intervalSpinner.adapter = getAdapter(INTERVALS.map { getString(it) })
        presenter.initNewTransaction()
    }

    override fun setAccountAdapter(list: List<Account>) {
        spinnerAccount.adapter = getAdapter(list)
        if (editableTransaction == null) {
            spinnerAccount.setSelection(initialAccount?: 0)
        } else {
            val index = list.indexOfFirst { it.id == editableTransaction?.accountId }
            spinnerAccount.setSelection(index)
        }
    }

    override fun setCategoryAdapter(list: List<String>) {
        spinnerCategory.adapter = getAdapter(list)
        spinnerCategory.setSelection(editableTransaction?.accountId?.toInt() ?: 0)
    }

    override fun setCurrencyAdapter(list: List<String>, initial: Int) {
        spinnerCurrency.adapter = getAdapter(list)
        spinnerCurrency.setSelection(editableTransaction?.currencyId?.toInt() ?: initial)
    }

    private fun<T> getAdapter(list: List<T>): ArrayAdapter<T> {
        val dataAdapterCategory = ArrayAdapter<T>(context, android.R.layout.simple_spinner_item, list)
        dataAdapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return dataAdapterCategory
    }

    fun save() {
        if (editableTransaction != null) {
            presenter.updateTransaction(Transaction(
                    if (toggleButton.isChecked) INCOME else EXPENSE,
                    BigDecimal(etSum.text.toString()),
                    spinnerCurrency.selectedItemPosition.toLong(),
                    spinnerCategory.selectedItemPosition.toLong(),
                    (spinnerAccount.selectedItem as Account).id,
                    editableTransaction?.date ?: Date(),
                    editableTransaction?.id ?: 0
            ))
        } else {
            if (repeatSwitch.isChecked) {
                presenter.addPeriodicTransaction(
                        toggleButton.isChecked,
                        etSum.text.toString(),
                        spinnerCurrency.selectedItemPosition,
                        spinnerCategory.selectedItemPosition,
                        (spinnerAccount.selectedItem as Account).id.toInt(),
                        intervalSpinner.selectedItemPosition,
                        intervalEditText.text.toString().toInt())
            } else {
                presenter.addNewTransaction(
                        toggleButton.isChecked,
                        etSum.text.toString(),
                        spinnerCurrency.selectedItemPosition,
                        spinnerCategory.selectedItemPosition,
                        (spinnerAccount.selectedItem as Account).id.toInt(),
                        patternCheckBox.isChecked)
            }
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