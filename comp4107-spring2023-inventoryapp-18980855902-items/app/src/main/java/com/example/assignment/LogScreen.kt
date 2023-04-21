package com.example.assignment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class Credential(val email: String,
                      val password: String)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogScreen(snackbarHostState: SnackbarHostState) {
    val padding = 30.dp
    var message by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var password by remember { mutableStateOf("") }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Email")
        TextField(
            maxLines = 1,
            value = message,
            onValueChange = { message = it }
        )
        Spacer(Modifier.size(padding))

        Text("Password")
        TextField(
            maxLines = 1,
            value = password,
            onValueChange = { password = it }
        )
        Spacer(Modifier.size(padding))

        Button(onClick = {
            coroutineScope.launch {
                val c = Credential(message, password)
                val stringBody: String = KtorClient.logpostFeedback(c)
                snackbarHostState.showSnackbar(stringBody)
            }
        }) {
            Text(text = "Log in")
        }
    }
}