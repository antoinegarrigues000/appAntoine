package fr.isen.antoine.thegreatestcocktailapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import fr.isen.antoine.thegreatestcocktailapp.ui.theme.TheGreatestCocktailAppTheme

/**
 * Activité principale.
 * Affiche les 4 onglets (Aléatoire, Liste, Favoris, Recherche) via un NavHost.
 * Toutes les transitions vers le détail ou la liste de drinks d'une catégorie
 * passent par des Activities séparées.
 */
class MainActivity : ComponentActivity() {

    private val tag = "Lifecycle"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tag, "onCreate")
        setContent {
            TheGreatestCocktailAppTheme {
                MainScreen()
            }
        }
    }

    override fun onStart()   { super.onStart();   Log.d(tag, "onStart") }
    override fun onResume()  { super.onResume();  Log.d(tag, "onResume") }
    override fun onPause()   { super.onPause();   Log.d(tag, "onPause") }
    override fun onStop()    { super.onStop();    Log.d(tag, "onStop") }
    override fun onRestart() { super.onRestart(); Log.d(tag, "onRestart") }
    override fun onDestroy() { super.onDestroy(); Log.d(tag, "onDestroy") }
}

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    data object Random    : BottomNavItem("random",    Icons.Default.Shuffle,             "Aléatoire")
    data object List      : BottomNavItem("list",      Icons.AutoMirrored.Filled.List,    "Liste")
    data object Favorites : BottomNavItem("favorites", Icons.Default.Favorite,            "Favoris")
    data object Search    : BottomNavItem("search",    Icons.Default.Search,              "Recherche")
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val items = listOf(
        BottomNavItem.Random,
        BottomNavItem.List,
        BottomNavItem.Favorites,
        BottomNavItem.Search
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                items.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Random.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(BottomNavItem.Random.route)    { RandomScreen() }
            composable(BottomNavItem.List.route)      { CategoriesScreen() }
            composable(BottomNavItem.Favorites.route) { FavoritesScreen() }
            composable(BottomNavItem.Search.route)    { SearchScreen() }
        }
    }
}
