package com.example.pruebadroid.ui.rickmorty.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pruebadroid.R
import com.example.pruebadroid.databinding.FragmentRickmortyListBinding
import com.example.pruebadroid.ui.rickmorty.RickMortyViewModel

class RickMortyListFragment : Fragment(), RickMortyListAdapter.OnRickMortyListener {

    private var _binding: FragmentRickmortyListBinding? = null
    private val binding get() = _binding!!

    private val rickMortyViewModel: RickMortyViewModel by activityViewModels()
    private lateinit var rickMortyAdapter: RickMortyListAdapter
    private var isFromMarkAsFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRickmortyListBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMenu()
        initListeners()
        initAdapter()
        initObservables()

        rickMortyViewModel.loadCharacterList(1)
    }

    private fun initMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_rickmorty_favorite, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_menu_favorite -> {
                        findNavController().navigate(R.id.nav_rickmorty_favorites)
                        true
                    }

                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun initListeners() {
        binding.btnPreviousRickMortyList.setOnClickListener {
            rickMortyViewModel.loadPreviousPage()
        }
        binding.btnNextRickMortyList.setOnClickListener {
            rickMortyViewModel.loadNextPage()
        }
        binding.btnNoDataRickMortyList.setOnClickListener({
            findNavController().navigate(R.id.nav_rickmorty_favorites)
        })
    }

    private fun initAdapter() {
        rickMortyAdapter = RickMortyListAdapter(this)
        binding.recyclerRickMortyList.layoutManager = LinearLayoutManager(context)
        binding.recyclerRickMortyList.adapter = rickMortyAdapter
    }

    private fun initObservables() {
        rickMortyViewModel.characterListLiveData.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.constraintNoDataRickMortyList.visibility = View.VISIBLE
            } else {
                rickMortyAdapter.updateDataList(it, rickMortyViewModel.characterFavoriteIDList)
                if (isFromMarkAsFavorite) isFromMarkAsFavorite = false
                else binding.recyclerRickMortyList.scrollToPosition(0)
                binding.constraintNoDataRickMortyList.visibility = View.GONE
            }
        }

        rickMortyViewModel.pageInfoLiveData.observe(viewLifecycleOwner) {
            it?.let {
                binding.textInfoPageRickMortyList.text =
                    getString(R.string.rickmorty_list_info_page, it.first, it.second)
                when (it.first) {
                    "1" -> setButtonState(false, binding.btnPreviousRickMortyList)
                    it.second -> setButtonState(false, binding.btnNextRickMortyList)
                    else -> setButtonState(
                        true,
                        binding.btnPreviousRickMortyList,
                        binding.btnNextRickMortyList
                    )
                }
            }
        }
    }

    private fun setButtonState(state: Boolean, vararg buttons: Button) {
        buttons.forEach { button ->
            button.isEnabled = state
        }
    }

    override fun onMarkAsFavorite(id: Int) {
        isFromMarkAsFavorite = true
        rickMortyViewModel.markAsFavorite(id)
    }

    override fun onUnmarkAsFavorite(id: Int) {
        isFromMarkAsFavorite = true
        rickMortyViewModel.unMarkAsFavorite(id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rickMortyViewModel.resetViewModel()
        _binding = null
    }
}