package com.example.marvelapp.presentation.detail

import androidx.lifecycle.ViewModel
import com.example.core.usecase.AddFavoriteUseCase
import com.example.core.usecase.CheckFavoriteUseCase
import com.example.core.usecase.GetCharacterCategoriesUseCase
import com.example.core.usecase.RemoveFavoriteUseCase
import com.example.core.usecase.base.CoroutineDispatchers
import com.example.marvelapp.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    getCharacterCategoriesUseCase: GetCharacterCategoriesUseCase,
    checkFavoriteUseCase: CheckFavoriteUseCase,
    addFavoriteUseCase: AddFavoriteUseCase,
    removeFavoriteUseCase: RemoveFavoriteUseCase,
    coroutineDispatchers: CoroutineDispatchers,
) : ViewModel() {

    val categories = UiActionStateLiveData(
        coroutineDispatchers.main(),
        getCharacterCategoriesUseCase
    )

    val favorite = FavoriteUiActionStateLiveData(
        coroutineDispatchers.main(),
        checkFavoriteUseCase,
        addFavoriteUseCase,
        removeFavoriteUseCase
    )

//    private val _uiState = MutableLiveData<UiState>()
//    val uiState : LiveData<UiState> get() = _uiState

//    private val _favoriteUiState = MutableLiveData<FavoriteUiState>()
//    val favoriteUiState : LiveData<FavoriteUiState> get() = _favoriteUiState

//    init {
//        favorite.setDefault()
//        _favoriteUiState.value = FavoriteUiState.FavoriteIcon(R.drawable.ic_favorite_unchecked)
//    }

//    fun getCharacterCategories(characterId: Int) = viewModelScope.launch {
//        getComicsUseCase(GetComicsUseCase.GetComicsParams(characterId))
//            .collect { status ->
//                when (status) {
//                    ResultStatus.Loading -> UiState.Loading
//                    is ResultStatus.Success -> UiState.Success(status.data)
//                    is ResultStatus.Error -> UiState.Error
//                }
//            }

//        getCharacterCategoriesUseCase(GetCharacterCategoriesUseCase.GetCategoriesParams(characterId))
//            .watchStatus(
//                loading = {
//                    _uiState.value = UiState.Loading
//                },
//                success = { data ->
//                    val detailParentList = mutableListOf<DetailParentVE>()
//
//                    val comics = data.first
//
//                    if (comics.isNotEmpty()) {
//                        comics.map {
//                            DetailChildVE(it.id, it.imageUrl)
//                        }.also {
//                            detailParentList.add(
//                                DetailParentVE(R.string.details_comics_category, it)
//                            )
//                        }
//                    }
//
//                    val events = data.second
//
//                    if (events.isNotEmpty()) {
//                        events.map {
//                            DetailChildVE(it.id, it.imageUrl)
//                        }.also {
//                            detailParentList.add(
//                                DetailParentVE(R.string.details_events_category, it)
//                            )
//                        }
//                    }
//
//                    _uiState.value = if (detailParentList.isNotEmpty()) {
//                        UiState.Success(detailParentList)
//                    } else UiState.Empty
//                },
//                error = {
//                    _uiState.value = UiState.Error
//                }
//            )
//    }

//    private fun Flow<ResultStatus<Pair<List<Comic>, List<Event>>>>.watchStatus() =
//        viewModelScope.launch {
//            collect { status ->
//                _uiState.value = when (status) {
//                    ResultStatus.Loading -> UiState.Loading
//                    is ResultStatus.Success -> {
////                        val detailChildList = status.data.map { DetailChildVE(it.id, it.imageUrl) }
////
////                        val detailParentList = listOf(
////                            DetailParentVE(
////                                R.string.details_comics_category,
////                                detailChildList
////                            )
////                        )
//
////                        UiState.Success(detailParentList)
//
//                        val detailParentList = mutableListOf<DetailParentVE>()
//
//                        val comics = status.data.first
//
//                        if (comics.isNotEmpty()) {
//                            comics.map {
//                                DetailChildVE(it.id, it.imageUrl)
//                            }.also {
//                                detailParentList.add(
//                                    DetailParentVE(R.string.details_comics_category, it)
//                                )
//                            }
//                        }
//
//                        val events = status.data.second
//
//                        if (events.isNotEmpty()) {
//                            events.map {
//                                DetailChildVE(it.id, it.imageUrl)
//                            }.also {
//                                detailParentList.add(
//                                    DetailParentVE(R.string.details_events_category, it)
//                                )
//                            }
//                        }
//
//                        if (detailParentList.isNotEmpty()) {
//                            UiState.Success(detailParentList)
//                        } else UiState.Empty
//
//                    }
//                    is ResultStatus.Error -> UiState.Error
//                }
//            }
//    }

//    fun updateFavorite(detailViewArg: DetailViewArg) = viewModelScope.launch {
//        detailViewArg.run {
//            addFavoriteUseCase.invoke(
//                AddFavoriteUseCase.Params(
//                    characterId,
//                    name,
//                    imageUrl
//                )
//            ).watchStatus(
//                loading = {
//                    _favoriteUiState.value = FavoriteUiState.Loading
//                },
//                success = {
//                    _favoriteUiState.value = FavoriteUiState.FavoriteIcon(R.drawable.ic_favorite_checked)
//                },
//                error = {}
//            )
//        }
//    }

//    sealed class UiState {
//        object Loading : UiState()
//        data class Success(val detailParentList: List<DetailParentVE>) : UiState()
//        object Error : UiState()
//        object Empty : UiState()
//
//    }

//    sealed class FavoriteUiState {
//        object Loading : FavoriteUiState()
//        class FavoriteIcon (@DrawableRes val icon: Int) : FavoriteUiState()
//    }
}