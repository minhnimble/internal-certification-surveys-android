package co.nimblehq.data.storage.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import co.nimblehq.data.model.Survey
import io.reactivex.Single

@Dao
interface SurveyDao : BaseDao<Survey> {

    @Query("SELECT * FROM survey")
    fun getAllSurveys(): Single<List<Survey>>

    @Query("DELETE FROM survey")
    fun deleteSurvey()

    @Transaction
    fun refresh(survey: Survey) {
        deleteSurvey()
        insert(survey)
    }
}
