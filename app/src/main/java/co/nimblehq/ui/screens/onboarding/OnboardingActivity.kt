package co.nimblehq.ui.screens.onboarding

import android.os.Bundle
import androidx.activity.viewModels
import co.nimblehq.R
import co.nimblehq.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingActivity : BaseActivity() {

    private val viewModel by viewModels<OnboardingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
    }
}
