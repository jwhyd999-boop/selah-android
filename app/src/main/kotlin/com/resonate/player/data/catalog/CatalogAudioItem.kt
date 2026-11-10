package com.resonate.player.data.catalog

/** Remote catalogue identity is a UUID, not a device-local MediaStore ID. */
data class CatalogAudioItem(
    val id: String,
    val categorySlug: String,
    val titleAr: String,
    val titleEn: String?,
    val descriptionAr: String?,
    val descriptionEn: String?,
    val speakerOrPerformer: String?,
    val languageCode: String,
    val audioStoragePath: String,
    val artworkStoragePath: String?,
    val durationMs: Long?,
    val publishedAt: String?,
)

enum class SelahCategory(val slug: String) {
    SERMONS("sermons"), HYMNS("hymns"), PSALMS("psalms"), AGPEYA("agpeya");

    companion object {
        fun fromSlug(value: String): SelahCategory? = entries.firstOrNull { it.slug == value }
    }
}
