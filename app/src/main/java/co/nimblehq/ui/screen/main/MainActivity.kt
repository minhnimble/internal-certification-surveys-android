package co.nimblehq.ui.screen.main

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import co.nimblehq.R
import co.nimblehq.ui.base.BaseActivity
import co.nimblehq.ui.screen.common.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.shimmer_loading_indicator_main.*
import javax.inject.Inject

interface LoaderAnimatable {
    fun toggleShimmerLoader(shouldShow: Boolean)
}

@AndroidEntryPoint
class MainActivity : BaseActivity(), LoaderAnimatable {

    @Inject
    lateinit var navigator: MainNavigator

    private val mainViewModel by viewModels<MainViewModel>()

    private val userViewModel by viewModels<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        toggleShimmerLoader(true)
        bindViewModel()
    }

    override fun toggleShimmerLoader(shouldShow: Boolean) {
        clMainShimmerContainer.visibility = if (shouldShow) View.VISIBLE else View.GONE
        if (shouldShow) sflMainContainer.startShimmer() else sflMainContainer.stopShimmer()
    }

    private fun bindViewModel() {
        userViewModel.output.error
            .subscribe(::displayError)
            .bindForDisposable()
    }
}
