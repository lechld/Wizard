package at.aau.edu.wizards.ui.lobby.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import at.aau.edu.wizards.databinding.*
import at.aau.edu.wizards.ui.lobby.LobbyItem

private const val ACCEPTED_VIEW_TYPE = 0
private const val HEADER_VIEW_TYPE = 1
private const val REQUESTED_VIEW_TYPE = 2
private const val ADD_CPU_VIEW_TYPE = 3
private const val CPU_PLAYER_VIEW_TYPE = 4

class LobbyAdapter(
    private val onClick: (LobbyItem) -> Unit,
) : ListAdapter<LobbyItem, LobbyItemViewHolder>(DiffItemCallback()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is LobbyItem.Accepted -> ACCEPTED_VIEW_TYPE
            is LobbyItem.Header -> HEADER_VIEW_TYPE
            is LobbyItem.Requested -> REQUESTED_VIEW_TYPE
            is LobbyItem.CpuPlayer -> CPU_PLAYER_VIEW_TYPE
            is LobbyItem.AddCpu -> ADD_CPU_VIEW_TYPE
            else -> throw IllegalStateException("ViewType not supported!")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LobbyItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            ACCEPTED_VIEW_TYPE -> {
                val binding = ItemLobbyAcceptedBinding.inflate(inflater, parent, false)

                LobbyItemViewHolder.Accepted(binding)
            }
            HEADER_VIEW_TYPE -> {
                val binding = ItemLobbyHeaderBinding.inflate(inflater, parent, false)

                LobbyItemViewHolder.Header(binding)
            }
            REQUESTED_VIEW_TYPE -> {
                val binding = ItemLobbyRequestedBinding.inflate(inflater, parent, false)

                LobbyItemViewHolder.Requested(binding, onClick)
            }
            ADD_CPU_VIEW_TYPE -> {
                val binding = ItemLobbyAddCpuBinding.inflate(inflater, parent, false)

                LobbyItemViewHolder.AddCpu(binding, onClick)
            }
            CPU_PLAYER_VIEW_TYPE -> {
                val binding = ItemLobbyCpuPlayerBinding.inflate(inflater, parent, false)

                LobbyItemViewHolder.CpuPlayer(binding)
            }
            else -> throw IllegalStateException("ViewType not supported!")
        }
    }

    override fun onBindViewHolder(holder: LobbyItemViewHolder, position: Int) {
        val item = getItem(position)

        when (holder) {
            is LobbyItemViewHolder.Accepted -> {
                holder.bind(item as LobbyItem.Accepted)
            }
            is LobbyItemViewHolder.Header -> {
                holder.bind(item as LobbyItem.Header)
            }
            is LobbyItemViewHolder.Requested -> {
                holder.bind(item as LobbyItem.Requested)
            }
            is LobbyItemViewHolder.AddCpu -> {
                holder.bind(item as LobbyItem.AddCpu)
            }
            is LobbyItemViewHolder.CpuPlayer -> {
                holder.bind(item as LobbyItem.CpuPlayer)
            }
        }
    }

    private class DiffItemCallback : DiffUtil.ItemCallback<LobbyItem>() {
        override fun areItemsTheSame(oldItem: LobbyItem, newItem: LobbyItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: LobbyItem, newItem: LobbyItem): Boolean {
            return when (oldItem) {
                is LobbyItem.Accepted -> {
                    newItem is LobbyItem.Accepted && oldItem.connection == newItem.connection
                }
                is LobbyItem.Header -> {
                    newItem is LobbyItem.Header && oldItem.text == newItem.text
                }
                is LobbyItem.Requested -> {
                    newItem is LobbyItem.Requested && oldItem.connection == newItem.connection
                }
                is LobbyItem.AddCpu -> {
                    newItem is LobbyItem.AddCpu
                }
                is LobbyItem.CpuPlayer -> {
                    newItem is LobbyItem.CpuPlayer && oldItem.text == newItem.text
                }
            }
        }
    }
}