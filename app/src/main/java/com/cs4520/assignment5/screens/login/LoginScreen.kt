package com.cs4520.assignment5.screens.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cs4520.assignment5.screens.nav.Screen
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

@Composable
fun LoginScreen(navController: NavHostController) {
    val context = LocalContext.current

    // State variables
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var loginState by remember { mutableStateOf<LoginState>(LoginState.Idle) }

    // Side effect for showing toast
    LaunchedEffect(loginState) {
        if (loginState is LoginState.Error) {
            Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
            // Reset the login state to Idle
            loginState = LoginState.Idle
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 50.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            label = { Text("Username") },
            value = username,
            onValueChange = { username = it }
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            label = { Text("Password") },
            value = password,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = { password = it }
        )
        // Space between text inputs and button
        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = {
                // Update the login state based on the validation
                loginState = if (validUser(username, password)) {
                    // Navigate to the product list screen if the user is valid
                    navController.navigate(Screen.ProductList.route)
                    LoginState.Success
                } else {
                    // Set the login state to Error if the user validation fails
                    LoginState.Error
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(50.dp)
        ) {
            Text("Login")
        }
    }
}

// A function to validate user credentials
fun validUser(username: String, password: String): Boolean {
    return username == "admin" && password == "admin"
}

// A simple enum to represent the login state
sealed class LoginState {
    object Idle : LoginState()
    object Success : LoginState()
    object Error : LoginState()
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    val navController = rememberNavController()
    LoginScreen(navController = navController)
}

@Composable
fun MockToast(message: String) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = message,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .background(Color.Black, RoundedCornerShape(10.dp))
                .padding(vertical = 8.dp, horizontal = 16.dp)
        )
    }
}

@Preview
@Composable
fun PreviewMockToast() {
    MockToast("This is a sample Toast message!")
}



