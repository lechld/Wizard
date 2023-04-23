package at.aau.edu.wizards.ui.discover.recycler

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import at.aau.edu.wizards.databinding.ItemDiscoverApprovedBinding
import at.aau.edu.wizards.databinding.ItemDiscoverHeaderBinding
import at.aau.edu.wizards.databinding.ItemDiscoverPendingBinding
import at.aau.edu.wizards.databinding.ItemDiscoverRequestedBinding
import at.aau.edu.wizards.ui.discover.MainscreenItem

sealed class MainscreenItemViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    class Approved(
        private val binding: ItemDiscoverApprovedBinding,
    ) : MainscreenItemViewHolder(binding) {

        @SuppressLint("SetTextI18n")
        fun bind(item: MainscreenItem.Approved) {
            binding.approvedText.text =
                item.connection.endpointId + " - " + item.connection.endpointName
        }
    }

    class Header(
        private val binding: ItemDiscoverHeaderBinding,
    ) : MainscreenItemViewHolder(binding) {

        fun bind(item: MainscreenItem.Header) {
            binding.root.text = item.text
        }
    }

    class Pending(
        private val binding: ItemDiscoverPendingBinding,
        private val onClick: (MainscreenItem.Pending) -> Unit,
    ) : MainscreenItemViewHolder(binding) {

        @SuppressLint("SetTextI18n")
        fun bind(item: MainscreenItem.Pending) {
            binding.pendingText.text =
                item.connection.endpointId + " - " + item.connection.endpointName
            binding.root.setOnClickListener {
                onClick(item)
            }
        }
    }

    class Requested(
        private val binding: ItemDiscoverRequestedBinding,
    ) : MainscreenItemViewHolder(binding) {

        @SuppressLint("SetTextI18n")
        fun bind(item: MainscreenItem.Requested) {
            binding.requestedText.text =
                item.connection.endpointId + " - " + item.connection.endpointName
        }
    }
}