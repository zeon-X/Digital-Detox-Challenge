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

## Project architecture

### Workflow 

![Frame 22](https://github.com/user-attachments/assets/c99373bb-f454-4edf-8895-c42cc7053045)

---

## Project Structure
```bash
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


```

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
   git clone https://github.com/zeon-X/Digital-Detox-Challenge.git
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


## Screenshots

### Below are some screenshots showcasing the app's functionality:

| ScreenShots |ScreenShots |ScreenShots |
|-------------------------------------------- |-------------------------------------------- |-------------------------------------------- |
| ![Screenshot_20241114_092003](https://github.com/user-attachments/assets/0dc9a14c-1513-4c16-bbaa-d947640fe832)|  ![Screenshot_20241114_091653](https://github.com/user-attachments/assets/2957b6f9-b9ff-4cb8-a923-79a42c2f9e9b)| ![Screenshot_20241114_091758](https://github.com/user-attachments/assets/13c8f537-40c3-438f-a99b-eebd3c6c5fcc) | 
| ![Screenshot_20241114_091737](https://github.com/user-attachments/assets/975e063f-b8dc-4b3d-ac7e-c5006cc382e2) |  ![Screenshot_20241114_091820](https://github.com/user-attachments/assets/e18e79dd-47b0-4c48-ac97-e09cbade884e) | ![Screenshot_20241114_091928](https://github.com/user-attachments/assets/106146cf-3f32-47bd-8e93-03a4869cc785) |
| ![Screenshot_20241114_091918](https://github.com/user-attachments/assets/38ddeea0-bcdc-4492-a777-41f84592c3f2) | ![Screenshot_20241114_091938](https://github.com/user-attachments/assets/296eaec2-a96f-41f3-84b9-3b62d2999468) | ![Screenshot_20241114_091949](https://github.com/user-attachments/assets/49c2f945-dcaf-4d85-a108-2a377194b8c5)|



## Acknowledgments
- Thanks to Firebase for providing seamless authentication.
- Special mention to the Android Developer Documentation for comprehensive guides.








