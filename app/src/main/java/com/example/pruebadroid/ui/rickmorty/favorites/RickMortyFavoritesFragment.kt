package com.example.pruebadroid.ui.rickmorty.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pruebadroid.databinding.FragmentRickMortyFavoritesBinding
import com.example.pruebadroid.ui.rickmorty.RickMortyViewModel
import com.example.pruebadroid.ui.rickmorty.list.RickMortyListAdapter

class RickMortyFavoritesFragment : Fragment(), RickMortyListAdapter.OnRickMortyListener {

    private var _binding: FragmentRickMortyFavoritesBinding? = null
    private val binding get() = _binding!!

    private val rickMortyViewModel: RickMortyViewModel by activityViewModels()
    private lateinit var rickMortyAdapter: RickMortyListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRickMortyFavoritesBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        initObservables()
    }

    private fun initAdapter(){
        rickMortyAdapter = RickMortyListAdapter(this)
        binding.recyclerRickMortyFavoritesList.layoutManager = LinearLayoutManager(context)
        binding.recyclerRickMortyFavoritesList.adapter = rickMortyAdapter
    }

    private fun initObservables(){
        rickMortyViewModel.characterFavoriteListLiveData.observe(viewLifecycleOwner) {
            rickMortyAdapter.updateDataList(it, rickMortyViewModel.characterFavoriteIDList)
            binding.recyclerRickMortyFavoritesList.scrollToPosition(0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMarkAsFavorite(id: Int) {
        // do nothing
    }

    override fun onUnmarkAsFavorite(id: Int) {
        rickMortyViewModel.unMarkAsFavorite(id)
    }
}