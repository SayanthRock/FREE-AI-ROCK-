# FREE-AI-ROCK 🚀

**FREE-AI-ROCK** is an advanced AI-powered developer toolkit built natively for Android. It bridges the GitHub API with Google's Gemini generative models to explore repositories, summarize complex pull requests, explain code, and prepare AI-assisted development workflows inside a strict, system-aware minimalist interface.

![Tests](https://img.shields.io/github/actions/workflow/status/SayanthRock/FREE-AI-ROCK-/android-test.yml?label=Tests)
![Debug Build](https://img.shields.io/github/actions/workflow/status/SayanthRock/FREE-AI-ROCK-/android-build.yml?label=Debug%20Build)
![Release](https://img.shields.io/github/v/release/SayanthRock/FREE-AI-ROCK-)
![Platform](https://img.shields.io/badge/Platform-Android_10%2B-3DDC84?logo=android)
![Kotlin](https://img.shields.io/badge/Kotlin-Android-7F52FF?logo=kotlin)

## ✨ Core Capabilities

- **Code AI:** Explore GitHub repositories, fetch raw code files, and generate approachable plain-English explanations of technical structures.
- **PR Review:** Fetch raw Git diffs through the GitHub API and send them to Gemini 1.5 Flash for high-level summaries, risk notes, and testing guidance.
- **Image Studio Foundation:** Keeps the Image Studio screen and bitmap state model stable while the production image renderer remains disabled for this build.
- **Settings & About:** Switch between System Auto, Light, and Dark modes while keeping the FREE-AI-ROCK charcoal/white identity.
- **Secure Setup:** Store user-provided GitHub and Gemini keys locally using encrypted Android storage.

## 🛡️ Architecture & Security

FREE-AI-ROCK prioritizes local security, lifecycle resilience, and production safety.

- **Zero hardcoded secrets:** API keys are not committed to version control.
- **Encrypted local storage:** Uses AndroidX Security Crypto and encrypted preferences for local key storage.
- **Lifecycle-safe ViewModels:** A unified `ViewModelProvider.Factory` keeps ViewModel state stable across rotation, theme changes, and Activity recreation.
- **Scoped storage:** Media output is written through Android `MediaStore` to `Pictures/FREE-AI-ROCK` without broad storage access.
- **Release hardening:** ProGuard/R8 compatibility rules are included for Retrofit, Gson models, AndroidX Security Crypto, and the Google AI SDK.
- **Release traceability:** The release workflow uploads build logs, unsigned artifacts, signed artifacts when signing secrets are configured, and `mapping.txt` when generated.

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
| CI/CD | GitHub Actions debug build, unit tests, unsigned release verification, signed release, GitHub Releases |

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
4. Use **PR Review** to summarize pull request diffs.
5. Use **Settings** to switch between System Auto, Light, and Dark UI modes.

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
| `android-test.yml` | Runs JUnit/MockK unit tests |
| `android-build.yml` | Builds debug APK and uploads artifact |
| `android-release.yml` | Builds release APK/AAB, uploads unsigned artifacts, signs when secrets are configured, and creates tagged releases |

## 🔐 Signed Release Setup

The release workflow now verifies build health before signing by uploading this artifact:

```text
FREE-AI-ROCK-unsigned-release
```

Signed APK/AAB output still requires these Repository Secrets:

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
```

For production publishing, create and push a release tag:

```bash
git tag v1.0.0
git push origin v1.0.0
```

The release workflow will generate release APK/AAB outputs, sign them when the required secrets are available, and attach signed assets to GitHub Releases for tags matching `v*`.

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
│   └── android-test.yml
├── app/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src/
│       ├── main/java/com/sayanthrock/freeairock/
│       └── test/java/com/sayanthrock/freeairock/
├── docs/
│   ├── ANDROID_RELEASE_SIGNING.md
│   └── RELEASE_CRASH_DEBUGGING.md
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## 👨‍💻 Developed By

**Sayanth Rock**  
Designed and developed in Kerala, India.

[Explore my GitHub](https://github.com/SayanthRock)
