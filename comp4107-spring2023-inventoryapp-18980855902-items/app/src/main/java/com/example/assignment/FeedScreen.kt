package com.example.assignment

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil.compose.AsyncImage
import com.example.assignment.KtorClient.getFeeds1
import com.example.assignment.KtorClient.getFeeds2
import com.example.assignment.KtorClient.getFeeds3
import com.example.assignment.KtorClient.getFeeds4
import com.example.assignment.KtorClient.httpClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

//books
@Serializable
data class Feedbook(val _id: String, val title: String, val author: String, val year: String, val isbn: String, val description: String,
                val category: String, val publisher: String, val location: String, val image: String, val remark: String, val type: String,val borrower: String? =null){}
suspend fun getBook(id: String): Feedbook? {
    val response = httpClient.get("http://comp4107.azurewebsites.net/inventory/$id")
    return response.body()
}
@Composable
fun BookScreen(id: String?, snackbarHostState: SnackbarHostState) {
    val book = remember { mutableStateOf<Feedbook?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(id) {
        book.value = id?.let { getBook(it) }
    }

    if(book.value != null){
        Column {
            Button(onClick = {
                coroutineScope.launch {
                    val stringBody: String =
                        KtorClient.borrowpostFeedback(book.value!!._id ?: "null")
                    snackbarHostState.showSnackbar(stringBody)
                }
            }) {
                Text(text = "Borrow")
            }
            Button(onClick = {
                coroutineScope.launch {
                    val stringBody: String =
                        KtorClient.returnpostFeedback(book.value!!._id ?: "null")
                    snackbarHostState.showSnackbar(stringBody)
                }
            }) {
                Text(text = "Return")
            }
            Text("The detail of this book:")
            Text("")
            Text("_id:" + book.value!!._id)
            Text("title:" + book.value!!.title)
            Text("author:" + book.value!!.author)
            Text("year:" + book.value!!.year)
            Text("isbn:" + book.value!!.isbn)
            Text("category:" + book.value!!.category)
            Text("publisher:" + book.value!!.publisher)
            Text("location:" + book.value!!.location)
            Text("image:" + book.value!!.image)
            Text("remark:" + book.value!!.remark)
            Text("type:" + book.value!!.type)
            Text("borrower:"+ book.value!!.borrower)
            Text("description:" + "_id:" + book.value!!.description)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbookScreen(navController: NavHostController) {
    var currentPage by remember { mutableStateOf(1) }
    var feeds by remember { mutableStateOf(emptyList<Feedbook>()) }
    var inputPage by remember { mutableStateOf("") }

    LaunchedEffect(currentPage) {
        val response = getFeeds1(currentPage, 5)
        feeds = response.feeds
    }

    Box(Modifier.fillMaxSize()) {
        LazyColumn {
            items(feeds) { feed ->
                Card(
                    onClick = {
                        navController.navigate("book/${feed._id}")
                    },
                )
                {
                    Column {
                        AsyncImage(
                            model = feed.image,
                            contentDescription = null,
                            modifier = Modifier.size(200.dp)
                        )
                        Box(Modifier.fillMaxSize()) {
                            feed.title?.let { Text(it, Modifier.align(Alignment.Center)) }
                        }
                    }
                }
                Divider()
            }
            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = inputPage,
                        onValueChange = { inputPage = it },
                        label = { Text("Go to page") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                if (currentPage > 1) {
                                    currentPage -= 1
                                }
                            }
                        ) {
                            Text("Previous")
                        }
                        Text("Page $currentPage")
                        Button(
                            onClick = {
                                currentPage += 1
                            }
                        ) {
                            Text("Next")
                        }
                        Button(
                            onClick = {
                                if (inputPage.isNotEmpty()) {
                                    val pageNumber = inputPage.toIntOrNull()
                                    if (pageNumber != null && pageNumber > 0) {
                                        currentPage = pageNumber
                                    } else {
                                        // Show an error message
                                    }
                                }
                            }
                        ) {
                            Text("Go")
                        }
                    }
                }
            }
        }
    }
}

//games
@Serializable
data class Feedgame(val _id: String, val title: String, val image: String, val quantity: Int, val description: String,
                    val category: String, val publisher: String, val location: String,  val remark: String, val type: String,val borrower: String?="")
