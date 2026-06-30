# KEYSTORE_BASE64 invalid input fix

If GitHub Actions fails during keystore validation with:

```text
base64: invalid input
```

then the `KEYSTORE_BASE64` repository secret is not clean Base64 text, or GitHub is still using an old corrupted secret. The source workflow now includes a stronger decoder, but it cannot magically repair a keystore secret that was pasted wrong. Annoying, but physics remains stubborn.

## Correct secrets

```text
KEYSTORE_BASE64 = full one-line Base64 text from clean.txt
STORE_PASSWORD = your keystore password
KEY_PASSWORD = your key password
KEY_ALIAS = freeairock
```

Do not paste the filename `clean.txt` into the secret value.

## Create keystore again in Termux

```bash
rm -f release-keystore.jks clean.txt test.jks

keytool -genkeypair -v \
  -keystore release-keystore.jks \
  -storetype PKCS12 \
  -alias freeairock \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000 \
  -storepass "$PASS" \
  -keypass "$PASS" \
  -dname "CN=Sayanth Rock, OU=Developer, O=FREE AI ROCK, L=Kochi, S=Kerala, C=IN"
```

## Generate one-line Base64

```bash
base64 -w 0 release-keystore.jks > clean.txt
```

If `-w 0` is not available:

```bash
base64 release-keystore.jks | tr -d '\n' > clean.txt
```

## Test before GitHub

```bash
wc -c clean.txt
base64 -d clean.txt > test.jks
echo $?

keytool -list -v \
  -keystore test.jks \
  -storetype PKCS12 \
  -alias freeairock
```

If this passes, the file is valid.

## Force replace GitHub secrets

```bash
gh secret set KEYSTORE_BASE64 -R SayanthRock/FREE-AI-ROCK- < clean.txt
gh secret set KEY_ALIAS -R SayanthRock/FREE-AI-ROCK- --body "freeairock"
gh secret set STORE_PASSWORD -R SayanthRock/FREE-AI-ROCK- --body "$PASS"
gh secret set KEY_PASSWORD -R SayanthRock/FREE-AI-ROCK- --body "$PASS"
```

Verify names:

```bash
gh secret list -R SayanthRock/FREE-AI-ROCK-
```

Expected:

```text
KEYSTORE_BASE64
KEY_ALIAS
KEY_PASSWORD
STORE_PASSWORD
```

## Validate in GitHub Actions

Run:

```text
Actions → Keystore Secret Check → Run workflow → Branch: main
```

Expected success:

```text
KEYSTORE_BASE64 decoded successfully. Alias validated: freeairock
```

Only paste Base64 text. Do not paste `KEYSTORE_BASE64 =`, terminal prompts, spaces, quotes, or the filename.
