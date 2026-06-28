# Android Release Signing Checklist

This project builds release APK/AAB files with GitHub Actions. Signing and GitHub Release publishing require a private Android keystore stored through GitHub Actions Repository Secrets.

## Current release pipeline

The release workflow is:

```text
.github/workflows/android-release.yml
```

It performs these stages:

1. Build release APK and AAB.
2. Upload unsigned release outputs as `FREE-AI-ROCK-unsigned-release`.
3. Validate signing secrets and keystore alias.
4. Sign APK and AAB.
5. Upload signed outputs as `FREE-AI-ROCK-signed-release`.
6. Publish a GitHub Release when the workflow is triggered by a `v*` tag.

If signing secrets are missing, the unsigned artifact can still confirm that the Android build itself is healthy.

## Required GitHub Repository Secrets

Add these under:

```text
Repository Settings → Secrets and variables → Actions → Repository secrets
```

Required names:

```text
KEYSTORE_BASE64
KEY_ALIAS
STORE_PASSWORD
KEY_PASSWORD
```

Do not add them under Variables, Environment secrets, Codespaces secrets, or Dependabot secrets.

## Create a new release keystore

Run this locally if you do not already have a release keystore:

```bash
keytool -genkeypair \
  -v \
  -keystore release-keystore.jks \
  -storetype PKCS12 \
  -alias free-ai-rock \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000
```

Recommended secret values for this generated keystore:

```text
KEY_ALIAS=free-ai-rock
STORE_PASSWORD=<the keystore password you entered>
KEY_PASSWORD=<the key password you entered>
```

If you used the same password for both store and key, set `STORE_PASSWORD` and `KEY_PASSWORD` to the same value.

## Convert keystore to Base64

Linux, macOS, or Git Bash:

```bash
base64 -w 0 release-keystore.jks > release-keystore.base64.txt
```

Windows PowerShell:

```powershell
[Convert]::ToBase64String([IO.File]::ReadAllBytes("release-keystore.jks")) | Set-Content -NoNewline release-keystore.base64.txt
```

Copy the full one-line content of `release-keystore.base64.txt` into the `KEYSTORE_BASE64` Repository Secret.

## Fix `base64: invalid input`

This error means the `KEYSTORE_BASE64` secret is not clean Base64 text.

Use Termux to regenerate and copy clean text:

```bash
base64 -w 0 release-keystore.jks > keystore-base64.txt
base64 -d keystore-base64.txt > test-release-keystore.jks
keytool -list -v \
  -keystore test-release-keystore.jks \
  -storetype PKCS12 \
  -storepass airock \
  -alias free-ai-rock
tr -d '\r\n\t ' < keystore-base64.txt | termux-clipboard-set
```

Then update the existing GitHub repository secret named `KEYSTORE_BASE64` and paste only the copied Base64 text. Do not paste `KEYSTORE_BASE64 =`, terminal prompts, spaces, or the filename `keystore-base64.txt`.

## Trigger a fresh release workflow run

For manual verification:

```text
Actions → Android Release Build → Run workflow → Branch: main
```

Do not use the old failed run's `Re-run jobs` button when testing changed secrets or workflow changes.

For production GitHub Release publishing:

```bash
git tag v1.0.0
git push origin v1.0.0
```

The release workflow publishes GitHub Releases only for tags matching `v*`.

## Expected success signals

Unsigned build verification:

```text
FREE-AI-ROCK-unsigned-release
```

Signing validation:

```text
Release keystore decoded and alias validated
```

Signed output:

```text
FREE-AI-ROCK-signed-release
```

## Common failures

### `KEYSTORE_BASE64 secret is missing`

The secret value is empty or not visible to the runner. Re-add it under Repository Secrets and start a fresh run from `main`.

### `KEY_ALIAS secret is missing`

The alias secret is missing or added in the wrong GitHub secret scope.

### `STORE_PASSWORD secret is missing`

The keystore password secret is missing or added in the wrong GitHub secret scope.

### `Alias <name> does not exist`

The `KEY_ALIAS` value does not match the alias inside the keystore.

Check aliases locally:

```bash
keytool -list -v -keystore release-keystore.jks -storetype PKCS12
```

### Keystore password errors

`STORE_PASSWORD` does not match the keystore password, or the Base64 content was generated from the wrong file.

### Signing fails after validation passes

Check `KEY_PASSWORD`. Validation confirms the keystore and alias; signing also needs the private key password.
