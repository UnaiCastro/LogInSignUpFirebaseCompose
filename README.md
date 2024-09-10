# Firebase Authentication Project with Jetpack Compose

This project is a simple Android application that allows users to sign up and log in using Firebase Authentication. User data (email and name) is stored in Firestore, Firebase's cloud database. The app is developed using Jetpack Compose for the UI, ViewModel for state management, and Dagger Hilt for dependency injection.

## Table of Contents
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
  - [1. Create a Firebase Project](#1-create-a-firebase-project)
  - [2. Link Firebase with the Code](#2-link-firebase-with-the-code)
  - [3. Configure Firebase Authentication and Firestore](#3-configure-firebase-authentication-and-firestore)
  - [4. Run the Project](#4-run-the-project)
- [Project Structure](#project-structure)
- [Explanation of Key Concepts](#explanation-of-key-concepts)
  - [1. ViewModel](#1-viewmodel)
  - [2. Dagger Hilt](#2-dagger-hilt)

---

## Technologies Used

- **Kotlin**
- **Jetpack Compose**
- **Firebase Authentication**
- **Firebase Firestore**
- **ViewModel**
- **Dagger Hilt**

---

## Getting Started

### 1. Create a Firebase Project

1. Visit [Firebase Console](https://console.firebase.google.com/).
2. Click on "Add Project" and follow the instructions to create a new project.
3. Once created, click on "Add Android App" and provide the package name of your application.
4. Download the `google-services.json` file and place it in the `app` folder of your Android project.

### 2. Link Firebase with the Code

1. Open the `libs.versions.toml` file, typically located in the `gradle/` folder of your project. Make sure to include the following versions and dependencies:

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

2. Open the `build.gradle` file at the module level (`app`) and ensure the dependencies are included using the aliases defined in `libs.versions.toml`:

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

1. In the Firebase Console, navigate to the "Authentication" section and enable email and password authentication.
2. Navigate to the "Firestore Database" section and create a database in test mode.

### 4. Run the Project

1. Ensure your Android device or emulator is connected.
2. Build and run the project in Android Studio.

---

## Explanation of Key Concepts

### 1. ViewModel

`ViewModel` is used to manage and store UI-related data in a lifecycle-conscious way. It ensures that data survives configuration changes, such as screen rotations. This is crucial in an app that handles authentication state, as it guarantees data is not lost when the device is rotated.

### 2. Dagger Hilt

Dagger Hilt is used for dependency injection in this application. It facilitates the provision of instances of classes like `FirebaseAuth` and `FirebaseFirestore`, which are used in multiple places throughout the app. Hilt simplifies dependency management and improves the modularity and testability of your code.

---

# Proyecto de Autenticación en Firebase con Jetpack Compose

Este proyecto es una aplicación Android simple que permite a los usuarios registrarse e iniciar sesión utilizando Firebase Authentication. Los datos del usuario (correo electrónico y nombre) se almacenan en Firestore, la base de datos en la nube de Firebase. La aplicación está desarrollada utilizando Jetpack Compose para la interfaz de usuario, ViewModel para la gestión del estado y Dagger Hilt para la inyección de dependencias.

## Tabla de Contenidos
- [Tecnologías Utilizadas](#tecnologías-utilizadas)
- [Primeros Pasos](#primeros-pasos)
  - [1. Crear un Proyecto en Firebase](#1-crear-un-proyecto-en-firebase)
  - [2. Vincular Firebase con el Código](#2-vincular-firebase-con-el-código)
  - [3. Configurar Firebase Authentication y Firestore](#3-configurar-firebase-authentication-y-firestore)
  - [4. Ejecutar el Proyecto](#4-ejecutar-el-proyecto)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Explicación de Conceptos Clave](#explicación-de-conceptos-clave)
  - [1. ViewModel](#1-viewmodel)
  - [2. Dagger Hilt](#2-dagger-hilt)

---

## Tecnologías Utilizadas

- **Kotlin**
- **Jetpack Compose**
- **Firebase Authentication**
- **Firebase Firestore**
- **ViewModel**
- **Dagger Hilt**

---

## Primeros Pasos

### 1. Crear un Proyecto en Firebase

1. Visita [Firebase Console](https://console.firebase.google.com/).
2. Haz clic en "Añadir proyecto" y sigue las instrucciones para crear un nuevo proyecto.
3. Una vez creado, haz clic en "Añadir aplicación Android" y proporciona el nombre del paquete de tu aplicación.
4. Descarga el archivo `google-services.json` y colócalo en la carpeta `app` de tu proyecto de Android.

### 2. Vincular Firebase con el Código

1. Abre el archivo `libs.versions.toml`, que normalmente se encuentra en la carpeta `gradle/` de tu proyecto. Asegúrate de incluir las siguientes versiones y dependencias:

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

2. Abre el archivo `build.gradle` a nivel de módulo (`app`) y asegúrate de que las dependencias se incluyan utilizando los alias definidos en `libs.versions.toml`:

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

1. En la consola de Firebase, navega a la sección "Authentication" y habilita el método de autenticación por correo electrónico y contraseña.
2. Navega a la sección "Firestore Database" y crea una base de datos en modo de prueba.

### 4. Ejecutar el Proyecto

1. Asegúrate de que tu dispositivo Android o emulador esté conectado.
2. Compila y ejecuta el proyecto en Android Studio.

---

## Explicación de Conceptos Clave

### 1. ViewModel

`ViewModel` se utiliza para gestionar y almacenar datos relacionados con la interfaz de usuario de forma que sobrevivan a los cambios de configuración, como las rotaciones de pantalla. Esto es crucial en una aplicación que maneja el estado de autenticación, ya que garantiza que los datos no se pierdan al rotar el dispositivo.

### 2. Dagger Hilt

Dagger Hilt se utiliza para la inyección de dependencias en esta aplicación. Facilita la provisión de instancias de clases como `FirebaseAuth` y `FirebaseFirestore`, que se utilizan en múltiples lugares a lo largo de la aplicación. Hilt simplifica la gestión de dependencias y mejora la modularidad y testabilidad del código.

---

