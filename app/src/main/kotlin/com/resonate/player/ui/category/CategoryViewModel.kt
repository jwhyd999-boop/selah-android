package com.resonate.player.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resonate.player.data.catalog.CatalogAudioItem
import com.resonate.player.data.catalog.CatalogRepository
import com.resonate.player.data.catalog.SelahCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

data class CategoryCatalogState(
    val loading: Boolean = true,
    val items: List<CatalogAudioItem> = emptyList(),
    val error: Throwable? = null,
)

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val catalogRepository: CatalogRepository,
) : ViewModel() {
    private val _state = kotlinx.coroutines.flow.MutableStateFlow(CategoryCatalogState())
    val state: kotlinx.coroutines.flow.StateFlow<CategoryCatalogState> = _state

    fun load(categorySlug: String) {
        val category = SelahCategory.fromSlug(categorySlug)
        if (category == null) {
            _state.value = CategoryCatalogState(loading = false, error = IllegalArgumentException("Unknown category"))
            return
        }
        viewModelScope.launch {
            _state.value = CategoryCatalogState(loading = true)
            try {
                _state.value = CategoryCatalogState(
                    loading = false,
                    items = catalogRepository.listPublished(category),
                )
            } catch (error: Exception) {
                _state.value = CategoryCatalogState(loading = false, error = error)
            }
        }
    }
}
