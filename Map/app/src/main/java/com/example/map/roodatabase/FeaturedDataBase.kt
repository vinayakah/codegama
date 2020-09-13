package com.example.map.roodatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [FeaturedEntity::class], version = 1)
abstract class FeaturedDataBase : RoomDatabase() {


    abstract val featuredDao: FeaturedDao

    companion object {
        private var INSTANCE: FeaturedDataBase? = null
        fun getInstanceDatabase(context: Context): FeaturedDataBase? {
            if (INSTANCE == null) {
                synchronized(FeaturedDataBase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        FeaturedDataBase::class.java, "featured_database"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}
