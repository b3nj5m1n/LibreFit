#!/bin/bash
#
# SPDX-License-Identifier: GPL-3.0-or-later
# Copyright (c) 2026. The LibreFit Contributors
#
# LibreFit is subject to additional terms covering author attribution and trademark usage;
# see the ADDITIONAL_TERMS.md and TRADEMARK_POLICY.md files in the project root.
#

set -e

# Output config
OUTPUT_DIR="repro-out"
APK_PATH="app/build/outputs/apk/release/app-release-unsigned.apk"
rm -rf "$OUTPUT_DIR" && mkdir -p "$OUTPUT_DIR"

echo "🏗️  Building Unsigned APK..."

# Initialize variable for engine-specific arguments
CONTAINER_ARGS=""

# Check if the 'docker' command is actually Podman or Standard Docker
if docker --version | grep -qi "podman"; then
    echo "  > Detected Engine: Podman"
    # Podman specific: maps user ID into the container automatically
    CONTAINER_ARGS="--userns=keep-id"
else
    echo "  > Detected Engine: Docker"
    # Docker specific: Manually run as current user and fix Gradle Home
    # Set GRADLE_USER_HOME because the default /root/.gradle won't be writable
    # shellcheck disable=SC2034
    CONTAINER_ARGS="-u $(id -u):$(id -g) -e GRADLE_USER_HOME=/project/.gradle"
fi

# Docker command with fix for SELinux (:z) and permissions (chmod)
docker run --rm \
    "$CONTAINER_ARGS" \
    -v "$PWD":/project:z \
    android-repro-check \
    /bin/bash -c "chmod +x gradlew && ./gradlew clean assembleRelease --no-daemon"

if [ -f "$APK_PATH" ]; then
    cp "$APK_PATH" "$OUTPUT_DIR/app-release-unsigned.apk"
    echo "✅ Build Successful: $OUTPUT_DIR/app-release-unsigned.apk"
    # Print Hash for logs
    sha256sum "$OUTPUT_DIR/app-release-unsigned.apk"
else
    echo "❌ Build Failed: APK not found."
    exit 1
fi