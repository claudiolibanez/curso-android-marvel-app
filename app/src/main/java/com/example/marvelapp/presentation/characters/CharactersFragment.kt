package com.example.marvelapp.presentation.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.marvelapp.R
import com.example.marvelapp.databinding.FragmentCharactersBinding
import com.example.marvelapp.framework.imageloader.ImageLoader
import com.example.marvelapp.presentation.characters.adapters.CharactersAdapter
import com.example.marvelapp.presentation.characters.adapters.CharactersLoadMoreStateAdapter
import com.example.marvelapp.presentation.characters.adapters.CharactersRefreshStateAdapter
import com.example.marvelapp.presentation.detail.DetailViewArg
import com.example.marvelapp.presentation.sort.SortFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CharactersFragment : Fragment(),
    MenuProvider,
    SearchView.OnQueryTextListener,
    MenuItem.OnActionExpandListener {

    private var _binding: FragmentCharactersBinding? = null
    private val binding: FragmentCharactersBinding get() = _binding!!

    private val viewModel: CharactersViewModel by viewModels()

    private lateinit var searchView: SearchView

    @Inject
    lateinit var imageLoader: ImageLoader

//    private lateinit var charactersAdapter: CharactersAdapter

    private val headerAdapter: CharactersRefreshStateAdapter by lazy {
        CharactersRefreshStateAdapter(
            charactersAdapter::retry
        )
    }

    private val charactersAdapter: CharactersAdapter by lazy {
        CharactersAdapter(
            imageLoader
        ) { character, view ->
            val extras = FragmentNavigatorExtras(
                view to character.name
            )

            val directions = CharactersFragmentDirections
                .actionCharactersFragmentToDetailFragment(
                    character.name,
                    DetailViewArg(
                        characterId = character.id,
                        name = character.name,
                        imageUrl = character.imageUrl
                    )
                )

            findNavController().navigate(directions, extras)
        }
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        setHasOptionsMenu(true)
//    }

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

        observeSortingDate()

        val menuHost = requireActivity()

        menuHost.addMenuProvider(
            this,
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )

        viewModel.state.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is CharactersViewModel.UiState.SearchResult -> {
                    charactersAdapter.submitData(viewLifecycleOwner.lifecycle, uiState.data)
                }
            }
        }
        viewModel.searchCharacters()

//        lifecycleScope.launch {
//            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.charactersPagingData("").collect { pagingData ->
//                    charactersAdapter.submitData(pagingData)
//                }
//            }
//        }

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
//        charactersAdapter = CharactersAdapter(
//            imageLoader
//        ) { character, view ->
//            val extras = FragmentNavigatorExtras(
//                view to character.name
//            )
//
//            val directions = CharactersFragmentDirections
//                .actionCharactersFragmentToDetailFragment(
//                    character.name,
//                    DetailViewArg(
//                        characterId = character.id,
//                        name = character.name,
//                        imageUrl = character.imageUrl
//                    )
//                )
//
//            findNavController().navigate(directions, extras)
//        }

        postponeEnterTransition()

        with(binding.recyclerCharacters) {
//            scrollToPosition(0)

            setHasFixedSize(true)

//            adapter = charactersAdapter.withLoadStateFooter(
//                footer = CharactersLoadMoreStateAdapter (
//                    charactersAdapter::retry
//                )
//            )

            adapter = charactersAdapter.withLoadStateHeaderAndFooter(
                header = headerAdapter,
                footer = CharactersLoadMoreStateAdapter (
                    charactersAdapter::retry
                )
            )

            viewTreeObserver.addOnDrawListener {
                startPostponedEnterTransition()
                true
            }
        }
    }

