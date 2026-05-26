package fr.isen.antoine.thegreatestcocktailapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton qui expose l'API. À utiliser comme : NetworkManager.api.getRandom()
 */
object NetworkManager {

    private const val BASE_URL = "https://www.thecocktaildb.com/api/json/v1/1/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
