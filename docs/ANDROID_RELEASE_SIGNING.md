# Android Release Signing Checklist

This project builds release APK/AAB files with GitHub Actions. Signing requires a private Android keystore stored through GitHub Actions Repository Secrets.

## Current release pipeline

The release workflow is:

```text
.github/workflows/android-release.yml
```

It performs these stages:

1. Resolve the release version and tag.
2. Decode and validate signing secrets when all four signing secrets exist.
3. Build release APK and AAB.
4. Sign release outputs automatically when the secrets are valid.
5. Upload release artifacts and publish/update the GitHub Release.

If signing secrets are missing, the workflow still builds unsigned release artifacts so the Android build can be verified. If signing secrets are present but wrong, the workflow fails early with a clear validation error, because silently shipping a broken signed release would be peak software comedy.

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
  -alias freeairock \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000
```

Recommended secret values for this generated keystore:

```text
KEY_ALIAS=freeairock
STORE_PASSWORD=<the keystore password you entered>
KEY_PASSWORD=<the key password you entered>
```

If you used the same password for both store and key, set `STORE_PASSWORD` and `KEY_PASSWORD` to the same value.

## Convert keystore to Base64

Linux, Termux, macOS, or Git Bash:

```bash
base64 -w 0 release-keystore.jks > clean.txt
```

Windows PowerShell:

```powershell
[Convert]::ToBase64String([IO.File]::ReadAllBytes("release-keystore.jks")) | Set-Content -NoNewline clean.txt
```

Copy the full one-line content of `clean.txt` into the `KEYSTORE_BASE64` Repository Secret.

## Fix `base64: invalid input`

This error means the `KEYSTORE_BASE64` secret is not clean Base64 text or GitHub still has an old corrupted value.

Use Termux to regenerate and verify clean text:

```bash
base64 -w 0 release-keystore.jks > clean.txt
wc -c clean.txt
base64 -d clean.txt > test.jks
echo $?
keytool -list -v \
  -keystore test.jks \
  -storetype PKCS12 \
  -alias freeairock
```

Then force replace the secret:

```bash
gh secret set KEYSTORE_BASE64 -R SayanthRock/FREE-AI-ROCK- < clean.txt
gh secret set KEY_ALIAS -R SayanthRock/FREE-AI-ROCK- --body "freeairock"
gh secret set STORE_PASSWORD -R SayanthRock/FREE-AI-ROCK- --body "$PASS"
gh secret set KEY_PASSWORD -R SayanthRock/FREE-AI-ROCK- --body "$PASS"
```

Do not paste `KEYSTORE_BASE64 =`, terminal prompts, spaces, or the filename `clean.txt`.

## Validate secrets before release

Run this workflow after updating secrets:

```text
Actions → Keystore Secret Check → Run workflow → Branch: main
```

Expected success message:

```text
KEYSTORE_BASE64 decoded successfully. Alias validated: freeairock
```

## Trigger a fresh release workflow run

For manual verification:

```text
Actions → Android Release Build → Run workflow → Branch: main
```

Do not use the old failed run's `Re-run jobs` button when testing changed secrets or workflow changes.

For production GitHub Release publishing:

```bash
git tag v1.0.3
git push origin v1.0.3
```

The release workflow publishes GitHub Releases for tags matching `v*` and can also create/update the version tag when manually dispatched.

## Expected success signals

Build artifact:

```text
FREE-AI-ROCK-release-artifacts
```

Signing validation:

```text
Release signing is enabled.
```

If secrets are incomplete:

```text
Signing secrets are incomplete. Building unsigned release artifacts.
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
