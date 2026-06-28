# KEYSTORE_BASE64 invalid input fix

If GitHub Actions fails at `Validate release keystore` with:

```text
base64: invalid input
```

then the `KEYSTORE_BASE64` repository secret is not clean Base64 text.

## Correct secrets

```text
KEYSTORE_BASE64 = full Base64 text from keystore-base64.txt
STORE_PASSWORD = airock
KEY_PASSWORD = airock
KEY_ALIAS = free-ai-rock
```

Do not paste the filename `keystore-base64.txt` into the secret value.

## Create keystore again in Termux

```bash
rm -f release-keystore.jks keystore-base64.txt test-release-keystore.jks

keytool -genkeypair -v \
  -keystore release-keystore.jks \
  -storetype PKCS12 \
  -alias free-ai-rock \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000 \
  -storepass airock \
  -keypass airock \
  -dname "CN=Sayanth Rock, OU=Developer, O=FREE AI ROCK, L=Kochi, S=Kerala, C=IN"
```

## Generate one-line Base64

```bash
base64 -w 0 release-keystore.jks > keystore-base64.txt
```

If `-w 0` is not available:

```bash
base64 release-keystore.jks | tr -d '\n' > keystore-base64.txt
```

## Test before GitHub

```bash
base64 -d keystore-base64.txt > test-release-keystore.jks

keytool -list -v \
  -keystore test-release-keystore.jks \
  -storetype PKCS12 \
  -storepass airock \
  -alias free-ai-rock
```

If this passes, the file is valid.

## Copy clean Base64 to clipboard

```bash
tr -d '\r\n\t ' < keystore-base64.txt | termux-clipboard-set
```

Paste that copied text into:

```text
Settings → Secrets and variables → Actions → Repository secrets → KEYSTORE_BASE64
```

Only paste the Base64 text. Do not paste `KEYSTORE_BASE64 =`, terminal prompts, spaces, or the filename.
