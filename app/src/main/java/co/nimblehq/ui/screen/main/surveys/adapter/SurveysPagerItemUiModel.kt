package co.nimblehq.ui.screen.main.surveys.adapter

import androidx.annotation.DrawableRes

data class SurveysPagerItemUiModel(
    @DrawableRes val imageDrawable: Int,
    val header: String,
    val description: String
)
