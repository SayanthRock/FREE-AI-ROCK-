# FREE-AI-ROCK 🚀

**FREE-AI-ROCK** is an Android AI developer toolkit that connects GitHub file links and pull request diffs with Google's Gemini models. It can store keys locally, summarize source files, review pull request diffs, and ship release builds through GitHub Actions.

[![Android Release Build](https://github.com/SayanthRock/FREE-AI-ROCK-/actions/workflows/android-release.yml/badge.svg)](https://github.com/SayanthRock/FREE-AI-ROCK-/actions/workflows/android-release.yml)
![Tests](https://img.shields.io/github/actions/workflow/status/SayanthRock/FREE-AI-ROCK-/android-test.yml?label=Tests)
![Debug Build](https://img.shields.io/github/actions/workflow/status/SayanthRock/FREE-AI-ROCK-/android-build.yml?label=Debug%20Build)
![Release](https://img.shields.io/github/v/release/SayanthRock/FREE-AI-ROCK-)
![Platform](https://img.shields.io/badge/Platform-Android_7%2B-3DDC84?logo=android)
![Kotlin](https://img.shields.io/badge/Kotlin-Android-7F52FF?logo=kotlin)

## 📌 Current App Version

```text
versionName: 1.0.3
versionCode: 4
```

## ✨ Core Capabilities

- **Code AI:** Paste a GitHub raw URL or normal `github.com/.../blob/...` file URL and generate a plain-English code explanation.
- **PR Review:** Fetch pull request diffs through the GitHub API and summarize purpose, risks, testing notes, and release notes.
- **Image Studio Foundation:** Keeps bitmap state and gallery-save helpers ready while production image rendering remains disabled for this build.
- **Settings & About:** Switch between System Auto, Light, and Dark modes while keeping the FREE-AI-ROCK charcoal/white identity.
- **Secure Setup:** Store user-provided GitHub and Gemini keys locally with encrypted preferences when available, with a safe local fallback.

## 🛡️ Architecture & Security

- **Zero hardcoded secrets:** API keys are not committed to version control.
- **Encrypted local storage:** Uses AndroidX Security Crypto for app-side key storage.
- **Lifecycle-safe ViewModels:** A unified `ViewModelProvider.Factory` keeps ViewModel state stable.
- **Scoped storage:** Media output is written through Android `MediaStore` to `Pictures/FREE-AI-ROCK` without broad storage access.
- **Release signing support:** CI can decode and validate `KEYSTORE_BASE64`, sign release outputs when secrets are valid, or build unsigned artifacts when secrets are incomplete.

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| UI | Jetpack Compose, Material 3, Compose Navigation |
| Language | Kotlin |
| Architecture | MVVM, ViewModelFactory, Coroutines, StateFlow |
| Networking | Retrofit, OkHttp, Gson |
| AI | Google AI Client SDK / Gemini |
| Security | AndroidX Security Crypto, Android Keystore-backed encrypted preferences |
| Storage | Android MediaStore, scoped storage |
| Testing | JUnit 4, MockK, Coroutines Test, AndroidX Core Testing |
| CI/CD | GitHub Actions debug build, unit tests, version bump automation, keystore validation, release build, GitHub Releases |

## 📱 App Sections

```text
Code AI
PR Review
Image Studio
Settings
```

## 🚀 Getting Started

1. Download the latest APK from the [Releases](https://github.com/SayanthRock/FREE-AI-ROCK-/releases) page.
2. Open FREE-AI-ROCK on your Android device.
3. Enter your GitHub Personal Access Token and Gemini API key in the secure setup screen.
4. Open **Code AI**, paste a GitHub raw/blob file URL, and summarize the file.
5. Open **PR Review**, enter owner, repo, and PR number, then summarize the diff.
6. Use **Settings** to switch between System Auto, Light, and Dark UI modes.

> Keep your API keys private. FREE-AI-ROCK stores them locally on your device and does not commit or upload them to this repository.

## 🧪 Testing

Run unit tests locally with:

```bash
gradle testDebugUnitTest --stacktrace
```

The repository also includes a dedicated GitHub Actions workflow:

```text
.github/workflows/android-test.yml
```

Test reports are uploaded as workflow artifacts for inspection.

## 📦 CI/CD Workflows

| Workflow | Purpose |
|---|---|
| `android-test.yml` | Runs unit tests |
| `android-build.yml` | Builds debug APK and uploads artifact |
| `android-unsigned-release.yml` | Builds unsigned release APK/AAB for verification |
| `android-release.yml` | Builds release APK/AAB, signs when secrets are valid, and creates/updates GitHub Releases |
| `keystore-check.yml` | Validates `KEYSTORE_BASE64`, passwords, and alias before release |
| `version-release.yml` | Manually updates `versionName` / `versionCode`, commits the bump, and optionally creates a release tag |

## 🤖 Version Update Automation

Use this when preparing the next app update:

```text
Actions → Version Update and Release → Run workflow
```

Current default next-version inputs:

```text
version_name: 1.0.4
version_code: 5
create_release_tag: true
```

When `create_release_tag` is enabled, the workflow creates `v<version_name>`. That tag starts `android-release.yml`, which builds release artifacts and signs them when signing secrets are valid.

## 🔐 Signed Release Setup

Required Repository Secrets:

```text
KEYSTORE_BASE64
KEY_ALIAS
STORE_PASSWORD
KEY_PASSWORD
```

Add them under:

```text
Settings → Secrets and variables → Actions → Repository secrets
```

Detailed setup and troubleshooting:

```text
docs/ANDROID_RELEASE_SIGNING.md
docs/KEYSTORE_BASE64_FIX.md
```

Before release, run:

```text
Actions → Keystore Secret Check → Run workflow
```

Expected success:

```text
KEYSTORE_BASE64 decoded successfully. Alias validated: freeairock
```

## 🧯 Release Crash Debugging

See:

```text
docs/RELEASE_CRASH_DEBUGGING.md
```

This guide explains how to capture release crashes with ADB, decode obfuscated stack traces with `retrace`, and upload mapping files to Google Play Console.

## 📂 Repository Structure

```text
FREE-AI-ROCK-/
├── .github/workflows/
│   ├── android-build.yml
│   ├── android-release.yml
│   ├── android-test.yml
│   ├── android-unsigned-release.yml
│   ├── keystore-check.yml
│   └── version-release.yml
├── app/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src/
│       ├── main/java/com/sayanthrock/freeairock/
│       ├── main/res/values/styles.xml
│       └── test/java/com/sayanthrock/freeairock/
├── docs/
├── scripts/
│   └── decode_keystore_secret.py
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## 👨‍💻 Developed By

**Sayanth Rock**  
Designed and developed in Kerala, India.

[Explore my GitHub](https://github.com/SayanthRock)
