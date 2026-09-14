# Changelog

## Unreleased

- Updated Android compile and target SDK targets to API 36.
- Added externalized release signing configuration without committing credentials.
- Hardened release build settings with minification and resource shrinking.
- Disabled automatic app backup and data extraction for local behavioral data.
- Added CI workflow for compile, lint, detekt, and unit tests.
- Added architecture, testing, and release documentation aligned to the actual implementation.

## V0.1

- Local-first usage tracking using Android UsageStats
- Room-backed local persistence for preferences, usage, reflections, insights, and session data
- Accessibility-based intervention flow with overlay prompt
- Deterministic intervention engine for threshold, quiet hours, app targeting, and deduplication
- Settings support for targeted apps, local data deletion, and privacy-related toggles
