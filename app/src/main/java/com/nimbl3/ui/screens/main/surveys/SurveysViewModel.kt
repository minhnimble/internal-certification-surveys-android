package com.nimbl3.ui.screens.main.surveys

import androidx.hilt.lifecycle.ViewModelInject
import com.nimbl3.ui.base.BaseViewModel

abstract class SurveysViewModel : BaseViewModel() {

    interface Input { }
}

class SurveysViewModelImpl @ViewModelInject constructor(
) : SurveysViewModel(), SurveysViewModel.Input { }
