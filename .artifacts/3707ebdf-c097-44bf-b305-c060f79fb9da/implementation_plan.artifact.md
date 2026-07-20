# Implementation Plan - Fix Target and Compile SDK Versions

The project currently has a mismatch between `compileSdk` (37) and `targetSdk` (35). Additionally, the build is failing because the current Android Gradle Plugin (AGP) version (9.0.1) is lower than what some dependencies require (9.1.0).

## Proposed Changes

### Build Configuration

#### [MODIFY] [app/build.gradle.kts](file:///C:/Sholin'sHUB/Note_App/app/build.gradle.kts)
- Update `compileSdk` to 35 to align with the current stable Android 15 SDK.
- Ensure `targetSdk` is set to 35.
- (Optional but recommended) Move these versions to `libs.versions.toml` for better management.

#### [MODIFY] [gradle/libs.versions.toml](file:///C:/Sholin'sHUB/Note_App/gradle/libs.versions.toml)
- Add `compileSdk` and `targetSdk` versions to the `[versions]` section.
- Update `agp` version from `9.0.1` to `9.1.0` to resolve dependency conflicts.

## Verification Plan

### Automated Tests
- Run `./gradlew assembleDebug` to ensure the project builds successfully with the updated SDK and AGP versions.

### Manual Verification
- Verify that the IDE syncs successfully and no lint warnings regarding target SDK are present.
