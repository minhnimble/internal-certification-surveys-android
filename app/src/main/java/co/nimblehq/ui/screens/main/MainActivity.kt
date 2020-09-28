package co.nimblehq.ui.screens.main

import android.content.ComponentName
import android.os.Bundle
import androidx.activity.viewModels
import co.nimblehq.R
import co.nimblehq.ui.base.BaseActivity
import co.nimblehq.ui.screens.onboarding.OnboardingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun startOnboardingActivity() {
        intent.component = ComponentName(this, OnboardingActivity::class.java)
        startActivity(intent)
        finish()
    }
}
