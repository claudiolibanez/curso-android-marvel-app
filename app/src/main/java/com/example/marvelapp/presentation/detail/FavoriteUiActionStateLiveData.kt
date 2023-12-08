package com.example.marvelapp.presentation.detail

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.example.core.usecase.AddFavoriteUseCase
import com.example.core.usecase.CheckFavoriteUseCase
import com.example.core.usecase.RemoveFavoriteUseCase
import com.example.marvelapp.R
import com.example.marvelapp.presentation.extensions.watchStatus
import kotlin.coroutines.CoroutineContext

class FavoriteUiActionStateLiveData(
    private val coroutineContext: CoroutineContext,
    private val checkFavoriteUseCase: CheckFavoriteUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase
) {

//    private var currentFavoriteIcon = R.drawable.ic_favorite_unchecked

    @set:VisibleForTesting
    var currentFavoriteIcon = R.drawable.ic_favorite_unchecked

    private val action = MutableLiveData<Action>()

    val state: LiveData<UiState> = action.switchMap {
        liveData(coroutineContext) {
            when (it) {
//                Action.Default -> emit(UiState.Icon(R.drawable.ic_favorite_unchecked))
                is Action.CheckFavorite -> {
                    checkFavoriteUseCase.invoke(
                        CheckFavoriteUseCase.Params(it.characterId)
                    ).watchStatus(
                        loading = {},
                        success = { isFavorite ->
//                            var icon = R.drawable.ic_favorite_unchecked

                            if (isFavorite) {
//                                icon = R.drawable.ic_favorite_checked
                                currentFavoriteIcon = R.drawable.ic_favorite_checked
                            }

//                            emit(UiState.Icon(currentFavoriteIcon))

                            emitFavoriteIcon()
                        },
                        error = {}
                    )
                }
                is Action.AddFavorite -> {
                    it.detailViewArg.run {
                        addFavoriteUseCase.invoke(
                            AddFavoriteUseCase.Params(
                                characterId,
                                name,
                                imageUrl
                            )
                        ).watchStatus(
                            loading = {
                                emit(UiState.Loading)
                            },
                            success = {
                                currentFavoriteIcon = R.drawable.ic_favorite_checked

//                                emit(UiState.Icon(currentFavoriteIcon))

                                emitFavoriteIcon()
                            },
                            error = {
                                emit(UiState.Error(R.string.error_add_favorite))
                            }
                        )
                    }
                }
                is Action.RemoveFavorite -> {
                    it.detailViewArg.run {
                        removeFavoriteUseCase.invoke(
                            RemoveFavoriteUseCase.Params(
                                characterId,
                                name,
                                imageUrl
                            )
                        ).watchStatus(
                            loading = {
                                emit(UiState.Loading)
                            },
                            success = {
                                currentFavoriteIcon = R.drawable.ic_favorite_unchecked

//                                emit(UiState.Icon(currentFavoriteIcon))

                                emitFavoriteIcon()
                            },
                            error = {
                                emit(UiState.Error(R.string.error_remove_favorite))
                            }
                        )
                    }
                }
            }
        }
    }

//    fun setDefault() {
//        action.value = Action.Default
//    }

    private suspend fun LiveDataScope<UiState>.emitFavoriteIcon() {
        emit(UiState.Icon(currentFavoriteIcon))
    }

    fun checkFavorite(characterId: Int) {
        action.value = Action.CheckFavorite(characterId)
    }

    fun update(detailViewArg: DetailViewArg) {
//        action.value = Action.AddFavorite(detailViewArg)

        action.value = if (currentFavoriteIcon == R.drawable.ic_favorite_unchecked) {
            Action.AddFavorite(detailViewArg)
        } else Action.RemoveFavorite(detailViewArg)
    }

    sealed class UiState {
        object Loading : UiState()
        data class Icon (@DrawableRes val icon: Int) : UiState()
        data class Error (@StringRes val messageResId: Int) : UiState()
    }

    sealed class Action {
//        object Default : Action()

        data class CheckFavorite(val characterId: Int) : Action()
//        data class Update(val detailViewArg: DetailViewArg) : Action()
        data class AddFavorite(val detailViewArg: DetailViewArg) : Action()
        data class RemoveFavorite(val detailViewArg: DetailViewArg) : Action()
    }
}