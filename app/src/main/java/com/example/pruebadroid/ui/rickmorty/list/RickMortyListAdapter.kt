package com.example.pruebadroid.ui.rickmorty.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pruebadroid.R
import com.example.pruebadroid.data.model.Character
import com.example.pruebadroid.databinding.ItemRickmortyListBinding

class RickMortyListAdapter(val listener: OnRickMortyListener) :
    RecyclerView.Adapter<RickMortyListAdapter.RickMortyViewHolder>() {

    private var dataList: ArrayList<Character> = ArrayList()
    private lateinit var characterFavoriteListID: Set<Int>

    inner class RickMortyViewHolder(private val binding: ItemRickmortyListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnLongClickListener { view ->
                val popupMenu = PopupMenu(binding.root.context, view)
                popupMenu.inflate(R.menu.context_menu_rickmorty)
                val favoriteItemMenu = popupMenu.menu.findItem(R.id.action_favorite)
                val noFavoriteItemMenu = popupMenu.menu.findItem(R.id.action_no_favorite)
                if (dataList[adapterPosition].id in characterFavoriteListID) {
                    favoriteItemMenu.isVisible = false
                    noFavoriteItemMenu.isVisible = true
                } else {
                    favoriteItemMenu.isVisible = true
                    noFavoriteItemMenu.isVisible = false
                }
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.action_favorite -> {
                            listener.onMarkAsFavorite(dataList[adapterPosition].id)
                            true
                        }

                        R.id.action_no_favorite -> {
                            listener.onUnmarkAsFavorite(dataList[adapterPosition].id)
                            true
                        }

                        else -> false
                    }
                }
                popupMenu.show()
                true
            }
        }

        fun bind(character: Character) {
            Glide.with(binding.root.context)
                .load(character.image)
                .placeholder(R.drawable.ic_avatar)
                .error(R.drawable.ic_avatar)
                .into(binding.imageRickMorty)
            binding.textNameRickMorty.text = character.name
            binding.textSpeciesRickMorty.text = character.species
            binding.textStatusRickMorty.text = character.status
            when (character.status) {
                Character.STATUS_ALIVE -> binding.textStatusRickMorty.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.text_color_alive
                    )
                )

                Character.STATUS_DEAD -> binding.textStatusRickMorty.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.text_color_dead
                    )
                )

                Character.STATUS_UNKNOWN -> binding.textStatusRickMorty.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.text_color_unknown
                    )
                )
            }
            binding.imageFavoriteRickMorty.visibility =
                if (character.id in characterFavoriteListID) View.VISIBLE else View.GONE
        }
    }

    fun updateDataList(dataList: ArrayList<Character>, characterFavoriteListID: Set<Int>) {
        this.dataList = dataList
        this.characterFavoriteListID = characterFavoriteListID
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RickMortyViewHolder {
        val binding =
            ItemRickmortyListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RickMortyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: RickMortyViewHolder, position: Int) {
        val character = dataList[position]
        holder.bind(character)
    }

    interface OnRickMortyListener {
        fun onMarkAsFavorite(id: Int)
        fun onUnmarkAsFavorite(id: Int)
    }
}