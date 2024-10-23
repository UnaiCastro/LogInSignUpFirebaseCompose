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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
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

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = hiltViewModel()) {
    val navigationEvent by viewModel.navigationEvent.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

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
                val focusRequester = remember { FocusRequester() }
                val focusManager = LocalFocusManager.current

                TextField(
                    value = viewModel.email,
                    onValueChange = { viewModel.email = it },
                    label = { Text("Email") },
                    placeholder = { Text("example@gmail.com") },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        errorIndicatorColor = Color.Red,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    supportingText = {
                        if (viewModel.emailError) {
                            Text(
                                text = "Invalid email format",
                                color = Color.Red,
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }
                )

                TextField(
                    value = viewModel.password,
                    onValueChange = { viewModel.password = it },
                    singleLine = true,
                    label = { Text("Password") },
                    isError = viewModel.passwordError,
                    visualTransformation = if (viewModel.passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
                    trailingIcon = {
                        IconButton(onClick = {
                            viewModel.passwordHidden = !viewModel.passwordHidden
                        }) {
                            val visibilityIcon = if (viewModel.passwordHidden) Icons.Filled.Lock else Icons.Filled.Lock
                            val description = if (viewModel.passwordHidden) "Show password" else "Hide password"
                            Icon(imageVector = visibilityIcon, contentDescription = description)
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        errorIndicatorColor = Color.Red
                    ),

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    supportingText = {
                        if (viewModel.passwordError) {
                            Text(
                                text = "Password cannot be empty",
                                color = Color.Red,
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }
                )

                TextButton(
                    onClick = {
                        viewModel.onForgotPasswordClick()
                    },
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .align(Alignment.End)
                ) {
                    Text(
                        text = "¿Forgot password?",
                        textAlign = TextAlign.End,
                        color = Color.White,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(top = 8.dp)
                    )
                }

                FilledTonalButton(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth(),
                    onClick = { viewModel.login() }
                ) {
                    Text("Log In")
                }

                HorizontalDivider(thickness = 2.dp, modifier = Modifier.padding(top = 16.dp))

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
