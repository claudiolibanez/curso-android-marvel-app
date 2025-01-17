package com.example.marvelapp.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.example.marvelapp.databinding.FragmentDetailBinding
import com.example.marvelapp.framework.imageloader.ImageLoader
import com.example.marvelapp.presentation.extensions.showShortToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding: FragmentDetailBinding get() = _binding!!

    private val viewModel: DetailViewModel by viewModels()

    @Inject
    lateinit var imageLoader: ImageLoader

    private val args by navArgs<DetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentDetailBinding.inflate(
        inflater,
        container,
        false
    ).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val detailViewArg = args.detailViewArg

        binding.imageCharacter.run {
            transitionName = detailViewArg.name

            imageLoader.load(this, detailViewArg.imageUrl)

//            Glide.with(context)
//                .load(detailViewArg.imageUrl)
//                .fallback(R.drawable.ic_img_loading_error)
//                .into(this)
        }

        setSharedelementTransitionOnEnter()

//        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
////            val logResult = when (uiState) {
////                DetailViewModel.UiState.Loading -> "Loading comics..."
////                is DetailViewModel.UiState.Success -> uiState.comics.toString()
////                is DetailViewModel.UiState.Error -> "Error when loading comics"
////            }
////
////            Log.d(DetailViewModel::class.simpleName, logResult)
//
//            binding.flipperDetail.displayedChild = when (uiState) {
//                DetailViewModel.UiState.Loading -> FLIPPER_CHILD_POSITION_LOADING
//                is DetailViewModel.UiState.Success -> {
//                    binding.recyclerParentDetail.run {
//                        setHasFixedSize(true)
//                        adapter = DetailParentAdapter(uiState.detailParentList, imageLoader)
//                    }
//
//                    FLIPPER_CHILD_POSITION_DETAIL
//                }
//                is DetailViewModel.UiState.Error -> {
//                    binding.includeErrorView.buttonRetry.setOnClickListener {
//                        viewModel.getCharacterCategories(detailViewArg.characterId)
//                    }
//                    FLIPPER_CHILD_POSITION_ERROR
//                }
//                is DetailViewModel.UiState.Empty -> FLIPPER_CHILD_POSITION_EMPTY
//            }
//        }

        loadCategoriesAndObserveUiState(detailViewArg)
        setAnd0bserveFavoriteUiState(detailViewArg)

//        viewModel.getCharacterCategories(detailViewArg.characterId)

