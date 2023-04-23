package at.aau.edu.wizards.ui.discover.recycler

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import at.aau.edu.wizards.databinding.*
import at.aau.edu.wizards.ui.discover.MainscreenItem

sealed class MainscreenItemViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    class Approved(
        private val binding: ItemMainscreenApprovedBinding,
    ) : MainscreenItemViewHolder(binding) {

        @SuppressLint("SetTextI18n")
        fun bind(item: MainscreenItem.Approved) {
            binding.approvedText.text =
                item.connection.endpointId + " - " + item.connection.endpointName
        }
    }

    class Header(
        private val binding: ItemMainscreenHeaderBinding,
    ) : MainscreenItemViewHolder(binding) {

        fun bind(item: MainscreenItem.Header) {
            binding.root.text = item.text
        }
    }

    class Pending(
        private val binding: ItemMainscreenPendingBinding,
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
        private val binding: ItemMainscreenRequestedBinding,
    ) : MainscreenItemViewHolder(binding) {

        @SuppressLint("SetTextI18n")
        fun bind(item: MainscreenItem.Requested) {
            binding.requestedText.text =
                item.connection.endpointId + " - " + item.connection.endpointName
        }
    }
}