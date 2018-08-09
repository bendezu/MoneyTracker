package com.rygital.moneytracker.ui.addAccount

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rygital.moneytracker.App
import com.rygital.moneytracker.R
import com.rygital.moneytracker.injection.components.fragment.AddAccountFragmentComponent
import com.rygital.moneytracker.ui.base.BaseFragment
import com.rygital.moneytracker.ui.home.OnMenuClickListener
import kotlinx.android.synthetic.main.fragment_add_account.*
import javax.inject.Inject

class AddAccountFragment : BaseFragment(), AddAccount.View {

    companion object {
        const val TAG: String = "AddAccountFragment"
    }

    @Inject
    @JvmSuppressWildcards lateinit var presenter: AddAccount.Presenter<AddAccount.View>
    private lateinit var onMenuClickListener: OnMenuClickListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (activity !is OnMenuClickListener) {
            throw ClassCastException(context.toString() + " must implement OnMenuClickListener")
        }
        onMenuClickListener = activity as OnMenuClickListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_account, container, false)

        (App.instance?.componentsHolder?.getComponent(javaClass) as AddAccountFragmentComponent)
                .inject(this)

        presenter.attachView(this)

        return view
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
    }

    private fun save() {
        val icon = when (iconGroup.checkedRadioButtonId) {
            R.id.ic_account_circle -> R.drawable.ic_account_circle
            R.id.ic_credit_card -> R.drawable.ic_credit_card
            R.id.ic_https -> R.drawable.ic_https
            R.id.ic_business_center -> R.drawable.ic_business_center
            R.id.ic_cash -> R.drawable.ic_cash
            R.id.ic_bank -> R.drawable.ic_bank
            R.id.ic_monetization -> R.drawable.ic_monetization
            else -> throw IllegalStateException("Radio button not found")
        }
        presenter.addAccount(nameEditText.text.toString(), icon)
    }

    override fun onDestroyView() {
        presenter.detachView()

        super.onDestroyView()
        if (isRemoving) App.instance?.componentsHolder?.releaseComponent(javaClass)
    }
}