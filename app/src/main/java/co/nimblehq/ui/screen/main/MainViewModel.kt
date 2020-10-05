package co.nimblehq.ui.screen.main

import androidx.hilt.lifecycle.ViewModelInject
import co.nimblehq.ui.base.BaseViewModel

interface Inputs { }

interface Outputs { }

class MainViewModel @ViewModelInject constructor(
) : BaseViewModel(), Inputs, Outputs {

    val inputs: Inputs = this
    val outputs: Outputs = this

    init { }
}
