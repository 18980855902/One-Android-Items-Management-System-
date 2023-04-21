package com.example.assignment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.assignment.*
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModelStore

data class Item(val name: String, val id: String?="")
object Items {
    val data = listOf(
        Item("Book", "book"),
        Item("Game", "game"),
        Item("Gift", "gift"),
        Item("Material", "material")
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemsScreen(navController: NavHostController) {
    LazyColumn {
        items(Items.data) { item ->
            ListItem(
                headlineText = { Text(item.name) },
                modifier = Modifier.clickable {
                    item.id?.let { navController.navigate(it) }
                },
                leadingContent = {
                    Icon(Icons.Filled.ThumbUp, contentDescription = null)
                }
            )
            Divider()
        }
    }
}
@Composable
fun ItemsNav(navController: NavHostController, snackbarHostState: SnackbarHostState) {
    NavHost(
        navController = navController,
        startDestination = "items",
    ) {
        composable("items") { ItemsScreen(navController) }
        composable("book") { FeedbookScreen(navController = navController) }
        composable("book/{itemId}") { backStackEntry ->
            BookScreen(id = backStackEntry.arguments?.getString("itemId"),snackbarHostState)
        }
        composable("game") { FeedgameScreen(navController = navController) }
        composable("game/{itemId}") { backStackEntry ->
            GameScreen(id = backStackEntry.arguments?.getString("itemId"),snackbarHostState)
        }
        composable("gift") { FeedgiftScreen(navController = navController) }
        composable("gift/{itemId}") { backStackEntry ->
            GiftScreen(id = backStackEntry.arguments?.getString("itemId"),snackbarHostState)
        }
        composable("material") { FeedmaterialScreen(navController = navController) }
        composable("material/{itemId}") { backStackEntry ->
            MaterialScreen(id = backStackEntry.arguments?.getString("itemId"),snackbarHostState)
        }
    }
}
