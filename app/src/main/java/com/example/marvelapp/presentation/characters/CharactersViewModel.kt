package com.example.marvelapp.presentation.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.core.domain.model.Character
import com.example.core.usecase.GetCharactersUseCase
import com.example.core.usecase.base.CoroutineDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val getCharactersUseCase : GetCharactersUseCase,
    coroutinesDispatchers: CoroutineDispatchers
) : ViewModel() {

    var currentSearchQuery = ""

    private val action = MutableLiveData<Action>()
    val state: LiveData<UiState> = action
        .distinctUntilChanged()
        .switchMap { action ->
             when (action) {
                is Action.Search, Action.Sort -> {
                    getCharactersUseCase(
                        GetCharactersUseCase.GetCharactersParams(currentSearchQuery, getPagingConfig())
                    ).cachedIn(viewModelScope).map {
                        UiState.SearchResult(it)
                    }.asLiveData(coroutinesDispatchers.main())
                }
             }
        }


    fun charactersPagingData(query: String): Flow<PagingData<Character>> {
        return getCharactersUseCase(
            GetCharactersUseCase.GetCharactersParams(query, getPagingConfig())
        ).cachedIn(viewModelScope)
    }

    private fun getPagingConfig() = PagingConfig(
        pageSize = 20
    )

    fun searchCharacters() {
        action.value = Action.Search
    }

    fun applySort() {
        action.value = Action.Sort
    }

    fun closeSort() {
        if (currentSearchQuery.isNotEmpty()) {
            currentSearchQuery = ""
        }
    }

    sealed class UiState {
        data class SearchResult(val data: PagingData<Character>) : UiState()
    }

    sealed class Action {
        object Search : Action()
        object Sort : Action()
    }
}