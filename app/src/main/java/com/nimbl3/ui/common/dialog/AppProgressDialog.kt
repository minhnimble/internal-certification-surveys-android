package com.nimbl3.ui.common.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.nimbl3.R
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

/**
 * @Details a global progress dialog for showing loading indicator in app
 */
class AppProgressDialog @Inject constructor(@ActivityContext context: Context) : Dialog(context) {

    init {
        window?.setLayout(
            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
            android.view.ViewGroup.LayoutParams.MATCH_PARENT
        )

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_loading_progress, null, false)
        setContentView(view)

        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}