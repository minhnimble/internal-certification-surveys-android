package co.nimblehq.data.storage.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<T>)

    @Update
    fun update(item: T)

    @Update
    fun updateAll(items: List<T>)

    @Delete
    fun delete(item: T)
}
