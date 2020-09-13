package com.example.map.roodatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FeaturedDao {
    @Insert
    suspend fun insert(featuredEntity: FeaturedEntity)

    @Delete
    suspend fun delete(featuredEntity: FeaturedEntity)

    //we will call it on showing on recyclerview
    @Query("SELECT * FROM  featured_table")
    fun getAllFeatured() : LiveData<List<FeaturedEntity>>

    @Query("DELETE FROM featured_table")
    suspend fun deleteAll()

}
