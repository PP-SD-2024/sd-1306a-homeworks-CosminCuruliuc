package com.sd.laborator.interfaces

import com.sd.laborator.model.CacheQuery

interface CachingDAO {
    fun createQueryTable()
    fun getCacheResultByQuery(query: String): String?
    fun insertQuery(cache: CacheQuery)
    fun deleteQuery(query: String)
}