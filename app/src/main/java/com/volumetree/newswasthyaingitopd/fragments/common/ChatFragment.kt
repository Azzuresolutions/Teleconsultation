package com.volumetree.newswasthyaingitopd.fragments.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.adapters.ChatsAdapter
import com.volumetree.newswasthyaingitopd.application.App
import com.volumetree.newswasthyaingitopd.databinding.FragmentChatBinding
import com.volumetree.newswasthyaingitopd.utils.*
import com.volumetree.newswasthyaingitopd.viewmodel.MasterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class ChatFragment(
    private val toUserId: String,
    private val toType: Int,
    private val consultationId: Int,
    private val isEditable: Boolean
) : Fragment(),
    View.OnClickListener {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private var loginUserId = ""
    private val masterViewModel: MasterViewModel by viewModel()

    companion object {
        var chatList: ArrayList<SignalR.ChatSenderReceiverModel> = ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chatList.clear()
        loginUserId = requireActivity().getSenderId()
        setupAdapter()
        if (isEditable) {
            binding.imgSend.setOnClickListener(this)
        } else {
            binding.laySendMessage.visibility = View.GONE
            getChats()
        }
        binding.etMessage.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendMessageRequest()
                true
            }
            false
        }
    }

    private fun sendMessageRequest() {
        if (binding.etMessage.getTextFromEt() != "") {
            sendMessage()
        }
    }

    private fun setupAdapter() {
        binding.rvChats.setVerticalLayoutManager(requireActivity())
        chatsAdapter = ChatsAdapter(loginUserId, chatList)
        binding.rvChats.adapter = chatsAdapter
    }

    private fun getChats() {
        masterViewModel.chatMessageByConsultationId(consultationId)
            .observeOnce(viewLifecycleOwner) { chatData ->
                if (chatData.success && chatList.size != chatData.lstModel.size) {
                    chatList.addAll(chatData.lstModel)
                    if (binding.rvChats.visibility == View.GONE) {
                        binding.rvChats.visibility = View.VISIBLE
                        binding.tvNoChat.visibility = View.GONE
                    }
                    updateChatList()
                }
            }
    }

    override fun onResume() {
        super.onResume()
        setupAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(clickedView: View) {
        when (clickedView.id) {
            R.id.imgSend -> {
                sendMessageRequest()
            }
            R.id.imgAttachment -> {
            }
        }
    }

    private fun sendMessage() {
        val fromId = requireActivity().getSenderId()

        val senderReceiverModel = SignalR.ChatSenderReceiverModel()
        senderReceiverModel.type = 1
        senderReceiverModel.fromType = requireActivity().getFromType()
        senderReceiverModel.toType = toType
        senderReceiverModel.senderId = fromId
        senderReceiverModel.fromId = fromId
        senderReceiverModel.toId = toUserId
        senderReceiverModel.receiverId = toUserId
        senderReceiverModel.message = binding.etMessage.getTextFromEt()
        senderReceiverModel.dateSent = Calendar.getInstance().time.localChatToServerDate()
        App.signalR?.sendMessage("SendMessage", senderReceiverModel)
        chatList.add(senderReceiverModel)
        binding.etMessage.setText("")
        updateChatList()
    }

    var chatsAdapter: ChatsAdapter? = null
    fun updateChatList() {
        requireActivity().runOnUiThread {
            if (binding.rvChats.visibility == View.GONE) {
                binding.rvChats.visibility = View.VISIBLE
                binding.tvNoChat.visibility = View.GONE
            }
            chatsAdapter?.addList(chatList)
            binding.rvChats.smoothScrollToPosition(chatList.size - 1)
        }
    }

}

