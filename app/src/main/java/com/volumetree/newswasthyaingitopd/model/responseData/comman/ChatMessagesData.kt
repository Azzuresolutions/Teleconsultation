package com.volumetree.newswasthyaingitopd.model.responseData.comman

import com.volumetree.newswasthyaingitopd.utils.SignalR

class ChatMessagesData(
    val lstModel: ArrayList<SignalR.ChatSenderReceiverModel> = ArrayList()
) : BaseResponse()
