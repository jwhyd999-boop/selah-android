# SELAH backend setup notes

The SQL migration in this folder defines the first Supabase catalogue schema. It is prepared but has **not** been applied to a Supabase project.

## Tables
- `selah_categories`: the four fixed Coptic Orthodox sections and Arabic/English labels.
- `selah_audio_items`: audio metadata, storage paths, publication state, and speaker/performer.
- `selah_lyrics`: plain lyrics and optional LRC timed lyrics.

Anonymous clients can read categories and published catalogue rows only. The Android app must never receive a Supabase service-role key.

## Storage
The migration creates a public `selah-audio` bucket for recordings approved for public distribution. Only upload material when the project owner has permission to publish it. Upload files through the Supabase dashboard or a trusted server; do not add upload credentials to the Android app.

Suggested object paths:
`<category>/<audio-item-uuid>/<filename>.mp3`

## What is still needed
1. Create a Supabase project controlled by the app owner.
2. Review and apply `migrations/202611110001_selah_catalog.sql`.
3. Provide the project URL and public anon key through a safe app configuration mechanism (not a service-role key).
4. Implement and verify the Android catalogue client and Media3 remote playback integration.
5. Add actual licensed recordings and metadata, then publish selected rows.

The migration is a starting schema, not a configured or running backend.
