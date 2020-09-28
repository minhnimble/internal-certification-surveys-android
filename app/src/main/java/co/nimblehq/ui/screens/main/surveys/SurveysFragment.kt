package co.nimblehq.ui.screens.main.surveys

import androidx.fragment.app.viewModels
import co.nimblehq.R
import co.nimblehq.ui.base.BaseFragment
import co.nimblehq.ui.base.BaseFragmentCallbacks
import co.nimblehq.ui.screens.main.MainNavigator
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
