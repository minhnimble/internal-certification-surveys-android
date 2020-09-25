package com.nimbl3.ui.screens.onboarding

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.nimbl3.R
import com.nimbl3.ui.base.BaseActivity
import com.nimbl3.ui.screens.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingActivity : BaseActivity() {

    private val viewModel by viewModels<OnboardingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
    }

    fun startMainActivity() {
        intent.component = ComponentName(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, OnboardingActivity::class.java)
            context.startActivity(intent)
        }
    }
}
