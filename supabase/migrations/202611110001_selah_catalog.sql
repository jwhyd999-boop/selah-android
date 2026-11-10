-- SELAH public catalogue foundation.
-- Keep this bucket for recordings that are approved for public streaming only.
create extension if not exists pgcrypto;

create table if not exists public.selah_categories (
    slug text primary key check (slug in ('sermons', 'hymns', 'psalms', 'agpeya')),
    name_ar text not null,
    name_en text not null,
    description_ar text,
    description_en text,
    sort_order integer not null default 0
);

insert into public.selah_categories
    (slug, name_ar, name_en, description_ar, description_en, sort_order)
values
    ('sermons', 'عظات', 'Sermons', 'تعليم ووعظ قبطي أرثوذكسي', 'Coptic Orthodox teaching and sermons', 1),
    ('hymns', 'ترانيم', 'Hymns', 'تسابيح وألحان كنسية', 'Church praise and hymns', 2),
    ('psalms', 'مزامير', 'Psalms', 'تلاوات وتأملات من سفر المزامير', 'Readings and reflections from the Psalms', 3),
    ('agpeya', 'الأجبية', 'Agpeya', 'صلوات السواعي اليومية', 'The Coptic daily hours and prayers', 4)
on conflict (slug) do update set
    name_ar = excluded.name_ar,
    name_en = excluded.name_en,
    description_ar = excluded.description_ar,
    description_en = excluded.description_en,
    sort_order = excluded.sort_order;

create table if not exists public.selah_audio_items (
    id uuid primary key default gen_random_uuid(),
    category_slug text not null references public.selah_categories(slug),
    title_ar text not null,
    title_en text,
    description_ar text,
    description_en text,
    speaker_or_performer text,
    language_code text not null default 'ar',
    audio_storage_path text not null unique,
    artwork_storage_path text,
    duration_ms bigint check (duration_ms is null or duration_ms >= 0),
    is_published boolean not null default false,
    published_at timestamptz,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

create index if not exists selah_audio_category_published_idx
    on public.selah_audio_items (category_slug, is_published, published_at desc);

create table if not exists public.selah_lyrics (
    id uuid primary key default gen_random_uuid(),
    audio_item_id uuid not null unique
        references public.selah_audio_items(id) on delete cascade,
    plain_text text,
    lrc_text text,
    updated_at timestamptz not null default now(),
    check (plain_text is not null or lrc_text is not null)
);

alter table public.selah_categories enable row level security;
alter table public.selah_audio_items enable row level security;
alter table public.selah_lyrics enable row level security;

drop policy if exists "SELAH categories are readable" on public.selah_categories;
create policy "SELAH categories are readable"
    on public.selah_categories for select
    to anon, authenticated
    using (true);

drop policy if exists "Published SELAH audio is readable" on public.selah_audio_items;
create policy "Published SELAH audio is readable"
    on public.selah_audio_items for select
    to anon, authenticated
    using (is_published = true);

drop policy if exists "Lyrics for published SELAH audio are readable" on public.selah_lyrics;
create policy "Lyrics for published SELAH audio are readable"
    on public.selah_lyrics for select
    to anon, authenticated
    using (
        exists (
            select 1
            from public.selah_audio_items item
            where item.id = audio_item_id
              and item.is_published = true
        )
    );

grant select on public.selah_categories to anon, authenticated;
grant select on public.selah_audio_items to anon, authenticated;
grant select on public.selah_lyrics to anon, authenticated;

-- Public bucket: upload only recordings already approved for public distribution.
-- Do not add client-side INSERT/UPDATE/DELETE policies. Upload through the dashboard
-- or a trusted server using a service-role key that never ships in the Android app.
insert into storage.buckets (id, name, public, file_size_limit, allowed_mime_types)
values (
    'selah-audio',
    'selah-audio',
    true,
    157286400,
    array['audio/mpeg', 'audio/mp4', 'audio/aac', 'audio/flac', 'audio/ogg', 'audio/opus', 'audio/wav']
)
on conflict (id) do update set
    public = excluded.public,
    file_size_limit = excluded.file_size_limit,
    allowed_mime_types = excluded.allowed_mime_types;
