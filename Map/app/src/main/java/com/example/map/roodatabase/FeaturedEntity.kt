package com.example.map.roodatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "featured_table")
class FeaturedEntity  (
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "address") val address: String?
)