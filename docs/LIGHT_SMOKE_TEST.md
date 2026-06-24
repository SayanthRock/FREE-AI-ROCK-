# FREE-AI-ROCK Light Smoke Test

Use this checklist after a successful debug APK build and before reusing the architecture in another project such as Rock DevOps.

This is a light runtime validation, not a full QA pass.

## 1. Launch Guard

Install the APK and open the app.

Expected result:

```text
App opens without crashing.
Setup screen is visible.
Bottom navigation is visible.
```

This verifies:

```text
MainActivity
ViewModelProvider wiring
AppViewModelFactory
SecureStorageManager creation
Retrofit service creation
Compose app shell
```

## 2. Navigation Loop

Tap every bottom navigation route.

Routes:

```text
Code AI
PR Review
Image Studio
Settings
```

Expected result:

```text
Every route opens without crash.
Back stack remains stable.
Theme remains consistent.
```

## 3. Persistence Gate

Open the setup screen, enter temporary placeholder values, and tap Save Securely.

Close and reopen the app.

Expected result:

```text
App does not crash after saving values.
Encrypted local storage initializes correctly.
Stored settings remain available to the app.
```

This verifies:

```text
AndroidX Security Crypto
MasterKey
EncryptedSharedPreferences
AppViewModel.saveKeys
ImageViewModel.refreshRenderer
```

## 4. PR Review API Handshake

Open PR Review and enter a public repository with a real pull request number.

Expected result:

```text
The app should not crash.
With valid user configuration, a review result should appear.
With invalid user configuration, a readable error message should appear.
```

This verifies:

```text
GitHubApiService
Retrofit
Gson
ReviewViewModel
AiCodeAnalyzer
CodeAnalysisState
```

## 5. Theme Toggle

Open Settings and switch between:

```text
System Auto Color
Light Mode
Dark Mode
```

Expected result:

```text
Theme changes immediately.
No Activity crash.
No navigation crash.
```

## Smoke Test Result

```text
[ ] Launch Guard passed
[ ] Navigation Loop passed
[ ] Persistence Gate passed
[ ] PR Review API Handshake passed
[ ] Theme Toggle passed
```

## Rule

Patch any failed check in FREE-AI-ROCK before copying the architecture into Rock DevOps.
