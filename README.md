# Expense Finance Manager

An Android app built with Kotlin and Jetpack Compose for managing personal expenses and income.

## Features

- **Dual View Tracking**: Separate tabs for expenses and income with identical functionality
- **Time-Based Analysis**: View totals for day, week, month, year, or custom date ranges
- **Visual Charts**: Category-wise spending breakdown with pie charts
- **Category Management**: Add, edit, and delete categories with custom icons and colors
- **Transaction Management**: Add, edit, and delete transactions with detailed information
- **Modern Architecture**: Built with MVVM, Room, Flow, Hilt, and Material3

## Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose with Material3
- **Architecture**: MVVM (Model-View-ViewModel)
- **Database**: Room
- **Dependency Injection**: Hilt
- **Async**: Kotlin Flow
- **Navigation**: Jetpack Navigation Compose

## Project Structure

```
app/src/main/java/com/expense/financemanager/
├── data/
│   ├── local/
│   │   ├── dao/           # Room DAOs
│   │   ├── ExpenseDatabase.kt
│   │   └── Converters.kt
│   └── repository/        # Repository layer
├── domain/
│   └── model/             # Data models (Category, Transaction)
├── presentation/
│   ├── screens/           # Compose screens
│   ├── components/        # Reusable UI components
│   ├── viewmodel/         # ViewModels
│   └── navigation/        # Navigation setup
├── di/                    # Dependency injection modules
└── ui/theme/              # Theme configuration

## Building the App

### Local Development

1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Run the app on an emulator or physical device

### Automated Builds (CI/CD)

This repository includes a GitHub Actions workflow that automatically builds and releases the app on every push to `main`:

- ✅ Builds signed release APK
- ✅ Creates GitHub Release with version tag
- ✅ Uploads APK to the release

**To set up APK signing for automated builds:**
1. Run `./generate-keystore.sh` to create a keystore
2. Follow the instructions in [SIGNING_SETUP.md](SIGNING_SETUP.md) to add secrets to GitHub

Without signing configuration, the workflow will create unsigned APKs (useful for testing).

## Requirements

- Android Studio Hedgehog or later
- Minimum SDK: 26 (Android 8.0)
- Target SDK: 34 (Android 14)
- Kotlin 1.9.20+