//    private fun observeInitialLoadingState() {
//        lifecycleScope.launch {
//            charactersAdapter.loadStateFlow.collectLatest { loadState ->
//                binding.flipperCharacters.displayedChild = when (loadState.refresh) {
//                    is LoadState.Loading -> {
//                        setShimmerVisibility(true)
//
//                        FLIPPER_CHILD_LOADING
//                    }
//                    is LoadState.NotLoading -> {
//                        setShimmerVisibility(false)
//
//                        FLIPPER_CHILD_CHARACTERS
//                    }
//                    is LoadState.Error -> {
//                        setShimmerVisibility(false)
//
//                        binding.includeErrorView.buttonRetry.setOnClickListener {
//                            charactersAdapter.retry()
//                        }
//
//                        FLIPPER_CHILD_ERROR
//                    }
//                }
//
//            }
//        }
//    }

    private fun observeInitialLoadingState() {
        lifecycleScope.launch {
            charactersAdapter.loadStateFlow.collectLatest { loadState ->
                headerAdapter.loadState = loadState.mediator
                    ?.refresh
                    ?.takeIf {
                        it is LoadState.Error && charactersAdapter.itemCount > 0
                    } ?: loadState.prepend

                binding.flipperCharacters.displayedChild = when {
                    loadState.mediator?.refresh is LoadState.Loading -> {
                        setShimmerVisibility(true)

                        FLIPPER_CHILD_LOADING
                    }

                    loadState.mediator?.refresh is LoadState.Error
                        && charactersAdapter.itemCount == 0 -> {

                        setShimmerVisibility(false)

                        binding.includeErrorView.buttonRetry.setOnClickListener {
                            charactersAdapter.retry()
                        }

                        FLIPPER_CHILD_ERROR
                    }

                    loadState.source.refresh is LoadState.NotLoading
                        || loadState.mediator?.refresh is LoadState.NotLoading -> {
                            setShimmerVisibility(false)

                            FLIPPER_CHILD_CHARACTERS
                        }
                    else -> {
                        setShimmerVisibility(false)

                        FLIPPER_CHILD_CHARACTERS
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

    private fun observeSortingDate() {
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.charactersFragment)
        val observer = LifecycleEventObserver { _, event ->
            val isSortingApplied = navBackStackEntry.savedStateHandle.contains(
                SortFragment.SORTING_APPLIED_BASK_STACK_KEY
            )

            if (event == Lifecycle.Event.ON_RESUME && isSortingApplied) {
                viewModel.applySort()

                navBackStackEntry.savedStateHandle.remove<Boolean>(
                    SortFragment.SORTING_APPLIED_BASK_STACK_KEY
                )
            }
        }

        navBackStackEntry.lifecycle.addObserver(observer)

        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }
        })
    }

//    @Deprecated("Deprecated in Java")
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.characters_menu_items, menu)
//
//        val searchItem = menu.findItem(R.id.menu_search)
//        searchView = searchItem.actionView as SearchView
//
//        searchItem.setOnActionExpandListener(this)
//
//        if (viewModel.currentSearchQuery.isNotEmpty()) {
//            searchItem.expandActionView()
//            searchView.setQuery(viewModel.currentSearchQuery, false)
//        }
//
//        searchView.run {
//            isSubmitButtonEnabled = true
//
//            setOnQueryTextListener(this@CharactersFragment)
//        }
//    }

//    @Deprecated("Deprecated in Java")
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.menu_sort -> {
//                findNavController().navigate(R.id.action_charactersFragment_to_sortFragment)
//                true
//            }
//
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.characters_menu_items, menu)

        val searchItem = menu.findItem(R.id.menu_search)
        searchView = searchItem.actionView as SearchView

        searchItem.setOnActionExpandListener(this)

        if (viewModel.currentSearchQuery.isNotEmpty()) {
            searchItem.expandActionView()
            searchView.setQuery(viewModel.currentSearchQuery, false)
        }

        searchView.run {
            isSubmitButtonEnabled = true

            setOnQueryTextListener(this@CharactersFragment)
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.menu_sort -> {
                findNavController().navigate(R.id.action_charactersFragment_to_sortFragment)
                true
            }
            else -> false
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return query?.let {

            viewModel.currentSearchQuery = it
            viewModel.searchCharacters()

            true
        } ?: false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    override fun onMenuItemActionExpand(item: MenuItem): Boolean {
        return true
    }

    override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
        viewModel.closeSort()
        viewModel.searchCharacters()

        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        searchView.setOnQueryTextListener(null)
        _binding = null
    }

    companion object {
        private const val FLIPPER_CHILD_LOADING = 0
        private const val FLIPPER_CHILD_CHARACTERS = 1
        private const val FLIPPER_CHILD_ERROR = 2
    }

}