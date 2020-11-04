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

class SubmitSurveyResponsesSuccessDialog(private val callback: EmptyCallback? = null) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(context, R.style.CenterFadeInOutDialogStyle)
        dialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_submit_survey_responses_success, null)
        dialog.setContentView(view)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        Timer("DismissSuccessDialog", false).schedule(DEFAULT_DURATION * 2) {
            dismiss()
            callback?.invoke()
        }
    }
}
