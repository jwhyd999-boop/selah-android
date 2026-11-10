package com.resonate.player.data.catalog

import com.resonate.player.BuildConfig
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import org.json.JSONArray

/** Public, read-only Supabase REST access. Privileged keys must never ship in the app. */
class SelahCatalogClient(
    private val baseUrl: String = BuildConfig.SUPABASE_URL,
    private val anonKey: String = BuildConfig.SUPABASE_ANON_KEY,
    private val connectionFactory: (URL) -> HttpURLConnection = {
        it.openConnection() as HttpURLConnection
    },
) {
    fun listPublished(category: SelahCategory): List<CatalogAudioItem> {
        requireConfigured()
        val columns = "id,category_slug,title_ar,title_en,description_ar,description_en," +
            "speaker_or_performer,language_code,audio_storage_path,artwork_storage_path," +
            "duration_ms,published_at"
        val query = "select=$columns&category_slug=eq.${encode(category.slug)}" +
            "&is_published=eq.true&order=published_at.desc"
        val connection = connectionFactory(
            URL("${baseUrl.trimEnd('/')}/rest/v1/selah_audio_items?$query")
        )
        try {
            connection.requestMethod = "GET"
            connection.connectTimeout = 10_000
            connection.readTimeout = 15_000
            connection.setRequestProperty("apikey", anonKey)
            connection.setRequestProperty("Authorization", "Bearer $anonKey")
            connection.setRequestProperty("Accept", "application/json")
            val status = connection.responseCode
            val stream = if (status in 200..299) connection.inputStream else connection.errorStream
            val body = stream?.bufferedReader(Charsets.UTF_8)?.use { it.readText() }.orEmpty()
            if (status !in 200..299) throw IOException("SELAH catalogue request failed (HTTP $status)")
            return parse(JSONArray(body))
        } finally {
            connection.disconnect()
        }
    }

    fun publicAudioUrl(path: String): String {
        requireConfigured()
        return publicObjectUrl(path)
    }

    fun publicArtworkUrl(path: String?): String? =
        path?.takeIf(String::isNotBlank)?.let(::publicObjectUrl)

    private fun publicObjectUrl(path: String): String {
        val encodedPath = path.split('/').joinToString("/") { encode(it) }
        return "${baseUrl.trimEnd('/')}/storage/v1/object/public/selah-audio/$encodedPath"
    }

    private fun requireConfigured() {
        check(baseUrl.isNotBlank() && anonKey.isNotBlank()) {
            "SELAH catalogue is not configured. Add SUPABASE_URL and SUPABASE_ANON_KEY."
        }
        require(baseUrl.startsWith("https://")) { "SELAH catalogue URL must use HTTPS." }
    }

    private fun parse(array: JSONArray): List<CatalogAudioItem> = buildList {
        for (index in 0 until array.length()) {
            val item = array.getJSONObject(index)
            val id = item.optString("id").takeIf(String::isNotBlank) ?: continue
            val category = item.optString("category_slug")
            if (SelahCategory.fromSlug(category) == null) continue
            val title = item.optString("title_ar").takeIf(String::isNotBlank) ?: continue
            val audioPath = item.optString("audio_storage_path").takeIf(String::isNotBlank) ?: continue
            add(
                CatalogAudioItem(
                    id = id,
                    categorySlug = category,
                    titleAr = title,
                    titleEn = item.nullableString("title_en"),
                    descriptionAr = item.nullableString("description_ar"),
                    descriptionEn = item.nullableString("description_en"),
                    speakerOrPerformer = item.nullableString("speaker_or_performer"),
                    languageCode = item.optString("language_code", "ar"),
                    audioStoragePath = audioPath,
                    artworkStoragePath = item.nullableString("artwork_storage_path"),
                    durationMs = if (item.isNull("duration_ms")) null
                        else item.optLong("duration_ms").takeIf { it >= 0L },
                    publishedAt = item.nullableString("published_at"),
                )
            )
        }
    }

    private fun org.json.JSONObject.nullableString(key: String): String? =
        optString(key).takeIf { it.isNotBlank() && it != "null" }

    private fun encode(value: String): String =
        URLEncoder.encode(value, Charsets.UTF_8.name()).replace("+", "%20")
}
