package at.aau.edu.wizards.ui.discover.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import at.aau.edu.wizards.databinding.ItemDiscoverApprovedBinding
import at.aau.edu.wizards.databinding.ItemDiscoverHeaderBinding
import at.aau.edu.wizards.databinding.ItemDiscoverPendingBinding
import at.aau.edu.wizards.databinding.ItemDiscoverRequestedBinding
import at.aau.edu.wizards.ui.discover.DiscoverItem
import java.util.concurrent.Executor

private const val HEADER_VIEW_TYPE = 0
private const val PENDING_VIEW_TYPE = 1
private const val REQUESTED_VIEW_TYPE = 2
private const val APPROVED_VIEW_TYPE = 3

class DiscoverAdapter(
    private val onClick: (DiscoverItem.Pending) -> Unit,
) : ListAdapter<DiscoverItem, DiscoverItemViewHolder>(DiffUtlCallback()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DiscoverItem.Header -> HEADER_VIEW_TYPE
            is DiscoverItem.Pending -> PENDING_VIEW_TYPE
            is DiscoverItem.Requested -> REQUESTED_VIEW_TYPE
            is DiscoverItem.Approved -> APPROVED_VIEW_TYPE
            else -> throw IllegalStateException("ViewType not supported!")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscoverItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            HEADER_VIEW_TYPE -> {
                val binding = ItemDiscoverHeaderBinding.inflate(inflater, parent, false)

                DiscoverItemViewHolder.Header(binding)
            }
            PENDING_VIEW_TYPE -> {
                val binding = ItemDiscoverPendingBinding.inflate(inflater, parent, false)

                DiscoverItemViewHolder.Pending(binding, onClick)
            }
            REQUESTED_VIEW_TYPE -> {
                val binding = ItemDiscoverRequestedBinding.inflate(inflater, parent, false)

                DiscoverItemViewHolder.Requested(binding)
            }
            APPROVED_VIEW_TYPE -> {
                val binding = ItemDiscoverApprovedBinding.inflate(inflater, parent, false)

                DiscoverItemViewHolder.Approved(binding)

            }
            else -> throw IllegalStateException("ViewType not supported!")
        }
    }

    override fun onBindViewHolder(holder: DiscoverItemViewHolder, position: Int) {
        val item = getItem(position)

        when (holder) {
            is DiscoverItemViewHolder.Header -> {
                holder.bind(item as DiscoverItem.Header)
            }
            is DiscoverItemViewHolder.Pending -> {
                holder.bind(item as DiscoverItem.Pending)
            }
            is DiscoverItemViewHolder.Requested -> {
                holder.bind(item as DiscoverItem.Requested)
            }
            is DiscoverItemViewHolder.Approved -> {
                holder.bind(item as DiscoverItem.Approved)
            }
        }
    }

    private class DiffUtlCallback : DiffUtil.ItemCallback<DiscoverItem>() {

        override fun areItemsTheSame(oldItem: DiscoverItem, newItem: DiscoverItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DiscoverItem, newItem: DiscoverItem): Boolean {
            return when (oldItem) {
                is DiscoverItem.Header -> {
                    newItem is DiscoverItem.Header && oldItem.text == newItem.text
                }
                is DiscoverItem.Pending -> {
                    newItem is DiscoverItem.Pending && oldItem.connection == newItem.connection
                }
                is DiscoverItem.Requested -> {
                    newItem is DiscoverItem.Requested && oldItem.connection == newItem.connection
                }
                is DiscoverItem.Approved -> {
                    newItem is DiscoverItem.Approved && oldItem.connection == newItem.connection
                }
            }
        }
    }
}