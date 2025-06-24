package com.example.sbuxapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.foundation.clickable


data class DrinkItem(val id: Int, val title: String, val price: Double, val imgRes: Int)
data class OrderEntry(val item: DrinkItem, var count: Int = 1)
data class CategoryItem(val name: String, val isSelected: Boolean = false)

val coffeeDrinks = listOf(
    DrinkItem(1, "Coffee 1", 50.0, R.drawable.caramel_macchiato),
    DrinkItem(2, "Coffee 2", 32.0, R.drawable.caffe_latte),
    DrinkItem(3, "Coffee 3", 28.0, R.drawable.americano),
    DrinkItem(4, "Coffee 4", 40.0, R.drawable.caffe_mocha)
)

val teaDrinks = listOf(
    DrinkItem(5, "Tea 1", 25.0, R.drawable.caramel_macchiato),
    DrinkItem(6, "Tea 2", 30.0, R.drawable.caffe_latte),
    DrinkItem(7, "Tea 3", 28.0, R.drawable.americano)
)

val frappuccinoDrinks = listOf(
    DrinkItem(8, "Frappuccino 1", 45.0, R.drawable.caramel_macchiato),
    DrinkItem(9, "Frappuccino 2", 48.0, R.drawable.caffe_mocha),
    DrinkItem(10, "Frappuccino 3", 42.0, R.drawable.caffe_latte),
    DrinkItem(11, "Frappuccino 4", 46.0, R.drawable.americano)
)

val foodItems = listOf(
    DrinkItem(12, "Food 1", 35.0, R.drawable.caramel_macchiato),
    DrinkItem(13, "Food 2", 25.0, R.drawable.caffe_latte),
    DrinkItem(14, "Food 3", 30.0, R.drawable.americano)
)

val menuCategories = listOf(
    CategoryItem("All", true),
    CategoryItem("Coffee"),
    CategoryItem("Tea"),
    CategoryItem("Frappuccino"),
    CategoryItem("Food"),
    CategoryItem("Label")
)


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoffeeHouseApp()
        }
    }
}

