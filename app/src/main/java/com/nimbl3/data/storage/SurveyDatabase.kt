package com.nimbl3.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nimbl3.data.model.SurveyModel
import com.nimbl3.data.storage.dao.SurveyDao

@Database(
    entities = [
        SurveyModel::class
    ], version = 1
)
abstract class SurveyDatabase : RoomDatabase() {
    abstract fun surveyDao(): SurveyDao
}

const val DATABASE_NAME = "SurveyDB"
