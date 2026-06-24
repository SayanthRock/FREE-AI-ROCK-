# FREE-AI-ROCK Release Crash Debugging

Release builds use R8 minification and obfuscation. That keeps the APK/AAB smaller, but raw crash logs may show unreadable class and method names such as `a.b.c`.

This guide explains how to capture and decode a production crash for a signed FREE-AI-ROCK release.

## 1. Capture the crash with ADB

Install the signed release APK on a test device, then clear the old log buffer before reproducing the crash.

```bash
adb logcat -c
```

To capture only fatal runtime crashes:

```bash
adb logcat AndroidRuntime:E *:S
```

For a focused app-only log while the app is running:

```bash
adb logcat --pid=$(adb shell pidof -s com.sayanthrock.freeairock)
```

Save the obfuscated crash output as:

```text
obfuscated_crash.txt
```

## 2. Find the matching R8 mapping file

Every release build generates a version-specific mapping file:

```text
app/build/outputs/mapping/release/mapping.txt
```

Keep the exact `mapping.txt` for every version you distribute. A mapping file from one version cannot reliably decode crashes from another version.

The release workflow uploads this file as:

```text
FREE-AI-ROCK-r8-mapping
```

It is also attached to tagged GitHub Releases next to the signed APK/AAB.

## 3. Decode the crash with retrace

The retrace tool is included in Android SDK command-line tools.

Common paths:

```text
Windows: %LOCALAPPDATA%\Android\Sdk\cmdline-tools\latest\bin\retrace.bat
macOS: ~/Library/Android/sdk/cmdline-tools/latest/bin/retrace
Linux: ~/Android/Sdk/cmdline-tools/latest/bin/retrace
```

Run:

```bash
retrace mapping.txt obfuscated_crash.txt
```

The output will show readable class, method, and line information for the release crash.

## 4. Google Play Console de-obfuscation

When publishing the AAB to Google Play, upload the same `mapping.txt` for that exact version code.

Path in Play Console:

```text
App Bundle Explorer → Version → Downloads → Deobfuscation file
```

This allows Android Vitals and Play Console crash reports to show readable stack traces for FREE-AI-ROCK production releases.

## Release checklist

Before tagging a production release:

- Run the debug workflow.
- Run the release workflow with signing secrets configured.
- Download and archive `FREE-AI-ROCK-r8-mapping`.
- Install the signed release APK on a real device.
- Test secure setup, PR Review, theme switching, and gallery saving.
- Keep the signed APK/AAB and mapping file together for that version.
