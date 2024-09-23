package com.tfg.loginsignupfirebasecompose.data.di

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.tfg.loginsignupfirebasecompose.data.implementations.AuthRepositoryImpl
import com.tfg.loginsignupfirebasecompose.data.implementations.ChatRepositoryImpl
import com.tfg.loginsignupfirebasecompose.data.implementations.DogRepositoryImpl
import com.tfg.loginsignupfirebasecompose.data.implementations.EstablishmentRepositoryImpl
import com.tfg.loginsignupfirebasecompose.data.implementations.MessageRepositoryImpl
import com.tfg.loginsignupfirebasecompose.data.implementations.UserRepositoryImpl
import com.tfg.loginsignupfirebasecompose.domain.repositories.AuthRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.ChatRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.DogRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.EstablishmentRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.MessageRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImpl(auth)
    }

    @Provides
    @Singleton
    fun provideUserRepository(db: FirebaseFirestore): UserRepository {
        return UserRepositoryImpl(db)
    }

    @Provides
    @Singleton
    fun provideEstablishmentRepository(db: FirebaseFirestore): EstablishmentRepository {
        return EstablishmentRepositoryImpl(db)
    }

    @Provides
    @Singleton
    fun provideDogRepository(db: FirebaseFirestore): DogRepository {
        return DogRepositoryImpl(db)
    }
    @Provides
    @Singleton
    fun provideChatRepository(db:FirebaseFirestore): ChatRepository {
        return ChatRepositoryImpl(db)
    }

    @Provides
    @Singleton
    fun provideMessageRepository(db:FirebaseFirestore): MessageRepository {
        return MessageRepositoryImpl(db)
    }

    @Provides
    @Singleton
    fun provideStorageFirestore(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }


}