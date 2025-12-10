# Android APK Signing Setup Instructions

This document provides instructions for setting up APK signing for automated builds in GitHub Actions.

## Overview

The GitHub Actions workflow (`android-build.yml`) automatically builds and releases your Android app whenever code is pushed to the `main` branch. To sign the APK, you need to:

1. Generate a keystore file
2. Add secrets to your GitHub repository
3. The workflow will automatically use these secrets to sign the APK

## Step 1: Generate a Keystore

A keystore is required to sign Android apps. Run the following command to generate one:

```bash
keytool -genkey -v -keystore keystore.jks -keyalg RSA -keysize 2048 -validity 10000 -alias expense-finance-key
```

You'll be prompted for:
- **Keystore password**: Choose a strong password (you'll need this for GitHub secrets)
- **Key password**: Choose a strong password (can be the same as keystore password)
- **Your name, organization, etc.**: Fill these in as requested

**Important:**
- Keep this keystore file safe and backed up securely
- Never commit the keystore file to version control
- Store the passwords in a secure password manager

## Step 2: Encode Keystore to Base64

GitHub secrets don't support binary files directly, so we need to encode the keystore to Base64:

```bash
base64 -i keystore.jks -o keystore.base64.txt
```

Or on Linux/macOS:
```bash
base64 keystore.jks > keystore.base64.txt
```

This creates a `keystore.base64.txt` file containing the Base64-encoded keystore.

## Step 3: Add Secrets to GitHub Repository

1. Go to your GitHub repository
2. Navigate to **Settings** → **Secrets and variables** → **Actions**
3. Click **New repository secret** and add the following secrets:

### Required Secrets:

| Secret Name | Description | How to Get Value |
|------------|-------------|------------------|
| `KEYSTORE_BASE64` | Base64-encoded keystore file | Copy the entire content of `keystore.base64.txt` |
| `KEYSTORE_PASSWORD` | Password for the keystore | The keystore password you entered when creating the keystore |
| `KEY_ALIAS` | Alias of the signing key | `expense-finance-key` (or whatever alias you used) |
| `KEY_PASSWORD` | Password for the signing key | The key password you entered when creating the keystore |

### Steps to Add Each Secret:

1. Click **New repository secret**
2. Enter the **Name** (e.g., `KEYSTORE_BASE64`)
3. Paste the **Value** (e.g., the content of `keystore.base64.txt`)
4. Click **Add secret**
5. Repeat for all four secrets

## Step 4: Verify the Setup

Once secrets are added:

1. Push code to the `main` branch
2. Go to **Actions** tab in your GitHub repository
3. Watch the workflow run
4. If successful, a signed APK will be uploaded to GitHub Releases

## Workflow Features

The workflow will:
- ✅ Build the app with Java 17 and Android SDK
- ✅ Sign the APK using your keystore (if secrets are configured)
- ✅ Create a GitHub Release with a version tag
- ✅ Upload the signed APK to the release
- ✅ Keep the APK as a workflow artifact for 30 days

## Building Without Signing

If you don't configure the secrets, the workflow will still run and create an **unsigned** APK. This is useful for:
- Testing the workflow
- Debug builds
- Apps that don't require Play Store distribution

## Security Best Practices

1. **Never commit your keystore file** - It's already excluded in `.gitignore`
2. **Store keystore securely** - Keep multiple backups in secure locations
3. **Use strong passwords** - For both keystore and key passwords
4. **Rotate keys periodically** - Consider updating your signing key every few years
5. **Limit access** - Only trusted team members should have access to signing secrets

## Troubleshooting

### Workflow fails with signing errors
- Verify all four secrets are correctly added
- Check that the `KEY_ALIAS` matches the alias used when creating the keystore
- Ensure passwords don't contain special characters that might need escaping

### APK is unsigned
- Check that the `KEYSTORE_BASE64` secret is set correctly
- Look at workflow logs to see if keystore decoding succeeded

### Version conflicts
- Each run creates a unique version by appending the run number
- Format: `v{versionName}-{run_number}` (e.g., `v1.0-42`)

## Additional Information

### Keystore Details
- **Algorithm**: RSA
- **Key Size**: 2048 bits
- **Validity**: 10,000 days (~27 years)
- **Recommended Alias**: `expense-finance-key`

### Files to Keep Secure
- `keystore.jks` - Your keystore file (never commit this)
- `keystore.base64.txt` - Base64 encoded keystore (delete after adding to secrets)
- Keystore password and key password (store in password manager)

### Cleaning Up
After adding secrets to GitHub, you should:
```bash
# Securely delete the base64 file
shred -u keystore.base64.txt  # Linux
# or
rm -P keystore.base64.txt     # macOS

# Keep keystore.jks in a secure location (not in the repo directory)
```

## References

- [Android App Signing](https://developer.android.com/studio/publish/app-signing)
- [GitHub Actions Secrets](https://docs.github.com/en/actions/security-guides/encrypted-secrets)
- [GitHub Actions for Android](https://github.com/android/architecture-samples/tree/main/.github/workflows)
