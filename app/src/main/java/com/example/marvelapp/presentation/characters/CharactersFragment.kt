package com.example.marvelapp.presentation.characters

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import com.example.marvelapp.databinding.FragmentCharactersBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharactersFragment : Fragment() {

    private var _binding: FragmentCharactersBinding? = null
    private val binding: FragmentCharactersBinding get() = _binding!!

    private val viewModel: CharactersViewModel by viewModels()

    private lateinit var charactersAdapter: CharactersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentCharactersBinding.inflate(
        inflater,
        container,
        false
    ).apply {
        _binding = this
    }.root

    @Suppress("MaxLineLength")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCharactersAdapter()

        observeInitialLoadingState()

        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.charactersPagingData("").collect { pagingData ->
                    charactersAdapter.submitData(pagingData)
                }
            }
        }

//        charactersAdapter.submitList(
//            fakeList()
//        )
    }

//    @Suppress("UnusedPrivateMember")
//    private fun fakeList(): List<Character> {
//        return listOf(
//            Character(
//                "Spider Man",
//                "https://lumiere-a.akamaihd.net/v1/images/marvelspidermanseries-emeagrid-m_4456fb99.jpeg"
//            ),
//            Character(
//                "Spider Man",
//                "https://lumiere-a.akamaihd.net/v1/images/marvelspidermanseries-emeagrid-m_4456fb99.jpeg"
//            ),
//            Character(
//                "Spider Man",
//                "https://lumiere-a.akamaihd.net/v1/images/marvelspidermanseries-emeagrid-m_4456fb99.jpeg"
//            ),
//            Character(
//                "Spider Man",
//                "https://lumiere-a.akamaihd.net/v1/images/marvelspidermanseries-emeagrid-m_4456fb99.jpeg"
//            )
//        )
//    }

    private fun initCharactersAdapter() {
        charactersAdapter = CharactersAdapter()

        with(binding.recyclerCharacters) {
            scrollToPosition(0)

            setHasFixedSize(true)

            adapter = charactersAdapter.withLoadStateFooter(
                footer = CharactersLoadStateAdapter (
                    charactersAdapter::retry
                )
            )
        }
    }

    private fun observeInitialLoadingState() {
        lifecycleScope.launch {
            charactersAdapter.loadStateFlow.collectLatest { loadState ->
                binding.flipperCharacters.displayedChild = when (loadState.refresh) {
                    is LoadState.Loading -> {
                        setShimmerVisibility(true)

                        FLIPPER_CHILD_LOADING
                    }
                    is LoadState.NotLoading -> {
                        setShimmerVisibility(false)

                        FLIPPER_CHILD_CHARACTERS
                    }
                    is LoadState.Error -> {
                        setShimmerVisibility(false)

                        binding.includeViewCharactersErrorState.buttonRetry.setOnClickListener {
                            charactersAdapter.refresh()
                        }

                        FLIPPER_CHILD_ERROR
                    }
                }

            }
        }
    }

    private fun setShimmerVisibility(visibility: Boolean) {
        binding.includeViewCharactersLoadingState.shimmerCharacters.run {
            isVisible = visibility

            if (visibility) {
                startShimmer()
            } else stopShimmer()
        }
    }

    companion object {
        private const val FLIPPER_CHILD_LOADING = 0
        private const val FLIPPER_CHILD_CHARACTERS = 1
        private const val FLIPPER_CHILD_ERROR = 2
    }
}