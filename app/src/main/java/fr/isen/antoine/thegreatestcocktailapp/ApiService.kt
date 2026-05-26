package fr.isen.antoine.thegreatestcocktailapp

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface Retrofit décrivant tous les endpoints de thecocktaildb.
 * Base URL : https://www.thecocktaildb.com/api/json/v1/1/
 */
interface ApiService {

    /** Renvoie un cocktail aléatoire */
    @GET("random.php")
    suspend fun getRandom(): DrinkResponse

    /** Renvoie la liste des catégories */
    @GET("list.php")
    suspend fun getCategories(@Query("c") list: String = "list"): CategoryResponse

    /** Renvoie tous les drinks d'une catégorie (aperçus seulement) */
    @GET("filter.php")
    suspend fun getDrinksByCategory(@Query("c") category: String): DrinkPreviewResponse

    /** Renvoie le détail complet d'un drink à partir de son id */
    @GET("lookup.php")
    suspend fun getDrinkById(@Query("i") id: String): DrinkResponse

    /** Recherche par nom (bonus partie 6) */
    @GET("search.php")
    suspend fun searchByName(@Query("s") name: String): DrinkResponse

    /** Recherche par ingrédient (bonus partie 6) */
    @GET("filter.php")
    suspend fun searchByIngredient(@Query("i") ingredient: String): DrinkPreviewResponse
}
