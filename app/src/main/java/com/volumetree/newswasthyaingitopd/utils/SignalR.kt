package com.volumetree.newswasthyaingitopd.utils

import android.content.Context
import android.os.Parcelable
import android.util.Log
import com.google.gson.Gson
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.HubConnectionState
import com.volumetree.newswasthyaingitopd.di.BASE_URL_SIGNALR
import kotlinx.android.parcel.Parcelize

class SignalR(val context: Context, var signalEvent: eventUpdate) {

    private val TAG = "SignalR"


    interface eventUpdate {
        fun generatedUserId()
        fun chatMessageReceived(message: ChatSenderReceiverModel)
        fun endCallFromDoctor(message: ChatSenderReceiverModel)
        fun consultationReceived(
            senderReceiverModel: SenderReceiverModel,
            consultationReceived: ConsultationReceived
        )

        fun acceptCall(message: MessageModelSignalRReceived)
        fun endCall()
        fun syncReceived(message: SenderReceiverModel)
    }

    public var chatHubConnection: HubConnection? = null
    private var isHubConnected: Boolean = false

    fun startChatHubConnection() {
        if (chatHubConnection == null || chatHubConnection?.connectionState != HubConnectionState.DISCONNECTED) {
            chatHubConnection = HubConnectionBuilder.create(BASE_URL_SIGNALR)
                .build()
        }

        chatHubConnection?.start()?.blockingAwait()
    }

    fun connectionDidOpen(userType: Int) {
        isHubConnected = true
        print("******** Connected to SignalR ***********")
        connectEvents()
        if (userType == 1) {
            val doctorData = PrefUtils.getDoctorData(context)
            if (doctorData != null) {
                var name = doctorData.firstName
                if (doctorData.lastName.isNotEmpty()) {
                    name = "$name ${doctorData.lastName}"
                }
                join(
                    doctorData.memberId,
                    name,
                    doctorData.institutionName, userType
                )
            }
        }
        if (userType == 2) {
            val patientData = PrefUtils.getPatientUserData(context)
            if (patientData != null) {
                var name = patientData.firstName
                if (patientData.lastName.isNotEmpty()) {
                    name = "$name ${patientData.lastName}"
                }
                join(
                    patientData.patientInfoId,
                    name,
                    name, userType
                )
            }
        }
        if (userType == 3) {
            val choData = PrefUtils.getChoData(context)
            if (choData != null) {
                var name = choData.firstName
                if (choData.lastName.isNotEmpty()) {
                    name = "$name ${choData.lastName}"
                }
                join(
                    choData.memberId,
                    name,
                    choData.institutionName, userType
                )
            }
        }

    }

    fun connectionDidFailToOpen(error: Error) {
        isHubConnected = false
        print("******** Failed to connect to SignalR (error)***********")
        Log.d(TAG, error.message.toString())
    }

    fun connectionDidClose(error: Error) {
        isHubConnected = false
        Log.d(TAG, "connectionDidClose ${error.message.toString()}")
    }

    fun connectionDidReconnect() {
        isHubConnected = true
        Log.d(TAG, "connectionDidReconnect ")
    }

    fun connectionWillReconnect(error: Error) {
        isHubConnected = false
        Log.d(TAG, "connectionWillReconnect ${error.message}")
    }

    //MARK: Send Message
    fun sendMessage(event: String, json: ChatSenderReceiverModel) {
        if (chatHubConnection?.connectionState == HubConnectionState.CONNECTED) {
            chatHubConnection!!.send(event, json)
        }
    }

    //MARK: JOIN
    private fun join(id: Int, name: String, instituteName: String, userType: Int) {
        if (chatHubConnection?.connectionState == HubConnectionState.CONNECTED) {
            chatHubConnection!!.send("join", name, id, instituteName, userType)
        }
    }

    fun endCallFromCHOOrDoctor(eventKey: String, json: SenderReceiverModel) {
        Log.d(TAG, "$eventKey-${Gson().toJson(json)}")
        if (chatHubConnection?.connectionState == HubConnectionState.CONNECTED) {
            chatHubConnection!!.send(eventKey, json)
        }
    }

    fun callRequest(json: SenderReceiverModel) {
        Log.d(TAG, "Send ConsultationCall-${Gson().toJson(json)}")
        chatHubConnection!!.send("ConsultationCall", json)
    }

    fun acceptDenyCall(eventName: String, json: SenderReceiverModel) {
        Log.d(TAG, "Call-${Gson().toJson(json)}")
        chatHubConnection!!.send(eventName, json)
    }

    fun syncMessage(json: SenderReceiverModel) {
        Log.d(TAG, "Send syncMessage-${Gson().toJson(json)}")
        chatHubConnection!!.send("SyncFromCHO", json)
    }

    fun syncFromDoctor(json: SenderReceiverModel) {
        Log.d(TAG, "Send SyncFromDoctor-${Gson().toJson(json)}")
        chatHubConnection!!.send("SyncFromDoctor", json)
    }