suspend fun getGame(id: String): Feedgame? {
    val response = httpClient.get("http://comp4107.azurewebsites.net/inventory/$id")
    return response.body()
}
@Composable
fun GameScreen(id: String?, snackbarHostState: SnackbarHostState) {
    val game = remember { mutableStateOf<Feedgame?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(id) {
        game.value = id?.let { getGame(it) }
    }

    if(game.value != null){
        Column {
            Button(onClick = {
                coroutineScope.launch {
                    val stringBody: String =
                        KtorClient.borrowpostFeedback(game.value!!._id ?: "null")
                    snackbarHostState.showSnackbar(stringBody)
                }
            }) {
                Text(text = "Borrow")
            }
            Button(onClick = {
                coroutineScope.launch {
                    val stringBody: String =
                        KtorClient.returnpostFeedback(game.value!!._id ?: "null")
                    snackbarHostState.showSnackbar(stringBody)
                }
            }) {
                Text(text = "Return")
            }
            Text("The detail of this game:")
            Text("")
            Text("_id:"+game.value!!._id)
            Text("title:"+game.value!!.title)
            Text("image:"+game.value!!.image)
            Text("quantity:"+game.value!!.quantity)
            Text("category:"+game.value!!.category)
            Text("publisher:"+game.value!!.publisher)
            Text("location:"+game.value!!.location)
            Text("remark:"+game.value!!.remark)
            Text("type:"+game.value!!.type)
            Text("borrower:"+game.value!!.borrower)
            Text("description:"+"_id:"+game.value!!.description)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedgameScreen(navController: NavHostController) {
    var currentPage by remember { mutableStateOf(1) }
    var feeds by remember { mutableStateOf(emptyList<Feedgame>()) }
    var inputPage by remember { mutableStateOf("") }

    LaunchedEffect(currentPage) {
        val response = getFeeds2(currentPage, 5)
        feeds = response.feeds
    }

    Box(Modifier.fillMaxSize()) {
        LazyColumn {
            items(feeds) { feed ->
                Card(
                    onClick = {
                        navController.navigate("game/${feed._id}")
                    },
                )
                {
                    Column {
                        AsyncImage(
                            model = feed.image,
                            contentDescription = null,
                            modifier = Modifier.size(200.dp)
                        )
                        Box(Modifier.fillMaxSize()) {
                            feed.title?.let { Text(it, Modifier.align(Alignment.Center)) }
                        }
                    }
                }
                Divider()
            }
            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = inputPage,
                        onValueChange = { inputPage = it },
                        label = { Text("Go to page") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                if (currentPage > 1) {
                                    currentPage -= 1
                                }
                            }
                        ) {
                            Text("Previous")
                        }
                        Text("Page $currentPage")
                        Button(
                            onClick = {
                                currentPage += 1
                            }
                        ) {
                            Text("Next")
                        }
                        Button(
                            onClick = {
                                if (inputPage.isNotEmpty()) {
                                    val pageNumber = inputPage.toIntOrNull()
                                    if (pageNumber != null && pageNumber > 0) {
                                        currentPage = pageNumber
                                    } else {
                                        // Show an error message
                                    }
                                }
                            }
                        ) {
                            Text("Go")
                        }
                    }
                }
            }
        }
    }
}

//gifts
@Serializable
data class Feedgift(val _id: String, val title: String, val image: String, val donatedBy: String, val description: String,
                    val category: String,  val amount: Int, val location: String, val unitPrice: Int, val remark: String, val type: String, val remaining: Int ?=0){
}
suspend fun getGift(id: String?): Feedgift? {
    val response = httpClient.get("http://comp4107.azurewebsites.net/inventory/$id")
    return response.body()
}
@Composable
fun GiftScreen(id: String?, snackbarHostState: SnackbarHostState) {
    val gift = remember { mutableStateOf<Feedgift?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(id) {
        gift.value = id?.let { getGift(it) }
    }

    if(gift.value != null){
        Column {
            Button(onClick = {
                coroutineScope.launch {
                    val stringBody: String =
                        KtorClient.consumepostFeedback(gift.value!!._id ?: "null")
                    snackbarHostState.showSnackbar(stringBody)
                }
            }) {
                Text(text = "Consume")
            }
            Text("The detail of this gift:")
            Text("")
            Text("_id:"+gift.value!!._id)
            Text("title:"+gift.value!!.title)
            Text("image:"+gift.value!!.image)
            Text("donatedBy:"+gift.value!!.donatedBy)
            Text("category:"+gift.value!!.category)
            Text("amount:"+gift.value!!.amount)
            Text("location:"+gift.value!!.location)
            Text("unitPrice:"+gift.value!!.unitPrice)
            Text("remark:"+gift.value!!.remark)
            Text("type:"+gift.value!!.type)
            Text("remaining:"+gift.value!!.remaining )
            Text("description:"+"_id:"+gift.value!!.description)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedgiftScreen(navController: NavHostController) {
    var currentPage by remember { mutableStateOf(1) }
    var feeds by remember { mutableStateOf(emptyList<Feedgift>()) }
    var inputPage by remember { mutableStateOf("") }

    LaunchedEffect(currentPage) {
        val response = getFeeds3(currentPage, 5)
        feeds = response.feeds
    }

    Box(Modifier.fillMaxSize()) {
        LazyColumn {
            items(feeds) { feed ->
                Card(
                    onClick = {
                        navController.navigate("gift/${feed._id}")
                    },
                )
                {
                    Column {
                        AsyncImage(
                            model = feed.image,
                            contentDescription = null,
                            modifier = Modifier.size(200.dp)
                        )
                        Box(Modifier.fillMaxSize()) {
                            feed.title?.let { Text(it, Modifier.align(Alignment.Center)) }
                        }
                    }
                }
                Divider()
            }
            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = inputPage,
                        onValueChange = { inputPage = it },
                        label = { Text("Go to page") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                if (currentPage > 1) {
                                    currentPage -= 1
                                }
                            }
                        ) {
                            Text("Previous")
                        }
                        Text("Page $currentPage")
                        Button(
                            onClick = {
                                currentPage += 1
                            }
                        ) {
                            Text("Next")
                        }
                        Button(
                            onClick = {
                                if (inputPage.isNotEmpty()) {
                                    val pageNumber = inputPage.toIntOrNull()
                                    if (pageNumber != null && pageNumber > 0) {
                                        currentPage = pageNumber
                                    } else {
                                        // Show an error message
                                    }
                                }
                            }
                        ) {
                            Text("Go")
                        }
                    }
                }
            }
        }
    }
}

