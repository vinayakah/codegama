package com.example.map.roodatabase

class FeaturedRepository(private val featuredDao: FeaturedDao) {
    val features = featuredDao.getAllFeatured()

    suspend fun insert(featuredEntity: FeaturedEntity){
        featuredDao.insert(featuredEntity)
    }

    suspend fun deleteAll() {
        featuredDao.deleteAll()
    }
    suspend fun delete(featuredEntity: FeaturedEntity){
        featuredDao.delete(featuredEntity)
    }

}