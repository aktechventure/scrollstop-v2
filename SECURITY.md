# Security

This document is an implementation-specific security review for ScrollStop V0.1. It reflects the current repository state and is not an OWASP certification claim.

## Scope

The app is a local-first Android application focused on usage analysis and interventions for selected apps. It stores local data in Room and does not integrate with a backend, cloud service, authentication system, or web API.

## Security posture

- No secrets, API keys, passwords, or tokens are present in source or build configuration.
- No internet permission is declared.
- No cloud SDKs or analytics SDKs are present in the app module.
- The app does not request or use location, contacts, SMS, microphone, calendar, photos, or financial permissions.
- The accessibility service is declared intentionally for the app's intervention flow and remains scope-limited to the app package and a narrow set of monitored packages.
- Local data is stored in the app-private SQLite database only.

## Security-relevant configuration

- `android:allowBackup="false"` disables automatic device backup for app data.
- `android:usesCleartextTraffic="false"` prevents non-TLS network traffic.
- Release build minification and resource shrinking are enabled.
- Room database is local-only and data reset is available via the settings flow.

## Known limitations

- Android accessibility APIs are inherently privileged and require user consent. The app must not claim to be secure against malicious or abusive accessibility behavior without device-level verification.
- Local database exposure remains possible if the device itself is rooted or the user exports the app data externally.
- Real-device verification is still required for the full AccessibilityService and overlay interaction path.
