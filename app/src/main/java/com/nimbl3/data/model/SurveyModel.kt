package com.nimbl3.data.model

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "survey")

@Parcelize
data class SurveyModel(
        @PrimaryKey
        @ColumnInfo(name = "survey_id") val surveyId: String = "",
        @ColumnInfo(name = "description") val description: String? = null,
        @ColumnInfo(name = "cover_image_url") val imageURL: String? = null,
        @ColumnInfo(name = "title") val title: String? = null
) : Parcelable
