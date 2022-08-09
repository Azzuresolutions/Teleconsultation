package com.volumetree.newswasthyaingitopd.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.volumetree.newswasthyaingitopd.databinding.LayoutReceiveMessageBinding
import com.volumetree.newswasthyaingitopd.databinding.LayoutSendMessageBinding
import com.volumetree.newswasthyaingitopd.utils.SignalR
import com.volumetree.newswasthyaingitopd.utils.serverDateToLocalChatDate

class ChatsAdapter(
    private val loginUserId: String,
    private var messageList: ArrayList<SignalR.ChatSenderReceiverModel>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return if (messageList[position].fromId == loginUserId || messageList[position].senderId == loginUserId) {
            1
        } else {
            2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            SentViewHolder(
                LayoutSendMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        } else {
            ReceiveViewHolder(
                LayoutReceiveMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SentViewHolder) {
            holder.bind(messageList[position])
        } else if (holder is ReceiveViewHolder) {
            holder.bind(messageList[position])
        }
    }

    override fun getItemCount() = messageList.size

    fun addList(messages: ArrayList<SignalR.ChatSenderReceiverModel>) {
        messageList = messages
//        messageList.addAll(messages)
        notifyDataSetChanged()
    }

    inner class SentViewHolder(private val binding: LayoutSendMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(messageData: SignalR.ChatSenderReceiverModel) {
            binding.tvMessage.text = messageData.message
            binding.tvTime.text = messageData.dateSent?.serverDateToLocalChatDate() ?: ""
        }
    }

    inner class ReceiveViewHolder(private val binding: LayoutReceiveMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(messageData: SignalR.ChatSenderReceiverModel) {
            messageData.message.replace("/n", "")
            binding.tvMessage.text = messageData.message.replace("\n", "")
            binding.tvTime.text = messageData.dateSent?.serverDateToLocalChatDate() ?: ""
        }
    }
}