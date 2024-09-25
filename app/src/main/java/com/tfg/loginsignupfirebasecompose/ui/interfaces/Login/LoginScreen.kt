package com.tfg.loginsignupfirebasecompose.ui.interfaces.Login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.compose.primaryLight

import com.tfg.loginsignupfirebasecompose.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = hiltViewModel()) {
    val navigationEvent by viewModel.navigationEvent.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(navigationEvent) {
        navigationEvent?.let { route ->
            navController.navigate(route)
            viewModel.clearNavigationEvent()
        }
    }

    if (errorMessage != null) {
        AlertDialog(
            onDismissRequest = { viewModel.clearErrorMessage() },
            title = { Text("Error") },
            text = { Text(errorMessage ?: "") },
            confirmButton = {
                Button(
                    onClick = { viewModel.clearErrorMessage() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryLight
                    )
                ) {
                    Text("OK")
                }
            },
        )
    }


        Surface {
            Box(
                modifier = Modifier
                    .fillMaxSize()

            ) {
                Image(
                    painter = painterResource(id = R.drawable.backgroundwelcome),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = null,
                        modifier = Modifier
                            .width(320.dp)
                            .height(240.dp),
                        contentScale = ContentScale.Fit
                    )

                    Text(
                        text = "Log in to your account",
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier
                            .padding(bottom = 32.dp)
                            .padding(top = 8.dp)
                            .fillMaxWidth()
                    )

                    TextField(
                        value = viewModel.email,
                        onValueChange = { viewModel.email = it },
                        label = { Text("Email") },
                        placeholder = { Text("example@gmail.com") },
                        singleLine = true,
                        /*colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.White,
                            unfocusedIndicatorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedPlaceholderColor = Color.White.copy(alpha = 0.5f),
                            cursorColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.White
                        ),*/
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    TextField(
                        value = viewModel.password,
                        onValueChange = { viewModel.password = it },
                        singleLine = true,
                        label = { Text("Password") },
                        placeholder = { Text("") },
                        visualTransformation =
                        if (viewModel.passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            IconButton(onClick = {
                                viewModel.passwordHidden = !viewModel.passwordHidden
                            }) {
                                val visibilityIcon =
                                    if (viewModel.passwordHidden) Icons.Filled.Lock else Icons.Filled.Lock
                                val description =
                                    if (viewModel.passwordHidden) "Show password" else "Hide password"
                                Icon(imageVector = visibilityIcon, contentDescription = description)
                            }
                        },
                        /*colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.White,
                            unfocusedIndicatorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedPlaceholderColor = Color.White.copy(alpha = 0.5f),
                            cursorColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.White
                        ),*/
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    )

                    Text(
                        text = "¿Forgot password?",
                        textAlign = TextAlign.End,
                        color = Color.White,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .padding(top = 8.dp)
                            .fillMaxWidth()
                    )

                    FilledTonalButton(
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .fillMaxWidth(),
                        onClick = { viewModel.login() }
                    ) {
                        Text("Login")
                    }

                    Text(
                        text = "Or continue with",
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(bottom = 32.dp)
                            .padding(top = 8.dp)
                            .fillMaxWidth()
                    )
                    //Sign in with Google and Facebook buttons
                    /*Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                    //Google Button
                        IconButton(
                            onClick = { },
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.google),
                                contentDescription = "Google",
                                tint = Color.Unspecified
                            )
                        }
                        //Facebook Buttons
                        IconButton(
                            onClick = { },
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.facebook),
                                contentDescription = "Facebook",
                                tint = Color.Unspecified
                            )
                        }
                    }*/
                    HorizontalDivider(thickness = 2.dp)
                    TextButton(
                        onClick = {
                            viewModel.onSignUpClick()
                        },
                        modifier = Modifier
                            .padding(bottom = 32.dp)
                            .padding(top = 8.dp)
                    ) {
                        Text(
                            "¿Don´t have an account? Sign in",
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            fontSize = 16.sp,
                        )
                    }

                }
            }
        }



}
