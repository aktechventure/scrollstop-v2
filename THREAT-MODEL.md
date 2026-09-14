# Threat Model

## Assets

- App usage event data stored locally in Room.
- User preferences and monitoring configuration.
- Reflection and intervention session data generated for local insight summaries.
- Accessibility and overlay permissions required for the local intervention experience.

## Trust boundaries

- User device OS and Android permission model.
- App process boundary.
- Local Room database inside app private storage.
- AccessibilityService and overlay features granted by the user.

## Threats considered

### 1. Malicious third-party app

A malicious app may attempt to trigger accessibility or overlay-based abuse. This app limits behavior to a small set of monitored package names and does not export privileged components beyond the required main activity and accessibility service.

### 2. Exported component abuse

The main activity is exported for launcher entry. The accessibility service is exported because Android requires it for accessibility activation, but it is protected by `android.permission.BIND_ACCESSIBILITY_SERVICE` and only enabled when the user explicitly grants accessibility access.

### 3. AccessibilityService misuse

The service watches accessibility events and triggers a prompt only for a limited set of monitored packages. It does not read arbitrary screen content beyond the package name and window state events.

### 4. Overlay misuse

The overlay is used only for the in-app intervention question surface and does not request or access external content or send data anywhere.

### 5. Local database exposure

The app stores behavioral data locally in Room. Automatic backup and cloud extraction are disabled to reduce accidental exposure.

### 6. Backup extraction

Backup rules and data extraction rules explicitly exclude the private database and permission history state.

### 7. Debug build leakage

The project currently uses debug configurations for local build/test work. This is not a production security claim; release build hardening is enabled, but production signing and Play/App Store review remain required.

### 8. Accidental sensitive logging

No logging statements are present in the production app code, and no network or cloud dependencies are included.

## Residual risks

- The app relies on Android OS permission enforcement; the user must grant accessibility and overlay access intentionally.
- A rooted or compromised device can still expose local app data.
- The service is still an accessibility feature and must be reviewed on-device for behavioral correctness and privacy impact at runtime.
