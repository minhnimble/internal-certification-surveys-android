package co.nimblehq.ui.common.dialog

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import co.nimblehq.R
import co.nimblehq.data.lib.common.DEFAULT_DURATION
import co.nimblehq.lib.EmptyCallback
import java.util.*
import kotlin.concurrent.schedule

private const val DISMISS_SUCCESS_DIALOG_TIMER_NAME = "DismissSuccessDialog"

class SubmitSurveyResponsesSuccessDialog(private val callback: EmptyCallback? = null) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_submit_survey_responses_success, null)
        return Dialog(context, R.style.CenterFadeInOutDialogStyle).apply {
            window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
            setContentView(view)
        }
    }

    override fun onStart() {
        super.onStart()
        Timer(DISMISS_SUCCESS_DIALOG_TIMER_NAME, false).schedule(DEFAULT_DURATION * 2) {
            dismiss()
            callback?.invoke()
        }
    }
}
