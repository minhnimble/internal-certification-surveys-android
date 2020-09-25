package com.nimbl3.ui.screens.main

import androidx.hilt.lifecycle.ViewModelInject
import com.nimbl3.ui.base.BaseViewModel

class MainViewModel @ViewModelInject constructor(
) : BaseViewModel(), Inputs, Outputs {

    val inputs: Inputs = this
    val outputs: Outputs = this

    init { }
}

interface Inputs { }


interface Outputs { }