@Composable
fun MenuSearchResultCard(item: DrinkItem, cart: MutableList<OrderEntry>) {
    val formatter = NumberFormat.getNumberInstance(Locale("in", "ID"))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Item Image
            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.size(60.dp)
            ) {
                Image(
                    painter = painterResource(id = item.imgRes),
                    contentDescription = item.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Item Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Rp${formatter.format((item.price * 1000).toInt())}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00704A)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Add to Cart Button
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val itemInCart = cart.find { it.item.id == item.id }
                if (itemInCart != null && itemInCart.count > 0) {
                    Text(
                        text = itemInCart.count.toString(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }

                Card(
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            val existingItem = cart.find { it.item.id == item.id }
                            if (existingItem != null) {
                                existingItem.count++
                            } else {
                                cart.add(OrderEntry(item, 1))
                            }
                        },
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF00704A))
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add to Cart",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CoffeeHouseApp() {
    var currentTab by remember { mutableStateOf(0) }
    val cart = remember { mutableStateListOf<OrderEntry>() }
    Scaffold(
        bottomBar = {
            NavigationBar {
                val tabs = listOf("Home", "Menu", "Order", "Account")
                val icons = listOf(
                    Icons.Default.Home,
                    Icons.Default.LocalCafe,
                    Icons.Default.ShoppingCart,
                    Icons.Default.Person
                )
                tabs.forEachIndexed { index, label ->
                    NavigationBarItem(
                        selected = currentTab == index,
                        onClick = { currentTab = index },
                        icon = { Icon(icons[index], contentDescription = label) },
                        label = { Text(label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentTab) {
                0 -> WelcomeScreen()
                1 -> DrinkMenuScreen(cart)
                2 -> CartPage(cart)
                3 -> UserProfileScreen()
            }
        }
    }
}

@Composable
fun SplashScreen(onContinue: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF00704A)), // Warna hijau khas Starbucks
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Gambar Logo Starbucks
            Image(
                painter = painterResource(id = R.drawable.logo_starbucks),
                contentDescription = "Starbucks Logo",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // "Welcome to"
            Text(
                "Welcome to",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium
            )

            // "Starbucks"
            Text(
                "Starbucks",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Deskripsi tambahan
            Text(
                "Thanks for downloading,\nthe new Starbucks ID App!",
                color = Color.White,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Tombol "Mulai"
            Button(
                onClick = onContinue,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("Start", color = Color.Black, fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun WelcomeScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }

    // Filter drinks based on search query
    val filteredCoffeeDrinks = if (searchQuery.isBlank()) {
        coffeeDrinks
    } else {
        coffeeDrinks.filter {
            it.title.contains(searchQuery, ignoreCase = true)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        if (!isSearchActive) {
            item {
                // Happy Mondays Banner
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF00704A))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "★ STARBUCKS REWARDS",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "HAPPY",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "MONDAYS",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Get any Grande Frappuccino, 2 poin for drink size upgrade and 1 poin for extra shot",
                                color = Color.White,
                                fontSize = 11.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = { },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                modifier = Modifier.height(32.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    "SEE ALL OFFERS",
                                    color = Color(0xFF00704A),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Image(
                            painter = painterResource(id = R.drawable.caramel_macchiato),
                            contentDescription = "Happy Monday Drinks",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            item {
                // Good Morning Section
                Text(
                    text = "Good Morning",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "How about a coffee break today?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }

        item {
            // Search Bar
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(android.R.drawable.ic_menu_search),
                        contentDescription = "Search",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))

                    if (isSearchActive) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search Menu", color = Color.Gray) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent
                            ),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )

                        IconButton(
                            onClick = {
                                isSearchActive = false
                                searchQuery = ""
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove, // Using Remove as close icon
                                contentDescription = "Close Search",
                                tint = Color.Gray
                            )
                        }
                    } else {
                        Text(
                            text = "Search Menu",
                            color = Color.Gray,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { isSearchActive = true }
                        )
                    }
                }
            }
        }

        if (isSearchActive) {
            // Search Results
            if (searchQuery.isNotBlank()) {
                item {
                    Text(
                        text = "Search Results (${filteredCoffeeDrinks.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                if (filteredCoffeeDrinks.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No results found",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Gray
                            )
                            Text(
                                text = "Try searching for something else",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                    }
                } else {
                    items(filteredCoffeeDrinks) { drink ->
                        SearchResultCard(drink)
                    }
                }
            }
        } else {
            // Normal home content
            if (!isSearchActive) {
                item {
                    // Card Balance
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF00704A))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Card Balance",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Rp100.000",
                                color = Color.White,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                item {
                    // Today Favourite Section
                    Text(
                        text = "Today Favourite",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(bottom = 24.dp)
                    ) {
                        items(coffeeDrinks.take(3)) { drink ->
                            HorizontalDrinkCard(drink)
                        }
                    }
                }

                item {
                    // Popular Section
                    Text(
                        text = "Popular",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(coffeeDrinks) { drink ->
                            HorizontalDrinkCard(drink)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchResultCard(item: DrinkItem) {
    val formatter = NumberFormat.getNumberInstance(Locale("in", "ID"))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = item.imgRes),
                contentDescription = item.title,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Rp${formatter.format((item.price * 1000).toInt())}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00704A)
            )
        }
    }
}

@Composable
fun HorizontalDrinkCard(item: DrinkItem) {
    val formatter = NumberFormat.getNumberInstance(Locale("in", "ID"))

    Card(
        modifier = Modifier
            .width(120.dp)
            .height(160.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = item.imgRes),
                contentDescription = item.title,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Rp${formatter.format((item.price * 1000).toInt())}",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF00704A),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun DrinkCard(item: DrinkItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = item.imgRes),
                contentDescription = item.title,
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun DrinkMenuScreen(cart: MutableList<OrderEntry>) {
    var selectedCategory by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    val categories = remember { menuCategories }

    // Combine all drinks for search
    val allDrinks = coffeeDrinks + teaDrinks + frappuccinoDrinks + foodItems

    // Filter drinks based on search query
    val filteredDrinks = if (searchQuery.isBlank()) {
        emptyList()
    } else {
        allDrinks.filter {
            it.title.contains(searchQuery, ignoreCase = true)
        }
    }

    // Filter drinks by category
    fun getDrinksByCategory(category: String): List<DrinkItem> {
        return when (category) {
            "Coffee" -> coffeeDrinks
            "Tea" -> teaDrinks
            "Frappuccino" -> frappuccinoDrinks
            "Food" -> foodItems
            else -> emptyList()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            // Menu Title
            Text(
                text = "Menu",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        item {
            // Search Bar
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(android.R.drawable.ic_menu_search),
                        contentDescription = "Search",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))

                    if (isSearchActive) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search Menu", color = Color.Gray) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent
                            ),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )

                        IconButton(
                            onClick = {
                                isSearchActive = false
                                searchQuery = ""
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Close Search",
                                tint = Color.Gray
                            )
                        }
                    } else {
                        Text(
                            text = "Search Menu",
                            color = Color.Gray,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { isSearchActive = true }
                        )
                    }
                }
            }
        }

        if (isSearchActive) {
            // Search Results
            if (searchQuery.isNotBlank()) {
                item {
                    Text(
                        text = "Search Results (${filteredDrinks.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                if (filteredDrinks.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 60.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(android.R.drawable.ic_menu_search),
                                contentDescription = "No Results",
                                tint = Color.Gray,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No results found",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Gray
                            )
                            Text(
                                text = "Try searching for something else",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                    }
                } else {
                    items(filteredDrinks) { drink ->
                        MenuSearchResultCard(drink, cart)
                    }
                }
            }
        } else {
            // Category Chips
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    items(categories) { category ->
                        CategoryChip(
                            category = category,
                            isSelected = category.name == selectedCategory,
                            onClick = { selectedCategory = category.name }
                        )
                    }
                }
            }

        // Coffee Section
        if (selectedCategory == "Coffee" || selectedCategory == "All") {
            item {
                Text(
                    text = "Coffee",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    items(coffeeDrinks) { drink ->
                        MenuDrinkCard(drink, cart)
                    }
                }
            }
        }

        // Tea Section
        if (selectedCategory == "Tea" || selectedCategory == "All") {
            item {
                Text(
                    text = "Tea",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    items(teaDrinks) { drink ->
                        MenuDrinkCard(drink, cart)
                    }
                }
            }
        }

        // Frappuccino Section
        if (selectedCategory == "Frappuccino" || selectedCategory == "All") {
            item {
                Text(
                    text = "Frappuccino",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    items(frappuccinoDrinks) { drink ->
                        MenuDrinkCard(drink, cart)
                    }
                }
            }
        }

        // Food Section
        if (selectedCategory == "Food" || selectedCategory == "All") {
            item {
                Text(
                    text = "Food",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    items(foodItems) { drink ->
                        MenuDrinkCard(drink, cart)
                    }
                }
            }
        }
    }
}
}

