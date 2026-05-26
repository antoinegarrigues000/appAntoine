package fr.isen.antoine.thegreatestcocktailapp

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Gère la liste des cocktails favoris dans les SharedPreferences.
 * On sérialise la liste complète des Drink en JSON sous une seule clé.
 */
object FavoritesManager {

    private const val PREFS = "favorites_prefs"
    private const val KEY = "favorite_drinks"

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    /** Lit la liste complète des favoris */
    fun getAll(context: Context): List<Drink> {
        val json = prefs(context).getString(KEY, null) ?: return emptyList()
        return try {
            val type = object : TypeToken<List<Drink>>() {}.type
            Gson().fromJson<List<Drink>>(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    /** Vrai si ce drink id est déjà en favori */
    fun isFavorite(context: Context, id: String): Boolean =
        getAll(context).any { it.id == id }

    /**
     * Bascule le favori : ajoute si absent, retire si présent.
     * Retourne true si le drink est maintenant en favori.
     */
    fun toggle(context: Context, drink: Drink): Boolean {
        val current = getAll(context).toMutableList()
        val existed = current.removeAll { it.id == drink.id }
        if (!existed) current.add(drink)
        prefs(context).edit().putString(KEY, Gson().toJson(current)).apply()
        return !existed
    }
}
