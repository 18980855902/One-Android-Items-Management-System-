package com.example.assignment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.assignment.KtorClient.httpClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.launch

suspend fun postFeedback(feedback: String): String {
    return httpClient.get("https://comp4107.azurewebsites.net/inventory?keyword=$feedback").body()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(snackbarHostState: SnackbarHostState) {
    val padding = 30.dp
    var message by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Type in the keyword")
        TextField(
            maxLines = 1,
            value = message,
            onValueChange = { message = it }
        )
        Spacer(Modifier.size(padding))

        Button(onClick = {
            coroutineScope.launch {
                val stringBody: String = postFeedback(message)
                snackbarHostState.showSnackbar(stringBody)
            }
        }) {
            Text(text = "Search")
        }
    }
}