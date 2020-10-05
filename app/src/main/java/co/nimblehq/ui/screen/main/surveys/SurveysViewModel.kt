package co.nimblehq.ui.screen.main.surveys

import androidx.hilt.lifecycle.ViewModelInject
import co.nimblehq.ui.base.BaseViewModel

interface Inputs { }

class SurveysViewModel @ViewModelInject constructor(
) : BaseViewModel(), Inputs {

    val inputs: Inputs = this

    init { }
}

