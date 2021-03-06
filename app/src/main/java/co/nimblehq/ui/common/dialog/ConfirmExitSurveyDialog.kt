package co.nimblehq.ui.common.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.DialogFragment
import co.nimblehq.R
import co.nimblehq.lib.EmptyCallback
import kotlinx.android.synthetic.main.dialog_confirm_exit_survey.view.*

class ConfirmExitSurveyDialog(private val confirmCallback: EmptyCallback): DialogFragment() {

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_confirm_exit_survey, null).apply {
            clConfirmExitSurveyDialog.setOnClickListener {
                dismiss()
            }
            tvConfirmExitSurveyCancel.setOnClickListener {
                dismiss()
            }
            tvConfirmExitSurveyYes.setOnClickListener {
                dismiss()
                confirmCallback()
            }
        }
        return Dialog(requireContext(), R.style.FullScreenDialogStyle).apply {
            setContentView(view)
        }
    }
}
