package fr.isen.antoine.thegreatestcocktailapp

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

/**
 * Écran de détail d'un cocktail.
 * `drink` est nullable pour gérer l'état de chargement.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailCocktailScreen(drink: Drink?) {
    val context = LocalContext.current

    // L'état "est en favori" se met à jour quand le drink change
    var isFavorite by remember(drink?.id) {
        mutableStateOf(drink?.let { FavoritesManager.isFavorite(context, it.id) } ?: false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(drink?.name ?: "Chargement…") },
                actions = {
                    IconButton(
                        onClick = {
                            drink?.let {
                                isFavorite = FavoritesManager.toggle(context, it)
                                val msg = if (isFavorite) "Ajouté aux favoris ❤"
                                          else "Retiré des favoris"
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite
                                          else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favori"
                        )
                    }
                }
            )
        }
    ) { padding ->

        // État de chargement
        if (drink == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        // Contenu réel
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            AsyncImage(
                model = drink.thumb,
                contentDescription = drink.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = drink.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            drink.category?.let {
                Text("Catégorie : $it", style = MaterialTheme.typography.bodyMedium)
            }
            drink.glass?.let {
                Text("Verre : $it", style = MaterialTheme.typography.bodyMedium)
            }
            drink.alcoholic?.let {
                Text("Type : $it", style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(Modifier.height(16.dp))

            // Card des ingrédients
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        "Ingrédients",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    drink.ingredientList().forEach { (ingredient, measure) ->
                        Text("• ${measure.orEmpty()} $ingredient".trim())
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Card de la recette
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        "Recette",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(drink.bestInstructions() ?: "Pas d'instructions disponibles.")
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}
