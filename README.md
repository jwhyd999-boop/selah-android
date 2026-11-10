# SELAH – سِلاه

**تطبيق صوتي للأندرويد متخصص في الكنيسة القبطية الأرثوذكسية.**

SELAH is being developed for audio-only listening to Coptic Orthodox content:

- **عظات / Sermons**
- **ترانيم / Hymns**
- **مزامير / Psalms**
- **الأجبية / Agpeya**

The app interface is being prepared in Arabic and English, with right-to-left support.

## Current development status

This repository contains the first SELAH foundation, not a finished public release.

- The home screen presents the four content sections.
- Each section now requests its published recordings from the read-only Supabase REST client and displays loading, empty, error, and retry states.
- Selecting a published catalogue recording hands its HTTPS storage URL to the existing Media3 MediaSession for playback; device-local library playback remains available.
- The catalogue client is not connected to a live Supabase project yet, so the app will show a clear not-configured state until project settings and approved audio are supplied.
- The Android application ID is `com.selah.audio`; the current version is `1.0.0`.
- Internet permission is present for the planned catalogue and streaming integration.
- A Supabase SQL migration is prepared under `supabase/migrations/`, but it has **not** been applied to a project.
- No audio recordings have been uploaded or published.
- The original local-audio playback foundation remains in the codebase; remote catalogue playback still needs implementation and verification.

**Do not distribute the APKs in `release/` as SELAH.** They are legacy Resonate builds, not SELAH releases.

## Content and rights

Only recordings that SELAH is authorized to distribute should be uploaded or published. Do not extract audio from YouTube or publish third-party recordings without permission.

The planned catalogue stores category metadata, audio metadata, and optional plain or timed lyrics. Public clients may read published catalogue entries only. A Supabase service-role key must never be placed in the Android app.

See [the Supabase setup notes](supabase/README.md) before configuring a backend.

## Android project

- Language: Kotlin
- UI: Jetpack Compose and Material 3
- Playback foundation: AndroidX Media3
- Local persistence: Room and DataStore
- Dependency injection: Hilt
- Android namespace retained from the MIT-licensed starter: `com.resonate.player`
- Android application ID: `com.selah.audio`
- Minimum Android version: API 26

The namespace is an internal source-code identifier; the installed app identity is `com.selah.audio`.

## Verification

GitHub Actions is configured in `.github/workflows/android-ci.yml` to run:

- `./gradlew testDebugUnitTest`
- `./gradlew assembleDebug`

Check the [Actions page](https://github.com/jwhyd999-boop/selah-android/actions) for the current result. A queued run is not a successful build. No release APK should be considered verified until the workflow completes successfully and the resulting app is smoke-tested.

## License and attribution

This project builds on an MIT-licensed Android audio-player foundation. The original MIT license and copyright notices are retained in [LICENSE](LICENSE). The existing starter code is not being represented as wholly original or exclusively owned by SELAH.

SELAH-specific branding, product configuration, and new contributions are developed in this repository while preserving the applicable upstream license terms.
