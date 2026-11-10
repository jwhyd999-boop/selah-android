# SELAH – سِلاه: implementation plan

## Product scope
An Android-only audio app dedicated to Coptic Orthodox content:
- Sermons (عظات)
- Hymns (ترانيم)
- Psalms (مزامير)
- Agpeya / daily hours (الأجبية)

No video playback is part of the product scope.

## Current foundation
The repository is based on an MIT-licensed local Android audio player. Existing useful capabilities include Media3 playback, background media session, notification/lock-screen controls, playlists, favorites, playback speed, history, and lyric parsing. The original MIT license and copyright notice must remain in distributed source and notices.

## Completed in this development branch
- App display name changed to SELAH – سِلاه.
- Android theme resource renamed to Theme.Selah.
- Home screen now introduces the four Coptic Orthodox content categories.
- Arabic resource strings added for core navigation and the new home sections.
- Android per-app locale declarations added for Arabic and English.
- GitHub Actions workflow added for unit tests and debug APK builds.

## Still required before a usable SELAH release
1. Replace the generic local music-library experience with a church-content catalogue and category browsing.
2. Add a complete Arabic translation and verify RTL layouts on real Android screens.
3. Add a backend catalogue schema and authenticated administration path for managing audio, metadata, and lyrics.
4. Configure an owned cloud storage project and delivery policy. Keep service-role credentials off the Android client.
5. Implement secure audio streaming with Media3 while preserving background playback and media-session behavior.
6. Add server-backed favorites/history only if needed; keep local playback preferences and queue behavior resilient offline.
7. Add tests for catalogue mapping, streaming errors, playback restoration, and lyrics.
8. Verify all dependency, font, artwork, and media licenses; retain the upstream MIT notice.
9. Build and smoke-test a debug APK, then prepare a signed release only after a signing key is safely configured.

## External inputs that will eventually be needed
- An owner-controlled backend/storage account (for example, a Supabase project) and its public client configuration.
- Audio files that the project owner has permission to publish, plus metadata and lyrics where available.
- A release signing key and secure backup before publishing an installable production release.

## Important safeguards
- Do not scrape or extract audio from YouTube or other video services.
- Do not publish user recordings without the necessary rights and permission.
- Never embed backend service-role keys or private storage credentials in the Android app.
- Do not replace or remove the upstream MIT license notice.
- Do not publish to Google Play or distribute a release APK without explicit owner approval.
