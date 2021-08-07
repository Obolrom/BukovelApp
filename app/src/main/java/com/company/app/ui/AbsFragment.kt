package com.company.app.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.company.app.appComponent
import com.company.app.di.AppViewModelFactory
import javax.inject.Inject

abstract class AbsFragment(
    @LayoutRes layout: Int
): Fragment(layout) {

    @Inject
    lateinit var appViewModelFactory: AppViewModelFactory

    protected abstract fun initViewModels()

    protected abstract fun initViews()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.inject(this)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModels()
        initViews()
    }
}