//        binding.imageFavoriteIcon.setOnClickListener {
//            viewModel.updateFavorite(detailViewArg)
//        }
    }

    private fun loadCategoriesAndObserveUiState(detailViewArg: DetailViewArg) {
//        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
//            binding.flipperDetail.displayedChild = when (uiState) {
//                DetailViewModel.UiState.Loading -> FLIPPER_CHILD_POSITION_LOADING
//                is DetailViewModel.UiState.Success -> {
//                    binding.recyclerParentDetail.run {
//                        setHasFixedSize(true)
//                        adapter = DetailParentAdapter(uiState.detailParentList, imageLoader)
//                    }
//
//                    FLIPPER_CHILD_POSITION_DETAIL
//                }
//                is DetailViewModel.UiState.Error -> {
//                    binding.includeErrorView.buttonRetry.setOnClickListener {
//                        viewModel.getCharacterCategories(detailViewArg.characterId)
//                    }
//                    FLIPPER_CHILD_POSITION_ERROR
//                }
//                is DetailViewModel.UiState.Empty -> FLIPPER_CHILD_POSITION_EMPTY
//            }
//        }

        viewModel.categories.load(detailViewArg.characterId)
        viewModel.categories.state.observe(viewLifecycleOwner) { uiState ->
            binding.flipperDetail.displayedChild = when (uiState) {
                UiActionStateLiveData.UiState.Loading -> FLIPPER_CHILD_POSITION_LOADING
                is UiActionStateLiveData.UiState.Success -> {
                    binding.recyclerParentDetail.run {
                        setHasFixedSize(true)
                        adapter = DetailParentAdapter(uiState.detailParentList, imageLoader)
                    }

                    FLIPPER_CHILD_POSITION_DETAIL
                }
                is UiActionStateLiveData.UiState.Error -> {
                    binding.includeErrorView.buttonRetry.setOnClickListener {
//                        viewModel.getCharacterCategories(detailViewArg.characterId)
                        viewModel.categories.load(detailViewArg.characterId)
                    }
                    FLIPPER_CHILD_POSITION_ERROR
                }
                is UiActionStateLiveData.UiState.Empty -> FLIPPER_CHILD_POSITION_EMPTY
            }
        }
    }

    private fun setAnd0bserveFavoriteUiState(detailViewArg: DetailViewArg) {
//        viewModel.favoriteUiState.observe(viewLifecycleOwner) { favoriteUiState ->
//            binding.flipperFavorite.displayedChild = when (favoriteUiState) {
//                DetailViewModel.FavoriteUiState.Loading -> FLIPPER_FAVORITE_CHILD_POSITION_LOADING
//                is DetailViewModel.FavoriteUiState.FavoriteIcon -> {
//                    binding.imageFavoriteIcon.setImageResource(favoriteUiState.icon)
//                    FLIPPER_FAVORITE_CHILD_POSITION_SUCCESS
//                }
//            }
//        }

//        binding.imageFavoriteIcon.setOnClickListener {
//            viewModel.favorite.update(detailViewArg)
//        }
//
//        viewModel.favorite.state.observe(viewLifecycleOwner) { uiState ->
//            binding.flipperFavorite.displayedChild = when (uiState) {
//                FavoriteUiActionStateLiveData.UiState.Loading -> FLIPPER_FAVORITE_CHILD_POSITION_LOADING
//                is FavoriteUiActionStateLiveData.UiState.Icon -> {
//                    binding.imageFavoriteIcon.setImageResource(uiState.icon)
//                    FLIPPER_FAVORITE_CHILD_POSITION_IMAGE
//                }
//                is FavoriteUiActionStateLiveData.UiState.Error -> {
//                    showShortToast(uiState.messageResId)
//                    FLIPPER_FAVORITE_CHILD_POSITION_IMAGE
//                }
//            }
//        }

        viewModel.favorite.run {
            checkFavorite(detailViewArg.characterId)

            binding.imageFavoriteIcon.setOnClickListener {
                update(detailViewArg)
            }

            state.observe(viewLifecycleOwner) { uiState ->
                binding.flipperFavorite.displayedChild = when (uiState) {
                    FavoriteUiActionStateLiveData.UiState.Loading -> FLIPPER_FAVORITE_CHILD_POSITION_LOADING
                    is FavoriteUiActionStateLiveData.UiState.Icon -> {
                        binding.imageFavoriteIcon.setImageResource(uiState.icon)
                        FLIPPER_FAVORITE_CHILD_POSITION_IMAGE
                    }
                    is FavoriteUiActionStateLiveData.UiState.Error -> {
                        showShortToast(uiState.messageResId)
                        FLIPPER_FAVORITE_CHILD_POSITION_IMAGE
                    }
                }
            }
        }
    }

    // Define a animação de transição como "move"
    private fun setSharedelementTransitionOnEnter() {
        TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move).apply {
                sharedElementEnterTransition = this
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val FLIPPER_CHILD_POSITION_LOADING = 0
        private const val FLIPPER_CHILD_POSITION_DETAIL = 1
        private const val FLIPPER_CHILD_POSITION_ERROR = 2
        private const val FLIPPER_CHILD_POSITION_EMPTY = 3
        private const val FLIPPER_FAVORITE_CHILD_POSITION_IMAGE = 0
        private const val FLIPPER_FAVORITE_CHILD_POSITION_LOADING = 1
    }
}