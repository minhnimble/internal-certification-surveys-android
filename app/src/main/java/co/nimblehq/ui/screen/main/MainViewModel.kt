package co.nimblehq.ui.screen.main

import androidx.hilt.lifecycle.ViewModelInject
import co.nimblehq.ui.base.BaseViewModel

class MainViewModel @ViewModelInject constructor(
) : BaseViewModel(), Inputs, Outputs {

    val inputs: Inputs = this
    val outputs: Outputs = this

    init { }
}

interface Inputs { }


interface Outputs { }
