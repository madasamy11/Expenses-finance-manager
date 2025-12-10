# Expense Finance Manager - Implementation Details

## Overview
This is a complete Android application for managing personal finances built with modern Android development practices.

## Architecture

### MVVM Pattern
- **Model**: Room database entities (Category, Transaction)
- **View**: Jetpack Compose UI screens
- **ViewModel**: ExpenseViewModel and IncomeViewModel manage UI state

### Key Components

#### 1. Data Layer
- **Room Database**: Local SQLite database with Room ORM
  - `ExpenseDatabase`: Main database class
  - `CategoryDao`: Data access for categories
  - `TransactionDao`: Data access for transactions
  - `Converters`: Type converters for Room
  
- **Repository**: Single source of truth
  - `ExpenseRepository`: Abstracts data access

#### 2. Domain Layer
- **Models**:
  - `Category`: Represents expense/income categories with icon and color
  - `Transaction`: Represents individual transactions
  - `TransactionType`: Enum for EXPENSE or INCOME

#### 3. Presentation Layer
- **ViewModels**:
  - `ExpenseViewModel`: Manages expense-related state
  - `IncomeViewModel`: Manages income-related state
  - Both use Kotlin Flow for reactive data streams

- **Screens**:
  - `ExpenseScreen`: Main expense tracking screen
  - `IncomeScreen`: Main income tracking screen
  - `AddEditCategoryScreen`: Add/edit categories with icon and color picker
  - `AddEditTransactionScreen`: Add/edit transactions

- **Components**:
  - `PeriodSelector`: Choose day/week/month/year/custom periods
  - `TransactionItem`: Display individual transaction
  - `CategoryTotalsChart`: Pie chart showing category breakdown

#### 4. Dependency Injection
- **Hilt**: Provides dependencies
  - `DatabaseModule`: Provides Room database and DAOs

## Features

### 1. Dual Tracking
- Separate tabs for Expenses and Income
- Identical functionality for both types

### 2. Time-Based Analysis
- **Day**: Shows today's transactions
- **Week**: Shows current week
- **Month**: Shows current month
- **Year**: Shows current year
- **Custom**: User-defined date range

### 3. Visual Analytics
- Pie chart showing category-wise breakdown
- Total amount display with color coding
- Legend with percentages and amounts

### 4. Category Management
- Add/edit/delete categories
- Choose from predefined icons
- Select custom colors
- Separate categories for expenses and income

### 5. Transaction Management
- Add/edit/delete transactions
- Select category
- Enter amount and description
- Choose date
- Automatic validation

### 6. Default Categories
On first launch, the app creates default categories:

**Expense Categories**:
- Food & Dining
- Shopping
- Transportation
- Entertainment
- Bills & Utilities

**Income Categories**:
- Salary
- Investment
- Freelance

## Technology Stack

### Core
- **Kotlin**: Primary programming language
- **Jetpack Compose**: Modern declarative UI
- **Material 3**: Latest Material Design

### Architecture Components
- **Room**: SQLite database
- **ViewModel**: Lifecycle-aware state management
- **Navigation Compose**: Type-safe navigation
- **Kotlin Flow**: Reactive data streams

### Dependency Injection
- **Hilt**: Compile-time DI framework

### Build System
- **Gradle KTS**: Kotlin DSL for build configuration

## Project Structure

```
app/
├── src/main/
│   ├── java/com/expense/financemanager/
│   │   ├── data/
│   │   │   ├── local/
│   │   │   │   ├── dao/
│   │   │   │   │   ├── CategoryDao.kt
│   │   │   │   │   └── TransactionDao.kt
│   │   │   │   ├── Converters.kt
│   │   │   │   ├── DatabaseCallback.kt
│   │   │   │   └── ExpenseDatabase.kt
│   │   │   └── repository/
│   │   │       └── ExpenseRepository.kt
│   │   ├── di/
│   │   │   └── DatabaseModule.kt
│   │   ├── domain/
│   │   │   └── model/
│   │   │       ├── Category.kt
│   │   │       └── Transaction.kt
│   │   ├── presentation/
│   │   │   ├── components/
│   │   │   │   ├── CategoryTotalsChart.kt
│   │   │   │   ├── PeriodSelector.kt
│   │   │   │   └── TransactionItem.kt
│   │   │   ├── navigation/
│   │   │   │   └── Screen.kt
│   │   │   ├── screens/
│   │   │   │   ├── AddEditCategoryScreen.kt
│   │   │   │   ├── AddEditTransactionScreen.kt
│   │   │   │   ├── ExpenseScreen.kt
│   │   │   │   └── IncomeScreen.kt
│   │   │   └── viewmodel/
│   │   │       ├── ExpenseViewModel.kt
│   │   │       └── IncomeViewModel.kt
│   │   ├── ui/theme/
│   │   │   ├── Color.kt
│   │   │   ├── Theme.kt
│   │   │   └── Type.kt
│   │   ├── ExpenseApp.kt
│   │   └── MainActivity.kt
│   ├── res/
│   │   ├── drawable/
│   │   ├── mipmap-*/
│   │   └── values/
│   └── AndroidManifest.xml
├── build.gradle.kts
└── proguard-rules.pro
```

## Database Schema

### Categories Table
```sql
CREATE TABLE categories (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    icon TEXT NOT NULL,
    color INTEGER NOT NULL,
    type TEXT NOT NULL
);
```

### Transactions Table
```sql
CREATE TABLE transactions (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    amount REAL NOT NULL,
    categoryId INTEGER NOT NULL,
    description TEXT NOT NULL,
    date INTEGER NOT NULL,
    type TEXT NOT NULL,
    FOREIGN KEY (categoryId) REFERENCES categories(id) ON DELETE CASCADE
);
```

## Build Instructions

### Prerequisites
- Android Studio Hedgehog or later
- JDK 17
- Android SDK with API 34
- Gradle 8.2+

### Steps
1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files
4. Build and run on emulator or device

### Gradle Commands
```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run tests
./gradlew test

# Install on connected device
./gradlew installDebug
```

## Future Enhancements
- Export data to CSV/Excel
- Backup and restore functionality
- Budget tracking and alerts
- Recurring transactions
- Multi-currency support
- Dark theme improvements
- Data visualization with more chart types
- Search and filter transactions
- Transaction attachments (receipts)
- Widgets for home screen
