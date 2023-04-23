package at.aau.edu.wizards.ui.discover.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import at.aau.edu.wizards.databinding.ItemDiscoverApprovedBinding
import at.aau.edu.wizards.databinding.ItemDiscoverHeaderBinding
import at.aau.edu.wizards.databinding.ItemDiscoverPendingBinding
import at.aau.edu.wizards.databinding.ItemDiscoverRequestedBinding
import at.aau.edu.wizards.ui.discover.MainscreenItem

private const val HEADER_VIEW_TYPE = 0
private const val PENDING_VIEW_TYPE = 1
private const val REQUESTED_VIEW_TYPE = 2
private const val APPROVED_VIEW_TYPE = 3

class MainscreenAdapter(
    private val onClick: (MainscreenItem.Pending) -> Unit,
) : ListAdapter<MainscreenItem, MainscreenItemViewHolder>(DiffUtlCallback()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is MainscreenItem.Header -> HEADER_VIEW_TYPE
            is MainscreenItem.Pending -> PENDING_VIEW_TYPE
            is MainscreenItem.Requested -> REQUESTED_VIEW_TYPE
            is MainscreenItem.Approved -> APPROVED_VIEW_TYPE
            else -> throw IllegalStateException("ViewType not supported!")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainscreenItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            HEADER_VIEW_TYPE -> {
                val binding = ItemDiscoverHeaderBinding.inflate(inflater, parent, false)

                MainscreenItemViewHolder.Header(binding)
            }
            PENDING_VIEW_TYPE -> {
                val binding = ItemDiscoverPendingBinding.inflate(inflater, parent, false)

                MainscreenItemViewHolder.Pending(binding, onClick)
            }
            REQUESTED_VIEW_TYPE -> {
                val binding = ItemDiscoverRequestedBinding.inflate(inflater, parent, false)

                MainscreenItemViewHolder.Requested(binding)
            }
            APPROVED_VIEW_TYPE -> {
                val binding = ItemDiscoverApprovedBinding.inflate(inflater, parent, false)

                MainscreenItemViewHolder.Approved(binding)

            }
            else -> throw IllegalStateException("ViewType not supported!")
        }
    }

    override fun onBindViewHolder(holder: MainscreenItemViewHolder, position: Int) {
        val item = getItem(position)

        when (holder) {
            is MainscreenItemViewHolder.Header -> {
                holder.bind(item as MainscreenItem.Header)
            }
            is MainscreenItemViewHolder.Pending -> {
                holder.bind(item as MainscreenItem.Pending)
            }
            is MainscreenItemViewHolder.Requested -> {
                holder.bind(item as MainscreenItem.Requested)
            }
            is MainscreenItemViewHolder.Approved -> {
                holder.bind(item as MainscreenItem.Approved)
            }
        }
    }

    private class DiffUtlCallback : DiffUtil.ItemCallback<MainscreenItem>() {

        override fun areItemsTheSame(oldItem: MainscreenItem, newItem: MainscreenItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MainscreenItem, newItem: MainscreenItem): Boolean {
            return when (oldItem) {
                is MainscreenItem.Header -> {
                    newItem is MainscreenItem.Header && oldItem.text == newItem.text
                }
                is MainscreenItem.Pending -> {
                    newItem is MainscreenItem.Pending && oldItem.connection == newItem.connection
                }
                is MainscreenItem.Requested -> {
                    newItem is MainscreenItem.Requested && oldItem.connection == newItem.connection
                }
                is MainscreenItem.Approved -> {
                    newItem is MainscreenItem.Approved && oldItem.connection == newItem.connection
                }
            }
        }
    }
}