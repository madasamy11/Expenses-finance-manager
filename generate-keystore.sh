#!/bin/bash

# Script to generate Android keystore for APK signing
# This script is for convenience and should be run locally

echo "=========================================="
echo "Android Keystore Generator"
echo "=========================================="
echo ""
echo "This script will help you generate a keystore for signing your Android app."
echo ""

# Check if keytool is available
if ! command -v keytool &> /dev/null; then
    echo "Error: keytool not found. Please install Java JDK."
    exit 1
fi

# Default values
DEFAULT_KEYSTORE="keystore.jks"
DEFAULT_ALIAS="expense-finance-key"
DEFAULT_VALIDITY="10000"
DEFAULT_KEYSIZE="2048"

# Prompt for keystore name
read -p "Enter keystore filename [${DEFAULT_KEYSTORE}]: " KEYSTORE_NAME
KEYSTORE_NAME=${KEYSTORE_NAME:-$DEFAULT_KEYSTORE}

# Prompt for key alias
read -p "Enter key alias [${DEFAULT_ALIAS}]: " KEY_ALIAS
KEY_ALIAS=${KEY_ALIAS:-$DEFAULT_ALIAS}

# Check if keystore already exists
if [ -f "$KEYSTORE_NAME" ]; then
    echo ""
    echo "Warning: Keystore file '$KEYSTORE_NAME' already exists!"
    read -p "Do you want to overwrite it? (yes/no): " OVERWRITE
    if [ "$OVERWRITE" != "yes" ]; then
        echo "Aborted."
        exit 0
    fi
    rm "$KEYSTORE_NAME"
fi

echo ""
echo "Generating keystore..."
echo ""
echo "You will be prompted for:"
echo "  1. Keystore password (remember this!)"
echo "  2. Your name, organization, etc."
echo "  3. Key password (can be same as keystore password)"
echo ""

# Generate the keystore
keytool -genkey -v \
    -keystore "$KEYSTORE_NAME" \
    -keyalg RSA \
    -keysize $DEFAULT_KEYSIZE \
    -validity $DEFAULT_VALIDITY \
    -alias "$KEY_ALIAS"

if [ $? -eq 0 ]; then
    echo ""
    echo "=========================================="
    echo "SUCCESS! Keystore generated."
    echo "=========================================="
    echo ""
    echo "Keystore file: $KEYSTORE_NAME"
    echo "Key alias: $KEY_ALIAS"
    echo ""
    
    # Convert to base64
    echo "Converting keystore to Base64..."
    
    if command -v base64 &> /dev/null; then
        base64 "$KEYSTORE_NAME" > "${KEYSTORE_NAME}.base64.txt"
        echo "Base64 file created: ${KEYSTORE_NAME}.base64.txt"
        echo ""
        echo "=========================================="
        echo "Next Steps:"
        echo "=========================================="
        echo ""
        echo "1. Keep '$KEYSTORE_NAME' safe and backed up securely"
        echo "2. Add the following secrets to your GitHub repository:"
        echo "   - KEYSTORE_BASE64: Content of '${KEYSTORE_NAME}.base64.txt'"
        echo "   - KEYSTORE_PASSWORD: Your keystore password"
        echo "   - KEY_ALIAS: $KEY_ALIAS"
        echo "   - KEY_PASSWORD: Your key password"
        echo ""
        echo "3. See SIGNING_SETUP.md for detailed instructions"
        echo ""
        echo "4. After adding secrets, securely delete the base64 file:"
        echo "   shred -u ${KEYSTORE_NAME}.base64.txt  # Linux"
        echo "   rm -P ${KEYSTORE_NAME}.base64.txt     # macOS"
        echo ""
    else
        echo "Warning: base64 command not found. Please convert manually."
        echo ""
        echo "Run: base64 $KEYSTORE_NAME > ${KEYSTORE_NAME}.base64.txt"
    fi
else
    echo ""
    echo "Error: Failed to generate keystore."
    exit 1
fi
