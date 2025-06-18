# Recipes App
The **Recipes App** is an Android project focused on clean architecture, modular code, and modern UI.

## Key Features
- **Clean Architecture**: Organized into Data, Domain, and Presentation layers.
- **MVVM Pattern**: ViewModel with `State` and `Flow` for reactive UI.
- **Jetpack Compose UI**: Declarative UI with dynamic search and lazy display.
- **API Integration**: Secure calls to [Food2Fork API](https://food2fork.ca) using Retrofit.
- **State Management**: Unified `UiState` for loading, success, and error handling.
- **Pagination**: for loading more items.

## Tech Stack
- **UI**: Jetpack Compose, Material3
- **Networking**: Retrofit, Kotlinx Serialization
- **Architecture**: Data, Domain, and Presentation layers, MVVM, Use Cases
- **Navigation**: Jetpack Navigation

## Structure Overview
recipesapp<br>
├── dataLayer: (API, DTOs, Repository)<br>
├── domain: (Models, Use Cases)<br>
├── presentation: (UI Screens, ViewModel)<br>
└── navigation: (NavGraph, Routes)<br>

## Note
This is a **conceptual app**, built to demonstrate best practices in modern Android development. and showcases clean code structure, scalable design, and Compose-based UI.

## Contact
**Feel free to connect or discuss any coding topics:**
- **Name**: Shahad Aldawsari
- **Email**: shahadali.dev@gmail.com  
- **LinkedIn**: [linkedin.com/in/shahad-ali](linkedin.com/in/shahad-ali-aldawsari-711792221)  
- **GitHub**: [github.com/shahadali](https://github.com/ShahadAliAldawsari)
