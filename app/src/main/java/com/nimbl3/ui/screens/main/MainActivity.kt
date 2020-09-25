package com.nimbl3.ui.screens.main

import android.content.ComponentName
import android.os.Bundle
import androidx.activity.viewModels
import com.nimbl3.R
import com.nimbl3.ui.base.BaseActivity
import com.nimbl3.ui.screens.onboarding.OnboardingActivity
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
