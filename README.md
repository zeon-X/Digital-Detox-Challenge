# Digital Detox Challenge

The **Digital Detox Challenge** app helps users manage their digital well-being by allowing them to temporarily suspend access to certain apps on their devices. With a simple and intuitive interface, users can set specific times to block distracting apps, track usage, and improve focus.

---

## Features

- **App Blocking**: Select apps to be temporarily blocked during specified time intervals.
- **Accessibility Service Integration**: Uses Accessibility Service to enforce app blocking and monitor app activity.
- **Firebase Authentication**: Users can sign up and log in securely using email and password.
- **Usage Statistics**: Track app usage with detailed stats to encourage healthy usage patterns.
- **User-Friendly Interface**: Clean and straightforward design to easily manage and view app blocking schedules.

---

## Project Structure

Digital-Detox-Challenge/
├── .gitignore
├── README.md
├── build.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
├── settings.gradle.kts
├── app/
│   ├── build.gradle.kts
│   ├── src/
│   │   ├── main/
│   │   │   ├── AndroidManifest.xml
│   │   │   ├── java/com/aleehatech/digitaldetoxchallange/
│   │   │   │   ├── FocusModeAccessibilityService.kt
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── ui/
│   │   │   │   │   ├── auth/
│   │   │   │   │   │   ├── SigninActivity.kt
│   │   │   │   │   │   ├── SignupActivity.kt
│   │   │   │   │   ├── home/
│   │   │   │   │   │   ├── HomeFragment.kt
│   │   │   │   │   │   ├── AppAdapter.kt
│   │   │   │   │   ├── stats/
│   │   │   │   │   │   ├── StatsFragment.kt
│   │   │   │   │   │   ├── StatsAdapter.kt
│   │   │   │   │   ├── blockscreen/
│   │   │   │   │   │   ├── BlockScreenActivity.kt
│   │   │   │   └── ...
│   │   └── res/
│   │       └── layout/...
└── ...




---

## Getting Started

Follow these steps to clone and set up the project on your local machine:

### Prerequisites

- **Android Studio**: Ensure Android Studio is installed.
- **Firebase Account**: Required for Firebase Authentication setup.
- **CocoaPods** (if using an iOS device/emulator).
- **Gradle** and **Java SDK**: Installed and properly configured.

### Installation

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/yourusername/Digital-Detox-Challenge.git
   cd Digital-Detox-Challenge
2. **Open the Project in Android Studio**
- Start Android Studio and select **Open an existing Android Studio project**.
- Navigate to the cloned `Digital-Detox-Challenge` directory and select it.

3. **Sync Gradle**
- Allow Android Studio to sync the Gradle files automatically.
- Ensure all dependencies are downloaded correctly.

4. **Configure Firebase Authentication**
- Set up a Firebase project in the [Firebase Console](https://console.firebase.google.com/).
- Add an Android app in your Firebase project, download the `google-services.json` file, and place it in the `app/` directory.

5. **Enable Accessibility Services**
- In `FocusModeAccessibilityService.kt`, configure any required permissions or adjustments to monitor and block apps.
- Ensure the service is enabled in the device’s **Accessibility settings** when testing.

6. **Build and Run**
- Connect an Android device or use an emulator.
- Click **Run** or use **Shift + F10** to build and deploy the app.
