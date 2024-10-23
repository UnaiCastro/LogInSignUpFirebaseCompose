# Firebase Authentication and Dog Adoption App with Jetpack Compose

This project is a mobile application that allows users to sign up, log in, and adopt dogs through Firebase Authentication and Firestore. It features real-time communication between users to facilitate the dog adoption process. The app follows modern development practices, including Clean Architecture, MVVM (Model-View-ViewModel) design pattern, and dependency injection with Dagger Hilt. The user data is stored in Firebase Firestore.

## Table of Contents
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
  - [1. Create a Firebase Project](#1-create-a-firebase-project)
  - [2. Link Firebase with the Code](#2-link-firebase-with-the-code)
  - [3. Configure Firebase Authentication and Firestore](#3-configure-firebase-authentication-and-firestore)
  - [4. Run the Project](#4-run-the-project)
- [Project Structure](#project-structure)
- [Screens](#screens)
  - [1. Login Screen](#1-login-screen)
  - [2. Sign Up Screen](#2-sign-up-screen)
  - [3. Dog Adoption Screen](#3-dog-adoption-screen)
  - [4. Chat Screen](#4-chat-screen)
- [Explanation of Key Concepts](#explanation-of-key-concepts)
  - [1. Clean Architecture](#1-clean-architecture)
  - [2. ViewModel](#2-viewmodel)
  - [3. Firebase Authentication & Firestore](#3-firebase-authentication--firestore)
  - [4. Coroutines and Data Handling](#4-coroutines-and-data-handling)

---

## Technologies Used

- **Kotlin**
- **Jetpack Compose**
- **Firebase Authentication**
- **Firebase Firestore**
- **Dagger Hilt**
- **Coroutines**
- **MVVM**
- **Clean Architecture**

---

## Getting Started

### 1. Create a Firebase Project

1. Visit [Firebase Console](https://console.firebase.google.com/).
2. Click on "Add Project" and follow the steps to create a new project.
3. Once created, click on "Add Android App" and provide the package name of your application.
4. Download the `google-services.json` file and place it in the `app` folder of your Android project.

### 2. Link Firebase with the Code

1. Open the `libs.versions.toml` file, usually found in the `gradle/` folder. Include the following versions and dependencies:

    ```toml
    [versions]
    firebase-bom = "32.1.0"
    google-services = "4.3.15"
    
    [plugins]
    google-services = { id = "com.google.gms.google-services" }

    [libraries]
    firebase-auth = { module = "com.google.firebase:firebase-auth" }
    firebase-firestore = { module = "com.google.firebase:firebase-firestore" }
    ```

2. Open the `build.gradle` file in the `app` module and ensure dependencies are included as per the `libs.versions.toml` aliases:

    ```gradle
    plugins {
        id 'com.android.application'
        id 'kotlin-android'
        id libs.plugins.google-services
    }

    dependencies {
        implementation platform(libs.firebase-bom)
        implementation libs.firebase-auth
        implementation libs.firebase-firestore
    }
    ```

3. Sync the project with Gradle.

### 3. Configure Firebase Authentication and Firestore

1. In the Firebase Console, go to the "Authentication" section and enable email/password authentication.
2. Navigate to "Firestore Database" and create a database in test mode.

### 4. Run the Project

1. Make sure your Android device or emulator is connected.
2. Build and run the project using Android Studio.

---

## Project Structure

The project follows the **Clean Architecture** principles and is structured into different layers to promote separation of concerns:

1. **Domain Layer**: Contains business logic and use cases.
2. **Data Layer**: Handles interaction with Firebase and Firestore, and contains repositories for data access.
3. **Presentation Layer**: Contains the ViewModels and UI components, developed using Jetpack Compose.

---

## Screens

### 1. Login Screen
- **Functionality**: Allows users to log in using email and password authentication. Validates input and interacts with Firebase Authentication for login.
- **Technology**: Firebase Authentication, ViewModel, Jetpack Compose.

### 2. Sign Up Screen
- **Functionality**: Users can register by providing their details such as name, email, and password. User data is stored in Firebase Firestore.
- **Technology**: Firebase Authentication, Firebase Firestore, Dagger Hilt, ViewModel, Jetpack Compose.

### 3. Dog Adoption Screen
- **Functionality**: Displays a list of dogs available for adoption. Users can view details and mark dogs for adoption.
- **Technology**: Firebase Firestore, ViewModel, Jetpack Compose.

### 4. Chat Screen
- **Functionality**: Users can communicate with each other to facilitate dog adoption. Real-time messaging is enabled via Firebase Firestore.
- **Technology**: Firebase Firestore, Coroutines, ViewModel, Jetpack Compose.

### 5. Profile Screen
- **Functionality**: Allows users to modify their profile information such as name, email, and profile picture. Changes are synced with Firebase Firestore to update the user’s data.
- **Technology**: Firebase Firestore, Firebase Storage (for profile pictures), ViewModel, Jetpack Compose.

### 6. Shared Dogs Screen
- **Functionality**: Displays a list of dogs that users have shared publicly for adoption. Other users can view details about these dogs and initiate conversations for adoption.
- **Technology**: Firebase Firestore, ViewModel, Jetpack Compose.

### 7. Liked Dogs Screen
- **Functionality**: Shows a list of dogs that the user has liked or marked as favorites. The user can easily track dogs they are interested in adopting.
- **Technology**: Firebase Firestore, ViewModel, Jetpack Compose.

### 8. Upload Dog Screen
- **Functionality**: Allows users to upload information about a dog for adoption, including the dog's picture, name, breed, and other details. The information is stored in Firebase Firestore, and the images are uploaded to Firebase Storage.
- **Technology**: Firebase Firestore, Firebase Storage, ViewModel, Jetpack Compose.


---

## Explanation of Key Concepts

### 1. Clean Architecture

The app follows the **Clean Architecture** approach, which separates the application into layers:

- **Domain Layer**: Contains the business rules and is independent of any UI or external service. Use cases are defined here to interact with the repositories.
- **Data Layer**: Contains implementations of repositories and handles data access (Firebase Firestore and Authentication).
- **Presentation Layer**: Contains the UI components and ViewModels that interact with the use cases to provide data to the views.

This architecture ensures high modularity, making the application easier to maintain and test.

### 2. ViewModel

The **ViewModel** is used to manage and store UI-related data in a lifecycle-aware manner. In this project, each screen has its own ViewModel to handle logic related to the screen. The ViewModel interacts with the domain layer to fetch and update data without being tied to the lifecycle of activities or fragments.

By separating the UI logic from the data layer, the app becomes more testable and maintainable.

### 3. Firebase Authentication & Firestore

- **Firebase Authentication**: The app allows users to sign up and log in using email and password. The `AuthRepository` abstracts Firebase calls to keep the business logic separate from the Firebase API.
- **Firestore Database**: User information, such as profile details and dogs available for adoption, is stored in **Firestore**. The app uses **real-time updates** to keep users informed of new adoption opportunities and chat messages.

### 4. Coroutines and Data Handling

**Coroutines** are used for making asynchronous calls to Firebase without blocking the main thread. This ensures that the app remains responsive when performing network operations, such as querying Firestore or authenticating users.

The app separates the Firebase calls from the ViewModel using repositories, which handle the logic of fetching and saving data. By doing this, the ViewModel remains focused on UI logic, while the data access is encapsulated in the repositories.

---

# Proyecto de Autenticación en Firebase y Adopción de Perros con Jetpack Compose

Este proyecto es una aplicación móvil que permite a los usuarios registrarse, iniciar sesión y adoptar perros a través de Firebase Authentication y Firestore. Incluye comunicación en tiempo real entre los usuarios para facilitar el proceso de adopción de perros. La app sigue las prácticas modernas de desarrollo, incluyendo Arquitectura Limpia, el patrón de diseño MVVM (Model-View-ViewModel) y la inyección de dependencias con Dagger Hilt. Los datos de usuario se almacenan en Firebase Firestore.

## Tabla de Contenidos
- [Tecnologías Utilizadas](#tecnologías-utilizadas)
- [Primeros Pasos](#primeros-pasos)
  - [1. Crear un Proyecto en Firebase](#1-crear-un-proyecto-en-firebase)
  - [2. Vincular Firebase con el Código](#2-vincular-firebase-con-el-código)
  - [3. Configurar Firebase Authentication y Firestore](#3-configurar-firebase-authentication-y-firestore)
  - [4. Ejecutar el Proyecto](#4-ejecutar-el-proyecto)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Pantallas](#pantallas)
  - [1. Pantalla de Inicio de Sesión](#1-pantalla-de-inicio-de-sesión)
  - [2. Pantalla de Registro](#2-pantalla-de-registro)
  - [3. Pantalla de Adopción de Perros](#3-pantalla-de-adopción-de-perros)
  - [4. Pantalla de Chat](#4-pantalla-de-chat)
- [Explicación de Conceptos Clave](#explicación-de-conceptos-clave)
  - [1. Arquitectura Limpia](#1-arquitectura-limpia)
  - [2. ViewModel](#2-viewmodel)
  - [3. Firebase Authentication y Firestore](#3-firebase-authentication-y-firestore)
  - [4. Corutinas y Manejo de Datos](#4-corutinas-y-manejo-de-datos)

---

## Tecnologías Utilizadas

- **Kotlin**
- **Jetpack Compose**
- **Firebase Authentication**
- **Firebase Firestore**
- **Dagger Hilt**
- **Coroutines**
- **MVVM**
- **Arquitectura Limpia**

---

## Primeros Pasos

### 1. Crear un Proyecto en Firebase

1. Visita [Firebase Console](https://console.firebase.google.com/).
2. Haz clic en "Añadir Proyecto" y sigue los pasos para crear uno nuevo.
3. Una vez creado, haz clic en "Añadir aplicación Android" y proporciona el nombre del paquete de tu aplicación.
4. Descarga el archivo `google-services.json` y colócalo en la carpeta `app` de tu proyecto de Android.

### 2. Vincular Firebase con el Código

1. Abre el archivo `libs.versions.toml`, generalmente ubicado en la carpeta `gradle/`. Incluye las siguientes versiones y dependencias:

    ```toml
    [versions]
    firebase-bom = "32.1.0"
    google-services = "4.3.15"
    
    [plugins]
    google-services = { id = "com.google.gms.google-services" }

    [libraries]
    firebase-auth = { module = "com.google.firebase:firebase-auth" }
    firebase-firestore = { module = "com.google.firebase:firebase-firestore" }
    ```

2. Abre el archivo `build.gradle` en el módulo `app` y asegúrate de que las dependencias estén incluidas según los alias definidos en `libs.versions.toml`:

    ```gradle
    plugins {
        id 'com.android.application'
        id 'kotlin-android'
        id libs.plugins.google-services
    }

    dependencies {
        implementation platform(libs.firebase-bom)
        implementation libs.firebase-auth
        implementation libs.firebase-firestore
    }
    ```

3. Sincroniza el proyecto con Gradle.

### 3. Configurar Firebase Authentication y Firestore

1. En la consola de Firebase, navega a la sección "Authentication" y habilita la autenticación por correo electrónico/contraseña.
2. Ve a la sección "Firestore Database" y crea una base de datos en modo de prueba.

### 4. Ejecutar el Proyecto

1. Asegúrate de que tu dispositivo Android o emulador esté conectado.
2. Compila y ejecuta el proyecto en Android Studio.

---

## Estructura del Proyecto

El proyecto sigue los principios de **Arquitectura Limpia** y está estructurado en capas para promover la separación de responsabilidades:

1. **Capa de Dominio**: Contiene la lógica de negocio y los casos de uso.
2. **Capa de Datos**: Maneja la interacción con Firebase y Firestore, e implementa los repositorios para acceder a los datos.
3. **Capa de Presentación**: Contiene los ViewModels y los componentes de UI, desarrollados usando Jetpack Compose.

---

## Pantallas

### 1. Pantalla de Inicio de Sesión
- **Funcionalidad**: Permite a los usuarios iniciar sesión utilizando autenticación por correo electrónico y contraseña. Valida la entrada e interactúa con Firebase Authentication.
- **Tecnología**: Firebase Authentication, ViewModel, Jetpack Compose.

### 2. Pantalla de Registro
- **Funcionalidad**: Los usuarios pueden registrarse proporcionando sus datos, como nombre, correo electrónico y contraseña. Los datos de usuario se almacenan en Firebase Firestore.
- **Tecnología**: Firebase Authentication, Firebase Firestore, Dagger Hilt, ViewModel, Jetpack Compose.

### 3. Pantalla de Adopción de Perros
- **Funcionalidad**: Muestra una lista de perros disponibles para adopción. Los usuarios pueden ver detalles y marcar perros para adoptar.
- **Tecnología**: Firebase Firestore, ViewModel, Jetpack Compose.

### 4. Pantalla de Chat
- **Funcionalidad**: Los usuarios pueden comunicarse entre sí para facilitar la adopción de perros. La mensajería en tiempo real está habilitada a través de Firebase Firestore.
- **Tecnología**: Firebase Firestore, Coroutines, ViewModel, Jetpack Compose.

### 5. Pantalla de Perfil
- **Funcionalidad**: Permite a los usuarios modificar la información de su perfil, como el nombre, correo electrónico y foto de perfil. Los cambios se sincronizan con Firebase Firestore para actualizar los datos del usuario.
- **Tecnología**: Firebase Firestore, Firebase Storage (para fotos de perfil), ViewModel, Jetpack Compose.

### 6. Pantalla de Perros Compartidos
- **Funcionalidad**: Muestra una lista de perros que los usuarios han compartido públicamente para adopción. Otros usuarios pueden ver detalles sobre estos perros e iniciar conversaciones para la adopción.
- **Tecnología**: Firebase Firestore, ViewModel, Jetpack Compose.

### 7. Pantalla de Perros Favoritos
- **Funcionalidad**: Muestra una lista de perros a los que el usuario ha dado "me gusta" o marcado como favoritos. El usuario puede hacer seguimiento de los perros en los que está interesado para adoptar.
- **Tecnología**: Firebase Firestore, ViewModel, Jetpack Compose.

### 8. Pantalla de Subir Perro
- **Funcionalidad**: Permite a los usuarios subir información sobre un perro para adopción, incluyendo la imagen del perro, nombre, raza y otros detalles. La información se guarda en Firebase Firestore y las imágenes se suben a Firebase Storage.
- **Tecnología**: Firebase Firestore, Firebase Storage, ViewModel, Jetpack Compose.

---

## Explicación de Conceptos Clave

### 1. Arquitectura Limpia

La aplicación sigue el enfoque de **Arquitectura Limpia**, separando la aplicación en capas:

- **Capa de Dominio**: Contiene las reglas de negocio y es independiente de cualquier UI o servicio externo. Aquí se definen los casos de uso que interactúan con los repositorios.
- **Capa de Datos**: Contiene las implementaciones de los repositorios y maneja el acceso a los datos (Firebase Firestore y Authentication).
- **Capa de Presentación**: Contiene los componentes de UI y ViewModels que interactúan con los casos de uso para proporcionar datos a las vistas.

Esta arquitectura garantiza una alta modularidad, lo que hace que la aplicación sea más fácil de mantener y probar.

### 2. ViewModel

El **ViewModel** se utiliza para gestionar y almacenar datos relacionados con la interfaz de usuario de forma consciente del ciclo de vida. En este proyecto, cada pantalla tiene su propio ViewModel, que maneja la lógica relacionada con la pantalla. El ViewModel interactúa con la capa de dominio para obtener y actualizar datos sin estar vinculado al ciclo de vida de actividades o fragmentos.

Al separar la lógica de la UI de la capa de datos, la app se vuelve más testeable y mantenible.

### 3. Firebase Authentication y Firestore

- **Firebase Authentication**: La aplicación permite a los usuarios registrarse e iniciar sesión utilizando correo electrónico y contraseña. El `AuthRepository` abstrae las llamadas a Firebase para mantener la lógica de negocio separada de la API de Firebase.
- **Base de datos Firestore**: La información del usuario, como los detalles del perfil y los perros disponibles para adopción, se almacena en **Firestore**. La aplicación utiliza **actualizaciones en tiempo real** para mantener a los usuarios informados sobre nuevas oportunidades de adopción y mensajes de chat.

### 4. Corutinas y Manejo de Datos

Se utilizan **corutinas** para realizar llamadas asíncronas a Firebase sin bloquear el hilo principal. Esto garantiza que la aplicación siga siendo receptiva cuando se realicen operaciones de red, como consultar Firestore o autenticar a los usuarios.

La aplicación separa las llamadas a Firebase del ViewModel utilizando repositorios, que manejan la lógica de obtención y almacenamiento de datos. Al hacer esto, el ViewModel se centra únicamente en la lógica de la interfaz de usuario, mientras que el acceso a los datos está encapsulado en los repositorios.

---

