package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.compose.primaryLight
import com.example.compose.surfaceContainerLight
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Message

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    chatId: String,
    navController: NavHostController,
    viewModel: ChatViewModel = hiltViewModel(),
) {
    val messages by viewModel.messages.collectAsState()  // Mensajes actuales
    val newMessageText = remember { mutableStateOf("") }  // Texto del nuevo mensaje
    val currentUser by viewModel.currentUser.collectAsState()
    val currentUserId = viewModel.currentIdUser

    LaunchedEffect(chatId) {
        viewModel.loadMessageIds(chatId)
        viewModel.loadCurrentUser(currentUserId)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Encabezado con el nombre del receptor
        TopAppBar(
            title = { Text(text = currentUser?.name ?: "Chat") },
            navigationIcon = {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        )

        // Lista de mensajes (tipo chat)
        LazyColumn(
            modifier = Modifier.weight(1f),  // El chat ocupará la mayor parte de la pantalla
            reverseLayout = true  // Para que empiece desde abajo, como en WhatsApp
        ) {
            items(messages) { message ->
                ChatMessageBubble(message = message, currentUserId = currentUserId)
            }
        }

        // Caja de texto para escribir un nuevo mensaje
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                modifier = Modifier.weight(1f),
                value = newMessageText.value,
                onValueChange = { newMessageText.value = it },
                placeholder = { Text(text = "Escribe un mensaje...") },
                maxLines = 1,
                /*colors = TextFieldDefaults.textFieldColors(
                    containerColor = surfaceContainerLight
                )*/
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    if (newMessageText.value.isNotEmpty()) {
                        viewModel.sendMessage(newMessageText.value, chatId, currentUserId)
                        newMessageText.value = ""  // Limpiar el campo de texto
                    }
                }
            ) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "Send Message")
            }
        }
    }
}

@Composable
fun ChatMessageBubble(message: Message, currentUserId: String) {
    // El bocadillo sale del lado derecho si el mensaje es del usuario actual, o del izquierdo si es del receptor
    val isCurrentUser = message.senderId == currentUserId
    val backgroundColor = if (isCurrentUser) primaryLight else surfaceContainerLight

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(backgroundColor, shape = RoundedCornerShape(16.dp))
                .padding(12.dp)
        ) {
            Text(text = message.messageContent, color = if (isCurrentUser) Color.White else Color.Black)
        }
    }
}

