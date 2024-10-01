package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
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
    val otherUser by viewModel.otherUser.collectAsState() // Detalles del otro usuario en el chat

    LaunchedEffect(chatId) {
        viewModel.loadMessageIds(chatId)
        viewModel.loadCurrentUser(currentUserId)
        viewModel.loadOtherUser(chatId) // Supongo que tienes una función para cargar el otro usuario del chat
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Mostrar la imagen de perfil de la otra persona
                    if (otherUser?.profileImageUrl != null) {
                        AsyncImage(
                            model = otherUser?.profileImageUrl,
                            contentDescription = "Other user profile picture",
                            modifier = Modifier
                                .size(40.dp)  // Tamaño de la imagen
                                .clip(CircleShape)  // Forma circular
                                .border(1.dp, Color.Gray, CircleShape),  // Borde alrededor
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(8.dp))  // Espacio entre la imagen y el nombre
                    }

                    // Mostrar el nombre del otro usuario
                    Text(text = otherUser?.name ?: "Chat")
                }
            },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            reverseLayout = false
        ) {
            items(messages) { message ->
                ChatMessageBubble(message = message, currentUserId = currentUserId)
            }
        }

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
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    if (newMessageText.value.isNotEmpty()) {
                        viewModel.sendMessage(newMessageText.value, chatId, currentUserId)
                        newMessageText.value = ""
                    }
                }
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Send Message")
            }
        }
    }
}

@Composable
fun ChatMessageBubble(message: Message, currentUserId: String) {
    val isCurrentUser = message.senderId == currentUserId
    val backgroundColor = if (isCurrentUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant

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

