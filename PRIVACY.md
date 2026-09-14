# Privacy

## Data minimization

ScrollStop V0.1 only stores data needed for local usage analysis, interventions, reflections, insights, and user preferences.

The current local data model includes:

- app usage timestamps and package names
- intervention session metadata
- reflection entries
- insight entries
- user preferences such as target apps, quiet hours, threshold, theme, and privacy mode

## Explicitly not collected

The app does not request, store, or transmit:

- contacts
- SMS messages
- location
- microphone input
- photos or media
- calendar events
- financial information
- family or relationship information
- birthdays
- arbitrary screen content

## Storage and retention

- Data is stored in the app-private Room database in the local device sandbox.
- The app supports a local delete-all flow that removes user data and resets relevant preferences.
- Backup and cloud extraction are disabled for app data and settings.

## Consent and system permissions

- The app requests only the Android permissions needed for the V0.1 intervention flow: usage access, overlay, and accessibility.
- No network permission is declared.
- No background data sync or external service is used.

## Risk status

The current implementation is privacy-minimal for the scoped V0.1 product, but it still stores usage metadata and app package names locally, which is necessary for the product but must be treated as personal behavioral data.
