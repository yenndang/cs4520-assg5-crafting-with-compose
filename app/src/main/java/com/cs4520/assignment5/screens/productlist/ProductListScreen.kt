package com.cs4520.assignment5.screens.productlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cs4520.assignment5.R
import com.cs4520.assignment5.models.Product
import com.cs4520.assignment5.viewmodel.ProductViewModel
import com.cs4520.assignment5.utils.Result
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.cs4520.assignment5.api.AppDatabaseSingleton
import com.cs4520.assignment5.api.RetrofitInstance
import com.cs4520.assignment5.repository.ProductRepository
import com.cs4520.assignment5.viewmodel.ProductViewModelFactory


@Composable
fun ProductListScreen(
    viewModel: ProductViewModel = viewModel(
        factory = ProductViewModelFactory(
            ProductRepository(
                RetrofitInstance.api,
                AppDatabaseSingleton.getDatabase(LocalContext.current).productDao(),
                LocalContext.current
            )
        )
    )) {
    val productListState = viewModel.productList.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchProducts()
    }

    when (val result = productListState.value) {
        is Result.Success -> {
            if (result.data.isNotEmpty()) {
                ProductList(products = result.data)
            } else {
                EmptyView()  // Show empty view if data is empty
            }
        }
        is Result.Error -> ErrorView(exception = result.exception)
        Result.Empty -> EmptyView()
        null -> LoadingView()
    }
}


@Composable
fun ProductList(products: List<Product>) {
    LazyColumn(
        contentPadding = PaddingValues(all = 0.dp),  // Adjust or remove padding as needed
        verticalArrangement = Arrangement.spacedBy(0.dp)  // Adjust space between items
    ) {
        items(products) { product ->
            ProductListItem(product = product)
        }
    }
}

@Composable
fun ProductListItem(product: Product) {
    val backgroundColor = when (product) {
        is Product.Food -> Color(0xFFFFD965) // Light Yellow
        is Product.Equipment -> Color(0xFFE06666) // Light Red
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 0.dp),
        elevation = 2.dp,
        backgroundColor = backgroundColor
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            val imageResource = when (product) {
                is Product.Food -> R.drawable.food
                is Product.Equipment -> R.drawable.equipment
            }

            Image(
                painter = painterResource(id = imageResource),
                contentDescription = null,
                contentScale = ContentScale.Crop,  // Add this to scale the image
                modifier = Modifier
                    .size(64.dp)
                    .aspectRatio(1f)  // Maintain a 1:1 aspect ratio
                    .clip(MaterialTheme.shapes.medium)  // Clip to the theme's medium shape
            )
            Spacer(modifier = Modifier.width(15.dp))
            Column {
                Text(text = product.name, style = MaterialTheme.typography.h6)
                // Conditionally display expiry date
                product.expiryDate?.let { expiryDate ->
                    if (expiryDate.isNotEmpty()) {
                        Text(text = "Expires on: $expiryDate", style = MaterialTheme.typography.body2)
                    }
                }
                Text(text = "Price: $${product.price}", style = MaterialTheme.typography.body2)
            }
        }
    }
}


@Composable
fun LoadingView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorView(exception: Exception) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Error: ${exception.localizedMessage}", style = MaterialTheme.typography.h6)
    }
}

@Composable
fun EmptyView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "No products available!", style = MaterialTheme.typography.h6)
    }
}


// Previews for this screen's components
@Preview
@Composable
fun PreviewProductItem() {
    ProductListItem(
        product = Product.Food(
            name = "Sample Product",
            expiryDate = "2024-12-31",
            price = "10.99"
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewLoadingView() {
    LoadingView()
}


@Preview(showBackground = true)
@Composable
fun PreviewErrorView() {
    ErrorView(Exception("This is a sample error message!"))
}

@Preview(showBackground = true)
@Composable
fun PreviewEmptyView() {
    EmptyView()
}


@Preview(showBackground = true)
@Composable
fun PreviewProductList() {
    val sampleProducts = listOf(
        Product.Food(name = "Apple", expiryDate = "2024-01-01", price = "1.99"),
        Product.Equipment(name = "Treadmill", expiryDate = null, price = "999.99"),
        Product.Food(name = "Peach", expiryDate = "2024-01-01", price = "999.99")
    )
    ProductList(products = sampleProducts)
}