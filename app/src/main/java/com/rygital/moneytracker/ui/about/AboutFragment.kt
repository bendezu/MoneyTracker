package com.rygital.moneytracker.ui.about

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.rygital.moneytracker.App
import com.rygital.moneytracker.R
import com.rygital.moneytracker.injection.components.fragment.AboutFragmentComponent
import com.rygital.moneytracker.ui.base.BaseFragment
import com.rygital.moneytracker.ui.home.OnMenuClickListener
import kotlinx.android.synthetic.main.fragment_about.*
import javax.inject.Inject


class AboutFragment: BaseFragment(), About.View {
    companion object {
        const val TAG: String = "AboutFragment"
    }

    @Inject @JvmSuppressWildcards lateinit var presenter: About.Presenter<About.View>

    private lateinit var onMenuClickListener: OnMenuClickListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (activity !is OnMenuClickListener) {
            throw ClassCastException(context.toString() + " must implement OnMenuClickListener")
        }

        onMenuClickListener = activity as OnMenuClickListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View = inflater.inflate(R.layout.fragment_about, container, false)

        (App.instance?.componentsHolder?.getComponent(javaClass) as AboutFragmentComponent)
                .inject(this)

        presenter.attachView(this)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        icon.setOnClickListener { rotateLogo() }

        tvVersion?.text = String.format("ver. %s",
                context?.packageManager?.getPackageInfo(context?.packageName, 0)?.versionName)
        toolbar.setNavigationOnClickListener { onMenuClickListener.navigateBack() }
    }

    fun rotateLogo() {
        val animation = AnimationUtils.loadAnimation(context, R.anim.rotation)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                icon.isClickable = false
            }
            override fun onAnimationEnd(animation: Animation) {
                icon.isClickable = true
            }
            override fun onAnimationRepeat(animation: Animation) {}
        })
        icon.startAnimation(animation)
    }

    override fun onDestroyView() {
        presenter.detachView()

        super.onDestroyView()
        if (isRemoving) App.instance?.componentsHolder?.releaseComponent(javaClass)
    }
}