@Composable
fun CategoryChip(
    category: CategoryItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF00704A) else Color(0xFFF5F5F5)
        )
    ) {
        Text(
            text = category.name,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = if (isSelected) Color.White else Color.Black,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}

@Composable
fun MenuDrinkCard(item: DrinkItem, cart: MutableList<OrderEntry>) {
    val formatter = NumberFormat.getNumberInstance(Locale("in", "ID"))

    Card(
        modifier = Modifier
            .width(140.dp)
            .height(180.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            // Drink Image
            Image(
                painter = painterResource(id = item.imgRes),
                contentDescription = item.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Rp${formatter.format((item.price * 1000).toInt())}",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF00704A),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            // Add to cart button (optional)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val itemInCart = cart.find { it.item.id == item.id }
                if (itemInCart != null && itemInCart.count > 0) {
                    Text(
                        text = itemInCart.count.toString(),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }

                Card(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            val existingItem = cart.find { it.item.id == item.id }
                            if (existingItem != null) {
                                existingItem.count++
                            } else {
                                cart.add(OrderEntry(item, 1))
                            }
                        },
                    shape = RoundedCornerShape(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF00704A))
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CartPage(cart: List<OrderEntry>) {
    val total = cart.sumOf { it.item.price * it.count }
    val formatter = NumberFormat.getNumberInstance(Locale("in", "ID"))

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            // Header with cart icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Shopping Cart",
                    tint = Color(0xFF00704A),
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "My Order",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (cart.isEmpty()) {
            item {
                // Empty cart state
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 60.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Empty Cart",
                        tint = Color.Gray,
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Your cart is empty",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                    Text(
                        "Add some delicious items to get started!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            items(cart) { orderEntry ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Item Image
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.size(60.dp)
                        ) {
                            Image(
                                painter = painterResource(id = orderEntry.item.imgRes),
                                contentDescription = orderEntry.item.title,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // Item Details
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = orderEntry.item.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Qty: ${orderEntry.count}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }

                        // Price
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Rp${formatter.format((orderEntry.item.price * 1000).toInt())}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                            Text(
                                text = "Rp${formatter.format((orderEntry.item.price * 1000 * orderEntry.count).toInt())}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF00704A)
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))

                // Order Summary
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            "Order Summary",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Subtotal (${cart.sumOf { it.count }} items)",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                "Rp${formatter.format((total * 1000).toInt())}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Tax & Fees",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                "Rp${formatter.format((total * 100).toInt())}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Divider line
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Color.Gray.copy(alpha = 0.3f))
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Total",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Rp${formatter.format(((total * 1000) + (total * 100)).toInt())}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF00704A)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Order Button
                Button(
                    onClick = { /* checkout action */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00704A)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Place Order",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun UserProfileScreen() {
    val greenStarbucks = Color(0xFF00704A)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(16.dp)
    ) {
        item {
            // Profile Header Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = greenStarbucks)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile Picture
                    Card(
                        shape = RoundedCornerShape(50.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.size(80.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "Profile Picture",
                            tint = greenStarbucks,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "Irfan Ridhana",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        "Premium Member ⭐",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Member since badge
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.2f)
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            "Member since 2020",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        item {
            // Stats Cards Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Points Card
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "2521",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = greenStarbucks
                        )
                        Text(
                            "Points",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }

                // Orders Card
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "47",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = greenStarbucks
                        )
                        Text(
                            "Orders",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }

                // Favorites Card
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "12",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = greenStarbucks
                        )
                        Text(
                            "Favorites",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        item {
            // Quick Actions Section
            Text(
                "Quick Actions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        item {
            // Quick Actions Grid
            val quickActions = listOf(
                "Edit Profile" to Icons.Default.Edit,
                "Order History" to Icons.Default.History,
                "Settings" to Icons.Default.Settings,
                "Sign Out" to Icons.Default.ExitToApp
            )

            quickActions.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowItems.forEach { (label, icon) ->
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { /* Action */ },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = label,
                                    tint = greenStarbucks,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    label,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    // Add empty space if odd number of items
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))

            // Account Settings Section
            Text(
                "Account Settings",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column {
                    val accountSettings = listOf(
                        "Notifications" to "Manage your notification preferences",
                        "Privacy" to "Control your privacy settings",
                        "Payment Methods" to "Manage your payment options",
                        "Help & Support" to "Get help when you need it"
                    )

                    accountSettings.forEachIndexed { index, (title, subtitle) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { /* Action */ }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    title,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    subtitle,
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.Add, // Using Add as arrow right placeholder
                                contentDescription = "Navigate",
                                tint = Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        if (index < accountSettings.size - 1) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(Color.Gray.copy(alpha = 0.1f))
                                    .padding(horizontal = 16.dp)
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
