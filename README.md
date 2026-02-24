# 🛒 Supermarket Android App

A modern, robust, and scalable Android application for a supermarket chain. Built entirely with **Kotlin** and **Jetpack Compose**, following **Clean Architecture** principles and the **MVVM** pattern.

This app allows users to browse products, check weekly sales, locate nearby stores on a map, use a digital QR code for loyalty bonuses, and manage their user profiles.

---

## ✨ Features

* **🔐 Authentication:** Secure login and registration using phone numbers and OTP verification.
* **🏠 Home & Banners:** Dynamic home screen featuring promotional banners and weekly discounted products.
* **🛍️ Catalog & Search:** Browse categories and search for products seamlessly with infinite scrolling (**Paging 3**).
* **🗺️ Store Locator:** Interactive map displaying all supermarket branches using **OpenStreetMap** (OSMDroid).
* **💳 Loyalty QR Code:** Auto-generated digital QR code for users to scan at checkout for collecting or spending bonus points.
* **🔔 Notifications & News:** Stay updated with the latest supermarket news and personalized push notifications.
* **👤 Profile Management:** Edit personal details, manage avatars, and configure app settings.

---

## 📱 Screenshots

> **Note:** Replace the placeholder links with your actual image paths once you upload screenshots to your repository.

<div align="center">
 <img width="476" height="1077" alt="Снимок экрана от 2026-02-24 11-48-00" src="https://github.com/user-attachments/assets/d2c72371-48a6-4a77-9fc1-2f5f12ebd8dd" />
<img width="476" height="1077" alt="Снимок экрана от 2026-02-24 11-52-24" src="https://github.com/user-attachments/assets/c407c66a-1b15-46da-a500-a9f8289a9e74" />
<img width="476" height="1077" alt="Снимок экрана от 2026-02-24 11-53-11" src="https://github.com/user-attachments/assets/c67f4b4a-31fc-4c6a-b55c-72a6bb3e6842" />
<img width="476" height="1077" alt="Снимок экрана от 2026-02-24 11-53-27" src="https://github.com/user-attachments/assets/7ae0ad9c-de6a-46eb-871e-e8789fd5d2a9" />
<img width="476" height="1077" alt="Снимок экрана от 2026-02-24 11-53-35" src="https://github.com/user-attachments/assets/d557b499-570d-42f8-a258-23cdca686522" />
<img width="476" height="1077" alt="Снимок экрана от 2026-02-24 11-53-44" src="https://github.com/user-attachments/assets/cc8868ea-e4c8-4417-b75a-e503eb3dca77" />
<img width="476" height="1077" alt="Снимок экрана от 2026-02-24 11-54-28" src="https://github.com/user-attachments/assets/0a46eace-a010-491c-a4f2-4226aa08cc24" />
<img width="476" height="1077" alt="Снимок экрана от 2026-02-24 11-54-37" src="https://github.com/user-attachments/assets/4e7c6c4f-bac6-43fa-9fe6-ae1cd7f5b31c" />
<img width="476" height="1077" alt="Снимок экрана от 2026-02-24 12-04-19" src="https://github.com/user-attachments/assets/f66634f0-80e6-48e4-b17f-0b9efe3225a7" />

</div>

---

## 🛠️ Tech Stack & Libraries

This project takes advantage of the latest modern Android development tools and libraries:

**UI & Design**
* [Jetpack Compose](https://developer.android.com/jetpack/compose) - Modern declarative UI toolkit.
* [Material Design 3](https://m3.material.io/) - UI components and styling.
* [Coil](https://coil-kt.github.io/coil/) - Image loading for Jetpack Compose.

**Architecture & Core**
* **Clean Architecture** (Domain, Data, and Presentation layers)
* **MVVM** (Model-View-ViewModel) pattern
* [Kotlin Coroutines & Flow](https://kotlinlang.org/docs/coroutines-overview.html) - For asynchronous programming and reactive streams.
* [Dagger Hilt](https://dagger.dev/hilt/) - Dependency Injection.

**Networking & Data**
* [Retrofit 2](https://square.github.io/retrofit/) - Type-safe HTTP client.
* [OkHttp 3](https://square.github.io/okhttp/) - Logging Interceptor for API calls.
* [Kotlinx Serialization](https://kotlinlang.org/docs/serialization.html) - JSON parsing and serialization.
* [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) - For paginated data loading (Catalog & Search).
* **DataStore / SharedPreferences** - For local token and settings management.

**Mapping & Others**
* [OSMDroid](https://github.com/osmdroid/osmdroid) - OpenStreetMap integration for Android.
* [ZXing (Zebra Crossing)](https://github.com/zxing/zxing) - Core library used for QR Code generation.

---

## 🏗️ Project Structure

The project strictly follows **Clean Architecture** to ensure separation of concerns, testability, and scalability:

* `presentation/`: Contains all UI-related code (Jetpack Compose screens, ViewModels, UI States, Navigation).
* `domain/`: The core business logic of the app (Use Cases, Domain Models/Entities, Repository Interfaces, AppExceptions). This layer is independent of any external frameworks.
* `data/`: Data management (Repository Implementations, Retrofit APIs, DTOs, Mappers, Local Storage, Paging Sources).

---

## 🚀 Getting Started

### Prerequisites
* Android Studio (Latest version recommended, e.g., Iguana or newer)
* JDK 17 or higher

### Installation
1.  Clone the repository:
    ```bash
    git clone [https://github.com/khidirov113/Supermarket.git](https://github.com/khidirov113/Supermarket.git)
    ```
2.  Open the project in Android Studio.
3.  Sync the project with Gradle files.
4.  Build and run the app on an emulator or physical device.

---

## 👨‍💻 Author

**Khidirov**
* GitHub: [@khidirov113](https://github.com/khidirov113)

Feel free to reach out if you have any questions or suggestions!
