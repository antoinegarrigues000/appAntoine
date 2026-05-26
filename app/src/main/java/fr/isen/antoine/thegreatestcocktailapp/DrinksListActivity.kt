package fr.isen.antoine.thegreatestcocktailapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import fr.isen.antoine.thegreatestcocktailapp.ui.theme.TheGreatestCocktailAppTheme

/**
 * Activité affichée quand l'utilisateur clique sur une catégorie.
 * Récupère la catégorie depuis l'Intent et affiche tous les drinks de cette catégorie.
 */
class DrinksListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val category = intent.getStringExtra("category") ?: "Cocktail"
        setContent {
            TheGreatestCocktailAppTheme {
                DrinksListScreen(category = category, onBack = { finish() })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrinksListScreen(category: String, onBack: () -> Unit) {
    val context = LocalContext.current
    var drinks by remember { mutableStateOf<List<DrinkPreview>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(category) {
        try {
            drinks = NetworkManager.api.getDrinksByCategory(category).drinks ?: emptyList()
        } catch (e: Exception) {
            Toast.makeText(context, "Erreur : ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(category) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { padding ->

        if (isLoading) {
            Box(
                Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(drinks) { drink ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            val intent = Intent(context, DetailCocktailActivity::class.java)
                            intent.putExtra("drinkId", drink.id)
                            context.startActivity(intent)
                        }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = drink.thumb,
                            contentDescription = drink.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(60.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(drink.name, style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}
