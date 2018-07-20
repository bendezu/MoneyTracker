package com.rygital.moneytracker.ui.home

import android.os.Bundle
import com.rygital.moneytracker.App
import com.rygital.moneytracker.R
import com.rygital.moneytracker.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

class HomeActivity: BaseActivity(), Home.View {

    @Inject @JvmSuppressWildcards lateinit var presenter: Home.Presenter<Home.View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.instance?.applicationComponent?.inject(this)
        setContentView(R.layout.activity_home)

        presenter.attachView(this)
        init()
    }

    private fun init() {

    }

    override fun showMoneyInRoubles(money: Float) {
        tvRoubles.text = String.format("₽ %.2f", money)
    }

    override fun showMoneyInDollars(money: Float) {
        tvDollars.text = String.format("$ %.2f", money)
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }
}