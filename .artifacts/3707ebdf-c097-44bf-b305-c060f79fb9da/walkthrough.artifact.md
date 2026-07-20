# Walkthrough - SDK and Build Configuration Fix

I have successfully fixed the SDK version mismatch and dependency conflicts by upgrading the build toolchain.

## Changes Made

### Build Configuration
- **Upgraded Android Gradle Plugin (AGP)**: Moved from `9.0.1` to `9.3.0` to support dependencies that require higher API levels and plugin features.
- **Updated SDK Versions**:
    - `compileSdk` set to **37** (required by Hilt and Lifecycle libraries).
    - `targetSdk` set to **35**.
- **Centralized Versions**: Moved `compileSdk` and `targetSdk` into `libs.versions.toml` for easier management.
- **Upgraded Gradle Wrapper**: Updated Gradle to **9.5.0** as required by AGP 9.3.0.

### Files Modified
- [libs.versions.toml](file:///C:/Sholin'sHUB/Note_App/gradle/libs.versions.toml): Updated `agp`, added `compileSdk` and `targetSdk`.
- [app/build.gradle.kts](file:///C:/Sholin'sHUB/Note_App/app/build.gradle.kts): Updated to use version catalog for SDK versions.
- [gradle-wrapper.properties](file:///C:/Sholin'sHUB/Note_App/gradle/wrapper/gradle-wrapper.properties): Upgraded Gradle version and updated checksum handling.

## Verification Results

### Automated Tests
- `gradle_sync`: Finished successfully.
- `app:assembleDebug`: Build finished successfully.

> [!NOTE]
> The project is now using highly experimental/preview versions of AGP and Gradle. This was necessary to satisfy the requirements of the `androidx.hilt:hilt-navigation-compose:1.4.0` and `androidx.lifecycle` libraries currently in use.
