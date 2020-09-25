package com.nimbl3.ui.screens.main.surveys

import androidx.fragment.app.viewModels
import com.nimbl3.R
import com.nimbl3.ui.base.BaseFragment
import com.nimbl3.ui.base.BaseFragmentCallbacks
import com.nimbl3.ui.screens.main.MainNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SurveysFragment: BaseFragment(), BaseFragmentCallbacks {

    @Inject lateinit var navigator: MainNavigator

    private val viewModel by viewModels<SurveysViewModelImpl>()

    override val layoutRes = R.layout.fragment_surveys

    override fun initViewModel() { }

    override fun setupView() { }

    override fun bindViewEvents() { }

    override fun bindViewModel() { }
}
