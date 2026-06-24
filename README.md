# FREE-AI-ROCK

**FREE-AI-ROCK** is a free developer assistant for Android that combines AI chat, GitHub repository exploration, code explanation, pull request summaries, and local file export tools in one clean mobile-first interface.

## Goal

Build a practical AI developer tool that helps users:

- Browse GitHub repositories and files.
- Explain code with AI.
- Summarize pull requests and diffs.
- Generate developer prompts and code snippets.
- Save generated code to local files using Android Storage Access Framework.
- Use a clean charcoal/white UI with Light, Dark, and System theme modes.

## Tech Stack

| Layer | Choice |
|---|---|
| UI | Jetpack Compose |
| Language | Kotlin |
| Architecture | MVVM-ready modular package structure |
| Networking | Retrofit + OkHttp |
| Images | Coil |
| Local settings | DataStore |
| Cache layer | Room-ready structure |
| AI | Gemini/OpenAI-compatible service abstraction |
| GitHub | GitHub REST API |
| CI | GitHub Actions Android build workflow |

## Core Features

### GitHub Explorer

- List repositories.
- Browse repository file trees.
- Open files and view code.
- Fetch pull requests.
- Send selected code or PR diffs to AI for explanation.

### AI Chat

- Markdown-friendly chat UI.
- Code explanation prompts.
- Copy generated code.
- Save generated code to a local file.

### Image Generation

- Prompt input screen.
- REST-based image generation provider support.
- Coil-based image preview.

### Theme Engine

- System Auto, Light, and Dark modes.
- Charcoal and white developer-focused palette.
- Minimal rounded UI blocks.
- Code-friendly typography.

## Repository Structure

```text
FREE-AI-ROCK-/
├── app/
│   └── src/main/java/com/sayanthrock/freeairock/
│       ├── MainActivity.kt
│       ├── data/
│       │   ├── AiService.kt
│       │   ├── GitHubApi.kt
│       │   └── RepositoryModels.kt
│       └── ui/theme/
│           └── Theme.kt
├── docs/
│   ├── ARCHITECTURE.md
│   └── ROADMAP.md
├── .github/workflows/android-build.yml
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## Security Rules

- Never hardcode GitHub tokens or AI API keys.
- Store tokens only through secure storage in a later implementation step.
- Use OAuth or user-provided PAT only with clear user consent.
- Keep file writes inside Android SAF, not unsafe raw storage paths.

## Development Status

This repository now contains the first project foundation. The next implementation step is to wire real ViewModels, secure token storage, OAuth/PAT login, and working API screens.

## Brand

Built by **@SayanthRock** as a free AI developer tool.