    private fun connectEvents() {

        chatHubConnection?.on(
            "consultationReceived",
            { consultationData: ConsultationReceived, sendReceiverModel: SenderReceiverModel ->
                Log.d(TAG, "consultationReceived")
                signalEvent.consultationReceived(sendReceiverModel, consultationData)
            },
            ConsultationReceived::class.java,
            SenderReceiverModel::class.java
        )

        chatHubConnection?.on(
            "SyncFromDoctor",
            { successfull: SuccessCall, messageData: SenderReceiverModel ->
                Log.d(TAG, "SyncFromDoctor")
                signalEvent.syncReceived(messageData)
            }, SuccessCall::class.java, SenderReceiverModel::class.java
        )
        chatHubConnection?.on(
            "generatedUserId",
            { connectionid: String, memberid: String ->
                Log.d(TAG, "generatedUserId $connectionid")
                signalEvent.generatedUserId()
            }, String::class.java, String::class.java
        )
        chatHubConnection?.on(
            "messageReceived",
            { connectionid: String, messageData: SenderReceiverModel ->
                Log.d(TAG, "messageReceived $connectionid")
            }, String::class.java, SenderReceiverModel::class.java
        )

        chatHubConnection?.on(
            "AcceptCall",
            { connectionid: SuccessCall, messageData: MessageModelSignalRReceived ->
                signalEvent.acceptCall(messageData)
            },
            SuccessCall::class.java, MessageModelSignalRReceived::class.java
        )

        chatHubConnection?.on(
            "DenyCall",
            { connectionid: SuccessCall, messageData: MessageModelSignalRReceived ->
                Log.d(TAG, "DenyCall")
                signalEvent.endCall()
            },
            SuccessCall::class.java, MessageModelSignalRReceived::class.java
        )

        chatHubConnection?.on(
            "fallbackcall",
            { messageData: SuccessCall, connectionid: MessageModelSignalRReceived ->
                Log.d(TAG, "fallbackcall")
            },
            SuccessCall::class.java,
            MessageModelSignalRReceived::class.java
        )
        chatHubConnection?.on(
            "successcall",
            { messageData: SuccessCall, connectionid: MessageModelSignalRReceived ->
                Log.d(TAG, "successcall")
            }, SuccessCall::class.java, MessageModelSignalRReceived::class.java
        )

        chatHubConnection?.on(
            "chatmessageReceived",
            { successData: SenderReceiverModel, chatMessageData: ChatSenderReceiverModel ->
                signalEvent.chatMessageReceived(chatMessageData)
                Log.d(TAG, "chatmessageReceived")


            },
            SenderReceiverModel::class.java, ChatSenderReceiverModel::class.java,
        )
        chatHubConnection?.on(
            "EndCallFromDoctor",
            { successData: SenderReceiverModel, chatMessageData: ChatSenderReceiverModel ->
                Log.d(TAG, "EndCallFromDoctor")
                signalEvent.endCallFromDoctor(chatMessageData)
            },
            SenderReceiverModel::class.java,
            ChatSenderReceiverModel::class.java,
        )

    }

    class SuccessCall() {
        var participantType = 0
        var id = ""
        var status = 0
        var avatar: String? = ""
        var displayName = ""
        var fromHospitalName = ""
        var memberId = 0
        var type = 0
        var connectionId = ""
        var isAvailable = false
        var crNumber: String? = ""
    }

    class MessageModelSignalRSend(
        var fromId: String = "",
        var fromType: Int = 0,
        var toId: String = "",
        var toType: Int = 0,
        var message: String = "",
        var type: Int = 0,
        var senderId: String = "",
        var receiverId: String = "",
    )

    @Parcelize
    class MessageModelSignalRReceived : Parcelable {
        var fromId = "0"
        var fromType = 0
        var toId = "0"
        var toType = 0
        var message: String? = ""
        var dateSent: String? = ""
        var downloadUrl: String? = ""
        var consultationId = 0
        var connectionId: String? = ""
        var toConnectionId: String? = ""
        var senderId: String = ""
        var receiverId: String = ""
        var type = 0
    }

    class SenderReceiverModel(
        var senderId: String = "",
        var receiverId: String = "",
        var message: String = "",
        var fromId: String = "",
        var toId: String = "",
        var type: Int = 0,
        var fromType: Int = 0,
        var toType: Int = 0,
        var consultationId: Int = 0
    )


    class ChatSenderReceiverModel(
        var senderId: String = "",
        var receiverId: String = "",
        var message: String = "",
        var fromId: String = "",
        var toId: String = "",
        var type: Int = 0,
        var fromType: Int = 0,
        var toType: Int = 0,
        var dateSent: String? = "",
        var consultationId: Int? = 0

    )

    class ConsultationReceived {
        var displayName: String? = ""
        var memberId: Int = 0
    }
}