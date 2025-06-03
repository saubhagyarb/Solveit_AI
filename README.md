# SolveIt AI - Android Chat Application

A modern Android chat application that leverages Google's Gemini AI model for interactive conversations and image analysis.

## Features

- 🤖 Powered by Google's Gemini AI model
- 📸 Image analysis capabilities
- 💬 Real-time chat interface
- 🎨 Material Design 3 with dynamic theming
- ✨ Smooth animations and transitions
- 📝 Markdown text rendering
- 📱 Responsive UI components
- 📷 Camera and gallery integration

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **AI Integration**: Google Generative AI
- **Image Loading**: Coil 3
- **Animations**: Lottie
- **Text Rendering**: Compose Markdown
- **Architecture**: MVVM
- **Minimum SDK**: 24
- **Target SDK**: 35

## Screenshots

### Light Theme
| Empty State | Image Analysis | Chat Interface |
|-------------|----------------|----------------|
| ![Empty Light](https://github.com/user-attachments/assets/6ce79312-3727-4184-ab13-280b39d3447b) | ![Chat Light](https://github.com/user-attachments/assets/a3c33c30-0f84-4f6a-b006-52908abafce9) | ![Image Light](https://github.com/user-attachments/assets/803e3cc7-bb9d-4ff4-a3e2-fa19d56d4ee1) |


### Dark Theme
| Empty State | Image Analysis | Chat Interface |
|-------------|----------------|----------------|
| ![Empty Light](https://github.com/user-attachments/assets/69084443-49ea-4dbf-af9a-504f7eca70c4) | ![Chat Light](https://github.com/user-attachments/assets/eae03461-9fed-4b2e-bcfd-3d7d9b3307a0) | ![Image Light](https://github.com/user-attachments/assets/80dccec9-2e8a-40c6-9975-b4c23c1ccc8b) |

### Features Demonstrated
- **Welcome Screen**: Dynamic greeting based on time of day with Lottie animations
- **Chat Interface**: Material 3 design with gradient borders and dynamic theming
- **Suggestion Chips**: Quick action buttons with icons for common queries
- **Image Analysis**: Visual content processing with AI-powered responses
- **Interactive UI**: Real-time chat with loading indicators and error handling

## Setup

1. Clone the repository
2. Add your Google Generative AI API key in `GenerativeModel.kt`
3. Build and run the project using Android Studio

## Dependencies

```kotlin
implementation("androidx.compose.ui:ui-text-google-fonts:1.8.0")
implementation("com.airbnb.android:lottie-compose:6.6.6")
implementation("androidx.compose.material:material-icons-extended:1.7.0")
implementation("com.github.jeziellago:compose-markdown:0.5.7")
implementation("io.coil-kt.coil3:coil-compose:3.1.0")
implementation("com.google.ai.client.generativeai:generativeai")
