package co.nimblehq.ui.screen.main.surveys

import androidx.hilt.lifecycle.ViewModelInject
import co.nimblehq.ui.base.BaseViewModel

abstract class SurveysViewModel : BaseViewModel() {

    interface Input { }
}

class SurveysViewModelImpl @ViewModelInject constructor(
) : SurveysViewModel(), SurveysViewModel.Input { }