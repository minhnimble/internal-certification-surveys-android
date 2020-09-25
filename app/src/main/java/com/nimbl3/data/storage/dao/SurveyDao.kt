package com.nimbl3.data.storage.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.nimbl3.data.model.SurveyModel
import io.reactivex.Single

@Dao
interface SurveyDao : BaseDao<SurveyModel> {

    @Query("SELECT * FROM survey LIMIT 1")
    fun getSurvey(): Single<SurveyModel>

    @Query("SELECT * FROM survey")
    fun getAllSurveys(): Single<List<SurveyModel>>

    @Query("DELETE FROM survey")
    fun deleteSurvey()

    @Transaction
    fun refresh(survey: SurveyModel) {
        deleteSurvey()
        insert(survey)
    }
}
