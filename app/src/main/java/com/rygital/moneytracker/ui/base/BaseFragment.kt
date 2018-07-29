package com.rygital.moneytracker.ui.base

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.view.MenuItem

abstract class BaseFragment: Fragment(), MvpView {
    protected var activity: BaseActivity? = null
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is BaseActivity) {
            this.activity = context
        }
    }

    override fun setActionBarTitle(resId: Int) {
        setActionBarTitle(getString(resId))
    }

    override fun setActionBarTitle(message: String) {
        activity?.setActionBarTitle(message)
    }

    override fun showMessage(@StringRes resId: Int) {
        activity?.showMessage(resId)
    }

    override fun showMessage(message: String) {
        activity?.showMessage(message)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> activity?.onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDetach() {
        activity = null
        super.onDetach()
    }
}