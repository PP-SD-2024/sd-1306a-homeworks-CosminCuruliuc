package org.example.services

import com.fasterxml.jackson.databind.util.JSONPObject
import org.example.interfaces.LocationSearchInterface
import org.springframework.stereotype.Service
import java.net.URL
import org.json.JSONObject
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Service
class LocationSearchService : LocationSearchInterface {
    override fun getLocationId (locationName: String): Int {
        // codificare parametru URL (deoarece poate conţine caractere speciale)
        val encodedLocationName = URLEncoder.encode(locationName, StandardCharsets.UTF_8.toString())

        // construire obiect de tip URL
        val locationSearchURL = URL("http://api.weatherapi.com/v1/search.json?key=35d24d2748e94bf7bc2215702241003&q=$encodedLocationName")

        // preluare raspuns HTTP (seface cerere GET şi se preia continutul raspunsului sub forma de text)
        val rawResponse: String = locationSearchURL.readText()

        // parsare obiect JSON
        val responseRootObject = JSONObject("{\"data\":${rawResponse}}")
        val responseContentObject = responseRootObject.getJSONArray("data").takeUnless { it.isEmpty }?.getJSONObject(0)
        return responseContentObject?.getInt("id") ?: -1
    }
}