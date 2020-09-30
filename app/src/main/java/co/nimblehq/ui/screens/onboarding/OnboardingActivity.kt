package co.nimblehq.ui.screens.onboarding

import android.os.Bundle
import androidx.activity.viewModels
import co.nimblehq.R
import co.nimblehq.extension.addBlurWithAnimation
import co.nimblehq.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_onboarding.*

interface BlurAnimatable {
    fun animateBlurBackground()
}

@AndroidEntryPoint
class OnboardingActivity : BaseActivity(), BlurAnimatable {

    private val viewModel by viewModels<OnboardingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
    }

    override fun animateBlurBackground() {
        clBackground.addBlurWithAnimation()
    }
}
