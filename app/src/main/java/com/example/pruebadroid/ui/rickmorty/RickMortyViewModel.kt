package com.example.pruebadroid.ui.rickmorty

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pruebadroid.data.model.Character
import com.example.pruebadroid.data.model.CharacterListResult
import com.example.pruebadroid.data.network.RetrofitClient
import com.example.pruebadroid.utils.SharedPreferencesUtils
import kotlinx.coroutines.launch

class RickMortyViewModel(application: Application) : AndroidViewModel(application) {

    private val _characterList = MutableLiveData<ArrayList<Character>>()
    val characterListLiveData: LiveData<ArrayList<Character>> = _characterList

    private val _pageInfo = MutableLiveData<Pair<String, String>>()
    val pageInfoLiveData: LiveData<Pair<String, String>> get() = _pageInfo

    private val _characterFavoriteList = MutableLiveData<ArrayList<Character>>()
    val characterFavoriteListLiveData: LiveData<ArrayList<Character>> = _characterFavoriteList
    lateinit var characterFavoriteIDList: Set<Int>

    private lateinit var characterListResult: CharacterListResult

    init {
        loadCharacterFavoriteList()
    }

    private fun loadCharacterFavoriteList() {
        val characterFavoriteList: ArrayList<Character> =
            SharedPreferencesUtils.loadRickMortyList(getApplication())
        characterFavoriteIDList = characterFavoriteList.map { it.id }.toSet()
        _characterFavoriteList.postValue(characterFavoriteList)
    }

    fun loadCharacterList(page: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getAllCharacters(page)
                if (response.isSuccessful && response.body() != null) {
                    characterListResult = response.body()!!
                    _characterList.postValue(characterListResult.results)
                    _pageInfo.postValue(Pair(page.toString(),
                        characterListResult.info.pages))
                } else {
                    resetCharacterList()
                }
            } catch (e: Exception) {
                resetCharacterList()
            }
        }
    }

    fun loadPreviousPage(){
        characterListResult.let {
            val page: Int = characterListResult.info?.prev?.substringAfterLast("=")?.toIntOrNull() ?: 1
            page.let {this.loadCharacterList(page)}
        }
    }

    fun loadNextPage(){
        characterListResult.let {
            val page: Int = characterListResult.info?.next?.substringAfterLast("=")?.toIntOrNull() ?: characterListResult.info.pages.toInt()
            page.let {this.loadCharacterList(page)}
        }
    }

    fun markAsFavorite(id: Int) {
        val characterFavoriteListNew = _characterFavoriteList.value
        val characterFavorite = _characterList.value?.find { it.id == id }
        if (characterFavoriteListNew != null && characterFavorite != null){
            characterFavoriteListNew.add(characterFavorite)
            SharedPreferencesUtils.saveRickMortyList(getApplication(), characterFavoriteListNew)
            loadCharacterFavoriteList()
            _characterList.postValue(_characterList.value)
        }
    }

    fun unMarkAsFavorite(id: Int) {
        val characterFavoriteListNew = _characterFavoriteList.value
        val characterFavorite = characterFavoriteListNew?.find { it.id == id }
        if (characterFavoriteListNew != null && characterFavorite != null){
            characterFavoriteListNew.remove(characterFavorite)
            SharedPreferencesUtils.saveRickMortyList(getApplication(), characterFavoriteListNew)
            loadCharacterFavoriteList()
            _characterList.postValue(_characterList.value)
        }
    }

    fun resetCharacterList(){
        _characterList.postValue(ArrayList())
    }
}