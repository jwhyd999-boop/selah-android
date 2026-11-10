# SELAH catalogue integration contract

This document is the implementation contract for connecting the Android app to the
Supabase schema in `supabase/migrations/202611110001_selah_catalog.sql`.

## Current state

- The Android app has no Supabase URL, public anon key, or configured project.
- The SQL migration is a proposal in this repository; it has not been applied.
- No audio objects have been uploaded and no remote catalogue is available.
- The current Media3 service is initialized for local-library playback. Do not claim
  remote streaming works until remote MediaItems are exercised on a device.

## Public read contract

Use the Supabase REST API with the project's HTTPS URL and publishable/anon key.
The key is a public client credential, not a secret. Never put a service-role key in
the APK, BuildConfig, repository, or CI logs.

Read only published rows:

- `selah_categories`: `slug,name_ar,name_en,description_ar,description_en,sort_order`
- `selah_audio_items`: `id,category_slug,title_ar,title_en,description_ar,description_en,speaker_or_performer,language_code,audio_storage_path,artwork_storage_path,duration_ms,published_at`
- `selah_lyrics`: `audio_item_id,plain_text,lrc_text,updated_at`

Filter catalogue items by `category_slug`, sort by `published_at.desc`, and rely on
Row Level Security to expose published items only. The client must still handle an
empty result, offline state, malformed metadata, and HTTP errors.

## Audio playback contract

Resolve `audio_storage_path` to an HTTPS object URL in the `selah-audio` bucket.
Pass that URL as the Media3 MediaItem URI while preserving the stable catalogue UUID
as the media ID. Set title, performer, artwork URI, and duration metadata when present.
The existing MediaSessionService should remain the single playback owner so lock-screen,
notification, headset, and background controls continue to work.

Use normal HTTP range-capable audio delivery. Do not download the whole recording
before playback. Keep local MediaStore playback working alongside catalogue streams.
Never extract audio from YouTube or another video service.

## Lyrics

Prefer `lrc_text` when available and parse it through the existing LRC presentation
path; otherwise show `plain_text`. Missing lyrics must not prevent playback.

## Release and content safeguards

1. Apply the migration to a dedicated Supabase project and verify RLS as anon.
2. Upload only recordings and artwork approved for public distribution.
3. Verify public object URLs and range requests before publishing catalogue rows.
4. Publish a test item and confirm it appears in exactly one intended category.
5. Test Arabic RTL, English LTR, offline/empty/error states, background playback,
   interruption handling, and app restart on a physical Android device.
6. Store only the Supabase project URL and public anon/publishable key in app config.
   Keep all privileged upload and moderation actions server-side.

No credentials or real audio URLs belong in this document or in source control.