//materials
@Serializable
data class Feedmaterial(val _id: String, val title: String, val image: String, val description: String,
                    val category: String, val quantity: Int, val location: String,  val remark: String, val type: String,val remaining: Int=0){
}
suspend fun getMaterial(id: String): Feedmaterial? {
    val response = httpClient.get("http://comp4107.azurewebsites.net/inventory/$id")
    return response.body()
}
@Composable
fun MaterialScreen(id: String?, snackbarHostState: SnackbarHostState) {
    val material = remember { mutableStateOf<Feedmaterial?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(id) {
        material.value = id?.let { getMaterial(it) }
    }

    if(material.value != null){
        Column {
            Button(onClick = {
                coroutineScope.launch {
                    val stringBody: String =
                        KtorClient.consumepostFeedback(material.value!!._id ?: "null")
                    snackbarHostState.showSnackbar(stringBody)
                }
            }) {
                Text(text = "Consume")
            }
            Text("The detail of this material:")
            Text("")
            Text("_id:"+material.value!!._id)
            Text("title:"+material.value!!.title)
            Text("image:"+material.value!!.image)
            Text("category:"+material.value!!.category)
            Text("quantity:"+material.value!!.quantity)
            Text("location:"+material.value!!.location)
            Text("remark:"+material.value!!.remark)
            Text("type:"+material.value!!.type)
            Text("remaining:"+material.value!!.remaining)
            Text("description:"+"_id:"+material.value!!.description)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedmaterialScreen(navController: NavHostController) {
    var currentPage by remember { mutableStateOf(1) }
    var feeds by remember { mutableStateOf(emptyList<Feedmaterial>()) }
    var inputPage by remember { mutableStateOf("") }

    LaunchedEffect(currentPage) {
        val response = getFeeds4(currentPage, 5)
        feeds = response.feeds
    }

    Box(Modifier.fillMaxSize()) {
        LazyColumn {
            items(feeds) { feed ->
                Card(
                    onClick = {
                        navController.navigate("material/${feed._id}")
                    },
                )
                {
                    Column {
                        AsyncImage(
                            model = feed.image,
                            contentDescription = null,
                            modifier = Modifier.size(200.dp)
                        )
                        Box(Modifier.fillMaxSize()) {
                            feed.title?.let { Text(it, Modifier.align(Alignment.Center)) }
                        }
                    }
                }
                Divider()
            }
            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = inputPage,
                        onValueChange = { inputPage = it },
                        label = { Text("Go to page") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                if (currentPage > 1) {
                                    currentPage -= 1
                                }
                            }
                        ) {
                            Text("Previous")
                        }
                        Text("Page $currentPage")
                        Button(
                            onClick = {
                                currentPage += 1
                            }
                        ) {
                            Text("Next")
                        }
                        Button(
                            onClick = {
                                if (inputPage.isNotEmpty()) {
                                    val pageNumber = inputPage.toIntOrNull()
                                    if (pageNumber != null && pageNumber > 0) {
                                        currentPage = pageNumber
                                    } else {
                                        // Show an error message
                                    }
                                }
                            }
                        ) {
                            Text("Go")
                        }
                    }
                }
            }
        }
    }
}



