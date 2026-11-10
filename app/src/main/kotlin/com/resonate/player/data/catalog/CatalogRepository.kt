package com.resonate.player.data.catalog

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
class CatalogRepository @Inject constructor(
    private val client: SelahCatalogClient,
) {
    suspend fun listPublished(category: SelahCategory): List<CatalogAudioItem> =
        withContext(Dispatchers.IO) { client.listPublished(category) }

    fun audioUrl(item: CatalogAudioItem): String = client.publicAudioUrl(item.audioStoragePath)

    fun artworkUrl(item: CatalogAudioItem): String? =
        client.publicArtworkUrl(item.artworkStoragePath)
}
