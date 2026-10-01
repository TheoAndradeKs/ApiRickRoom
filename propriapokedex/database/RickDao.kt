package com.theoandrade.propriapokedex.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RickDao {

    @Query("SELECT * FROM personagens ORDER BY id ASC")
    fun observeAll(): Flow<List<RickEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(personagens: List<RickEntity>)
}