package fr.isen.antoine.thegreatestcocktailapp

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

/**
 * Écran de l'onglet "Aléatoire" du NavHost.
 * Appelle random.php au démarrage et affiche le cocktail dans DetailCocktailScreen.
 */
@Composable
fun RandomScreen() {
    val context = LocalContext.current
    var drink by remember { mutableStateOf<Drink?>(null) }

    LaunchedEffect(Unit) {
        try {
            drink = NetworkManager.api.getRandom().drinks?.firstOrNull()
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "Erreur réseau : ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    DetailCocktailScreen(drink = drink)
}
