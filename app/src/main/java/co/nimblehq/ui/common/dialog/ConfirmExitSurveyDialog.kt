package co.nimblehq.ui.common.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.DialogFragment
import co.nimblehq.R
import kotlinx.android.synthetic.main.dialog_confirm_exit_survey.view.*

class ConfirmExitSurveyDialog(private val confirmCallback: () -> Unit): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(context, R.style.FullScreenDialogStyle)
        dialog.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm_exit_survey, null)
        view.tvConfirmExitSurveyYes.setOnClickListener {
            dismiss()
            confirmCallback.invoke()
        }
        view.tvConfirmExitSurveyCancel.setOnClickListener {
            dismiss()
        }
        dialog.setContentView(view)
        return dialog
    }

}
