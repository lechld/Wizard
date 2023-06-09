package at.aau.edu.wizards.ui.discover.recycler

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import at.aau.edu.wizards.databinding.*
import at.aau.edu.wizards.ui.discover.DiscoverItem

sealed class DiscoverItemViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    class Approved(
        private val binding: ItemDiscoverApprovedBinding,
    ) : DiscoverItemViewHolder(binding) {

        @SuppressLint("SetTextI18n")
        fun bind(item: DiscoverItem.Approved) {
            binding.approvedText.text =
                item.connection.endpointId + " - " + item.connection.endpointName
        }
    }

    class Header(
        private val binding: ItemDiscoverHeaderBinding,
    ) : DiscoverItemViewHolder(binding) {

        fun bind(item: DiscoverItem.Header) {
            binding.root.text = item.text
        }
    }

    class Pending(
        private val binding: ItemDiscoverPendingBinding,
        private val onClick: (DiscoverItem.Pending) -> Unit,
    ) : DiscoverItemViewHolder(binding) {

        @SuppressLint("SetTextI18n")
        fun bind(item: DiscoverItem.Pending) {
            binding.pendingText.text =
                item.connection.endpointId + " - " + item.connection.endpointName
            binding.root.setOnClickListener {
                onClick(item)
            }
        }
    }

    class Requested(
        private val binding: ItemDiscoverRequestedBinding,
    ) : DiscoverItemViewHolder(binding) {

        @SuppressLint("SetTextI18n")
        fun bind(item: DiscoverItem.Requested) {
            binding.requestedText.text =
                item.connection.endpointId + " - " + item.connection.endpointName
        }
    }
}