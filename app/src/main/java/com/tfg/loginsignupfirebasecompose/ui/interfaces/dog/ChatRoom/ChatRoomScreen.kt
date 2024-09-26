package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.ChatRoom

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Chat
import com.tfg.loginsignupfirebasecompose.data.collectionsData.Dog
import com.tfg.loginsignupfirebasecompose.data.collectionsData.User
import com.tfg.loginsignupfirebasecompose.navigation.BottomNavItem

@Composable
fun ChatRoomScreen(
    navController: NavHostController,
    viewModel: ChatRoomViewModel = hiltViewModel()
) {
    val chats by viewModel.chats.collectAsState()
    val lastMessages by viewModel.lastMessages.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(modifier = Modifier.padding(8.dp), onClick = {
            navController.navigate(BottomNavItem.Perfil.route)
        }) {
            Text(text = "Go Back")
        }

        Text(
            "Chats",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(8.dp)
        )

        LazyColumn {
            items(chats) { (chat, userDetails, dogDetails) ->
                ChatItem(
                    chat = chat,
                    userDetails = userDetails,
                    dogDetails = dogDetails,
                    lastMessage = lastMessages[chat.chatId] ?: "No messages yet",
                    onChatClicked = { chatId ->
                        navController.navigate("chat/${chat.chatId}")
                    }
                )
            }
        }
    }
}


@Composable
fun ChatItem(
    chat: Chat,
    userDetails: User,
    dogDetails: Dog,
    lastMessage: String,
    onChatClicked: (String) -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onChatClicked(chat.chatId) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            // Mostrar la imagen del usuario
            AsyncImage(
                model = userDetails.profileImageUrl,
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "${userDetails.name} · ${dogDetails.name}",
                    color = Color.Black
                )
                Text(
                    text = lastMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

