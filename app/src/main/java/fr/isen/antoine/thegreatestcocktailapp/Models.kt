package fr.isen.antoine.thegreatestcocktailapp

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/** Réponse pour random.php et lookup.php */
data class DrinkResponse(val drinks: List<Drink>?) : Serializable

/** Un cocktail complet (avec ingrédients, mesures, instructions) */
data class Drink(
    @SerializedName("idDrink")           val id: String = "",
    @SerializedName("strDrink")          val name: String = "",
    @SerializedName("strDrinkThumb")     val thumb: String? = null,
    @SerializedName("strCategory")       val category: String? = null,
    @SerializedName("strGlass")          val glass: String? = null,
    @SerializedName("strInstructions")   val instructions: String? = null,
    @SerializedName("strInstructionsFR") val instructionsFr: String? = null,
    @SerializedName("strAlcoholic")      val alcoholic: String? = null,

    @SerializedName("strIngredient1")  val i1: String? = null,
    @SerializedName("strIngredient2")  val i2: String? = null,
    @SerializedName("strIngredient3")  val i3: String? = null,
    @SerializedName("strIngredient4")  val i4: String? = null,
    @SerializedName("strIngredient5")  val i5: String? = null,
    @SerializedName("strIngredient6")  val i6: String? = null,
    @SerializedName("strIngredient7")  val i7: String? = null,
    @SerializedName("strIngredient8")  val i8: String? = null,
    @SerializedName("strIngredient9")  val i9: String? = null,
    @SerializedName("strIngredient10") val i10: String? = null,
    @SerializedName("strIngredient11") val i11: String? = null,
    @SerializedName("strIngredient12") val i12: String? = null,
    @SerializedName("strIngredient13") val i13: String? = null,
    @SerializedName("strIngredient14") val i14: String? = null,
    @SerializedName("strIngredient15") val i15: String? = null,

    @SerializedName("strMeasure1")  val m1: String? = null,
    @SerializedName("strMeasure2")  val m2: String? = null,
    @SerializedName("strMeasure3")  val m3: String? = null,
    @SerializedName("strMeasure4")  val m4: String? = null,
    @SerializedName("strMeasure5")  val m5: String? = null,
    @SerializedName("strMeasure6")  val m6: String? = null,
    @SerializedName("strMeasure7")  val m7: String? = null,
    @SerializedName("strMeasure8")  val m8: String? = null,
    @SerializedName("strMeasure9")  val m9: String? = null,
    @SerializedName("strMeasure10") val m10: String? = null,
    @SerializedName("strMeasure11") val m11: String? = null,
    @SerializedName("strMeasure12") val m12: String? = null,
    @SerializedName("strMeasure13") val m13: String? = null,
    @SerializedName("strMeasure14") val m14: String? = null,
    @SerializedName("strMeasure15") val m15: String? = null
) : Serializable {
    /** Retourne les paires (ingrédient, mesure) non vides */
    fun ingredientList(): List<Pair<String, String?>> {
        val ing = listOf(i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12, i13, i14, i15)
        val mes = listOf(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15)
        return ing.zip(mes)
            .filter { !it.first.isNullOrBlank() }
            .map { it.first!! to it.second?.trim() }
    }

    /** Donne les instructions FR si dispo, sinon EN */
    fun bestInstructions(): String? = instructionsFr ?: instructions
}

/** Réponse pour list.php?c=list */
data class CategoryResponse(val drinks: List<CategoryItem>?)
data class CategoryItem(@SerializedName("strCategory") val name: String)

/** Réponse pour filter.php?c=... */
data class DrinkPreviewResponse(val drinks: List<DrinkPreview>?)
data class DrinkPreview(
    @SerializedName("idDrink")       val id: String,
    @SerializedName("strDrink")      val name: String,
    @SerializedName("strDrinkThumb") val thumb: String?
) : Serializable
