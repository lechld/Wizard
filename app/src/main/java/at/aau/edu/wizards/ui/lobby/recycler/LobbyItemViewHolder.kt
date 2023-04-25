package at.aau.edu.wizards.ui.lobby.recycler

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import at.aau.edu.wizards.databinding.ItemLobbyAcceptedBinding
import at.aau.edu.wizards.databinding.ItemLobbyHeaderBinding
import at.aau.edu.wizards.databinding.ItemLobbyRequestedBinding
import at.aau.edu.wizards.ui.lobby.LobbyItem

sealed class LobbyItemViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    class Accepted(
        private val binding: ItemLobbyAcceptedBinding,
    ) : LobbyItemViewHolder(binding) {

        @SuppressLint("SetTextI18n")
        fun bind(item: LobbyItem.Accepted) {
            binding.acceptedText.text =
                item.connection.endpointId + " - " + item.connection.endpointName
        }
    }

    class Header(
        private val binding: ItemLobbyHeaderBinding,
    ) : LobbyItemViewHolder(binding) {

        fun bind(item: LobbyItem.Header) {
            binding.root.text = item.text
        }
    }

    class Requested(
        private val binding: ItemLobbyRequestedBinding,
        private val onClick: (LobbyItem.Requested) -> Unit,
    ) : LobbyItemViewHolder(binding) {

        @SuppressLint("SetTextI18n")
        fun bind(item: LobbyItem.Requested) {
            binding.text.text = item.connection.endpointId + " - " + item.connection.endpointName
            binding.root.setOnClickListener {
                onClick(item)
            }
        }
    }
}