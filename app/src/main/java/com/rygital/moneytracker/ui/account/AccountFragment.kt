package com.rygital.moneytracker.ui.account


import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rygital.moneytracker.App
import com.rygital.moneytracker.R
import com.rygital.moneytracker.data.model.database.DetailedTransaction
import com.rygital.moneytracker.injection.components.fragment.AccountFragmentComponent
import com.rygital.moneytracker.ui.base.BaseFragment
import com.rygital.moneytracker.ui.home.OnMenuClickListener
import kotlinx.android.synthetic.main.fragment_account.*
import javax.inject.Inject


class AccountFragment : BaseFragment(), Account.View {

    companion object {
        const val TAG: String = "AccountFragment"
        const val ARG_ACCOUNT_ID = "account_id_argument"

        fun newInstance(accountId: Int): AccountFragment {
            return AccountFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_ACCOUNT_ID, accountId)
                }
            }
        }
    }

    @Inject
    @JvmSuppressWildcards lateinit var presenter: Account.Presenter<Account.View>
    private lateinit var onMenuClickListener: OnMenuClickListener
    @Inject lateinit var adapter: TransactionsAdapter

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (activity !is OnMenuClickListener) {
            throw ClassCastException(context.toString() + " must implement OnMenuClickListener")
        }
        onMenuClickListener = activity as OnMenuClickListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        (App.instance?.componentsHolder?.getComponent(javaClass) as AccountFragmentComponent)
                .inject(this)

        presenter.attachView(this)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.loadData(arguments?.getInt(ARG_ACCOUNT_ID) ?: 0)

        recycler.layoutManager = LinearLayoutManager(context)
        recycler.setHasFixedSize(true)
        recycler.isNestedScrollingEnabled = false
        recycler.adapter = adapter
        checkTransactionsCount()
        ItemTouchHelper(object : SwipeToDeleteCallback(context) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val transactionId = (viewHolder as TransactionsAdapter.ViewHolder).id
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                presenter.deleteTransaction(transactionId)
                checkTransactionsCount()
            }
        }).attachToRecyclerView(recycler)

        toolbar.setNavigationOnClickListener { onMenuClickListener.navigateBack() }
    }

    private fun checkTransactionsCount() {
        if (adapter.itemCount == 0) {
            recycler.visibility = View.GONE
            emptyTransactions.visibility = View.VISIBLE
        } else {
            emptyTransactions.visibility = View.GONE
            recycler.visibility = View.VISIBLE
        }
    }

    override fun showTransactions(transactions: List<DetailedTransaction>) {
        adapter.transactionList = transactions
        checkTransactionsCount()
    }

    override fun openEditTransactionScreen(transaction: DetailedTransaction) {
        onMenuClickListener.openAddTransactionScreen(null, transaction)
    }

    override fun onDestroyView() {
        presenter.detachView()

        super.onDestroyView()
        if (isRemoving) App.instance?.componentsHolder?.releaseComponent(javaClass)
    }

}
