package at.aau.edu.wizards.ui.lobby.recycler

import android.annotation.SuppressLint
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import at.aau.edu.wizards.R
import at.aau.edu.wizards.databinding.*
import at.aau.edu.wizards.ui.lobby.LobbyItem

sealed class LobbyItemViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    class Accepted(
        private val binding: ItemLobbyAcceptedBinding,
    ) : LobbyItemViewHolder(binding) {

        @SuppressLint("SetTextI18n")
        fun bind(item: LobbyItem.Accepted) {
            val username = item.connection.endpointName.split(":")[0]
            val avatar = item.connection.endpointName.split(":")[1].toInt()

            binding.acceptedText.text = username
            binding.acceptedAvatar.setImageResource(avatar)
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
            val username = item.connection.endpointName.split(":")[0]
            val avatar = item.connection.endpointName.split(":")[1].toInt()

            binding.text.text = username
            binding.avatar.setImageResource(avatar)
            binding.root.setOnClickListener {
                onClick(item)
            }
        }
    }

    class AddCpu(
        private val binding: ItemLobbyAddCpuBinding,
        private val onClick: (LobbyItem) -> Unit,
    ) : LobbyItemViewHolder(binding) {

        fun bind(item: LobbyItem.AddCpu) {
            binding.root.setOnClickListener {
                onClick(item)
            }
        }
    }

    class RemoveCpu(
        private val binding: ItemLobbyRemoveCpuBinding,
        private val onClick: (LobbyItem) -> Unit,
    ) : LobbyItemViewHolder(binding) {

        fun bind(item: LobbyItem.RemoveCpu) {
            binding.root.setOnClickListener {
                onClick(item)
            }
        }
    }

    class CpuPlayer(
        private val binding: ItemLobbyCpuPlayerBinding
    ) : LobbyItemViewHolder(binding) {

        fun bind(item: LobbyItem.CpuPlayer) {
            val username = item.text.split(":")[0]
            val avatar = item.text.split(":")[1].toInt()

            binding.cpuText.text = username
            binding.cpuAvatar.setImageResource(avatar)
        }
    }
}