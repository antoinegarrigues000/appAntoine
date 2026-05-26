package fr.isen.antoine.thegreatestcocktailapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import fr.isen.antoine.thegreatestcocktailapp.ui.theme.TheGreatestCocktailAppTheme

/**
 * Activité affichée quand l'utilisateur clique sur un drink (depuis DrinksList ou Favoris).
 * Récupère le drinkId depuis l'Intent, fait l'appel lookup.php et passe le résultat
 * à DetailCocktailScreen.
 */
class DetailCocktailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val drinkId = intent.getStringExtra("drinkId") ?: ""
        setContent {
            TheGreatestCocktailAppTheme {
                DetailCocktailRoute(drinkId)
            }
        }
    }
}

@Composable
fun DetailCocktailRoute(drinkId: String) {
    val context = LocalContext.current
    var drink by remember { mutableStateOf<Drink?>(null) }

    LaunchedEffect(drinkId) {
        if (drinkId.isBlank()) return@LaunchedEffect
        try {
            drink = NetworkManager.api.getDrinkById(drinkId).drinks?.firstOrNull()
        } catch (e: Exception) {
            Toast.makeText(context, "Erreur : ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    DetailCocktailScreen(drink = drink)
}
