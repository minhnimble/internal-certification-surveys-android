package co.nimblehq.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import co.nimblehq.data.model.Survey
import co.nimblehq.data.storage.dao.SurveyDao

@Database(
    entities = [
        Survey::class
    ], version = 1
)
abstract class SurveyDatabase : RoomDatabase() {
    abstract fun surveyDao(): SurveyDao
}

const val DATABASE_NAME = "SurveyAppDB"
