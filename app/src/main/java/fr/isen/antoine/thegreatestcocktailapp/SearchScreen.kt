package fr.isen.antoine.thegreatestcocktailapp

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

/**
 * Bonus partie 6 : écran de recherche.
 * Permet de chercher un cocktail par nom ou par ingrédient.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var query by remember { mutableStateOf("") }
    var searchByIngredient by remember { mutableStateOf(false) }
    var results by remember { mutableStateOf<List<DrinkPreview>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text(if (searchByIngredient) "Ingrédient" else "Nom du cocktail") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = {
                    if (query.isBlank()) return@IconButton
                    scope.launch {
                        isLoading = true
                        try {
                            results = if (searchByIngredient) {
                                NetworkManager.api.searchByIngredient(query).drinks ?: emptyList()
                            } else {
                                // searchByName retourne des Drink complets, on les convertit en preview
                                NetworkManager.api.searchByName(query).drinks?.map {
                                    DrinkPreview(it.id, it.name, it.thumb)
                                } ?: emptyList()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Erreur : ${e.message}", Toast.LENGTH_LONG).show()
                        } finally {
                            isLoading = false
                        }
                    }
                }) {
                    Icon(Icons.Default.Search, contentDescription = "Rechercher")
                }
            }
        )

        Spacer(Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Recherche par ingrédient")
            Spacer(Modifier.width(8.dp))
            Switch(
                checked = searchByIngredient,
                onCheckedChange = { searchByIngredient = it }
            )
        }

        Spacer(Modifier.height(16.dp))

        if (isLoading) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Column
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(results) { drink ->
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
