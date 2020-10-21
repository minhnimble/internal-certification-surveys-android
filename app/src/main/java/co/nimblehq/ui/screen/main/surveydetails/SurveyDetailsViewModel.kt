package co.nimblehq.ui.screen.main.surveydetails

import androidx.hilt.lifecycle.ViewModelInject
import co.nimblehq.ui.base.BaseViewModel

interface Inputs { }

class SurveyDetailsViewModel @ViewModelInject constructor(
    // TODO: Add needed use cases here
) : BaseViewModel(), Inputs {

    val inputs: Inputs = this
}

