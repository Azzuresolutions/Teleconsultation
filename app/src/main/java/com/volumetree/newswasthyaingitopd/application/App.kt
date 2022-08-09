package com.volumetree.newswasthyaingitopd.application

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.activities.ChatCallActivity
import com.volumetree.newswasthyaingitopd.activities.ChatCallActivity2
import com.volumetree.newswasthyaingitopd.activities.SplashInfoActivity
import com.volumetree.newswasthyaingitopd.di.networkModule
import com.volumetree.newswasthyaingitopd.di.repositoryModule
import com.volumetree.newswasthyaingitopd.di.viewModelModule
import com.volumetree.newswasthyaingitopd.model.requestData.cho.ChangeQueueRequest
import com.volumetree.newswasthyaingitopd.model.requestData.doctor.InProgressConsultationRequest
import com.volumetree.newswasthyaingitopd.utils.*
import com.volumetree.newswasthyaingitopd.viewmodel.DoctorViewModel
import com.volumetree.newswasthyaingitopd.viewmodel.MasterViewModel
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

public class App : Application(), SignalR.eventUpdate {
    private var mActivity: Activity? = null

    companion object {
        var signalR: SignalR? = null
        var consultationId = 0
        var last_status = -1
    }

    override fun onCreate() {
        super.onCreate()
        initKoin()
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, p1: Bundle?) {
                mActivity = activity
            }

            override fun onActivityStarted(p0: Activity) {
            }

            override fun onActivityResumed(p0: Activity) {
                mActivity = p0
            }

            override fun onActivityPaused(p0: Activity) {
            }

            override fun onActivityStopped(p0: Activity) {
            }

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
                if (activity == mActivity) {
                    mActivity = null
                }
            }
        })

    }


    fun setupSignalR(eventUpdate: SignalR.eventUpdate = this) {
        signalR = SignalR(this, eventUpdate)
        signalR?.startChatHubConnection()
        signalR?.connectionDidOpen(PrefUtils.getLoginUserType(this))
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@App)
            modules(
                listOf(repositoryModule, viewModelModule, networkModule)
            )
        }
    }

    override fun generatedUserId() {

    }

    override fun chatMessageReceived(message: SignalR.ChatSenderReceiverModel) {
        if (mActivity is ChatCallActivity) {
            (mActivity as ChatCallActivity).chatMessageReceived(message)
        } else if (mActivity is ChatCallActivity2) {
            (mActivity as ChatCallActivity2).chatMessageReceived(message)
        }
    }

    override fun endCallFromDoctor(message: SignalR.ChatSenderReceiverModel) {
        if (mActivity is ChatCallActivity) {
            (mActivity as ChatCallActivity).endCallFromDoctor(message)
        } else if (mActivity is ChatCallActivity2) {
            (mActivity as ChatCallActivity2).endCallFromDoctor(message)
        }
    }

    override fun consultationReceived(
        senderReceiverModel: SignalR.SenderReceiverModel,
        consultationReceived: SignalR.ConsultationReceived
    ) {
        showIncomingCallNotification(senderReceiverModel, consultationReceived)
    }

    private fun showIncomingCallNotification(
        senderReceiverModel: SignalR.SenderReceiverModel,
        consultationReceived: SignalR.ConsultationReceived
    ) {
        if (mActivity != null) {
            consultationId = senderReceiverModel.consultationId
            Handler(Looper.getMainLooper()).post {
                mActivity!!.showIncomingCallDialog(
                    senderReceiverModel,
                    consultationReceived,
                    ::onAccept,
                    ::onDeny
                )
            }
        }
    }

    private fun onAccept(
        senderReceiverModel: SignalR.SenderReceiverModel,
        consultationReceived: SignalR.ConsultationReceived
    ) {
        mActivity?.get<DoctorViewModel>()
            ?.updateOnlineDoctorsAcceptCall(
                InProgressConsultationRequest(
                    consultationId = senderReceiverModel.consultationId,
                    sendTo = senderReceiverModel.fromId.toInt(),
                    sendBy = PrefUtils.getDoctorData(mActivity!!)!!.memberId,
                    statusId = 4
                )
            )
        signalR!!.acceptDenyCall(
            "AcceptCall", SignalR.SenderReceiverModel(
                senderId = "",
                receiverId = "",
                message = "AcceptCall",
                fromId = PrefUtils.getDoctorData(mActivity!!)!!.memberId.toString(),
                toId = senderReceiverModel.fromId,
                type = 1,
                fromType = PrefUtils.getLoginUserType(mActivity!!),
                toType = senderReceiverModel.fromType,
                consultationId = senderReceiverModel.consultationId
            )
        )

        val messageModelSignalRReceived = SignalR.MessageModelSignalRReceived()
        messageModelSignalRReceived.fromId = senderReceiverModel.fromId
        messageModelSignalRReceived.consultationId = senderReceiverModel.consultationId
        mActivity!!.startActivity(
            Intent(mActivity, ChatCallActivity::class.java)
                .putExtra(
                    Constants.MESSAGE_DATA,
                    messageModelSignalRReceived
                )
                .putExtra(Constants.TO_DOCTOR_ID, senderReceiverModel.fromId.toInt())
                .putExtra(Constants.TO_TYPE, senderReceiverModel.fromType)
                .putExtra(Constants.TO_DOCTOR_NAME, consultationReceived.displayName)

        )
    }

    private fun onDeny(
        senderReceiverModel: SignalR.SenderReceiverModel,
        consultationReceived: SignalR.ConsultationReceived
    ) {
        mActivity?.get<MasterViewModel>()?.changePatientQueue(
            ChangeQueueRequest(
                patientInfoId = senderReceiverModel.fromId.toInt(),
                doctorId = PrefUtils.getDoctorData(mActivity!!)!!.memberId
            )
        )

        signalR!!.acceptDenyCall(
            "DenyCall", SignalR.SenderReceiverModel(
                senderId = "",
                receiverId = "",
                message = "DenyCall",
                fromId = PrefUtils.getDoctorData(mActivity!!)!!.memberId.toString(),
                toId = senderReceiverModel.fromId,
                type = 1,
                fromType = PrefUtils.getLoginUserType(mActivity!!),
                toType = senderReceiverModel.fromType,
                consultationId = senderReceiverModel.consultationId
            )
        )
    }

    override fun acceptCall(message: SignalR.MessageModelSignalRReceived) {
    }

    override fun endCall() {

    }

    override fun syncReceived(message: SignalR.SenderReceiverModel) {
        if (mActivity is ChatCallActivity) {
            (mActivity as ChatCallActivity).syncReceived(message)
        } else if (mActivity is ChatCallActivity2) {
            (mActivity as ChatCallActivity2).syncReceived(message)
        }
    }

    fun setupEvent() {
        signalR?.signalEvent = this
    }

    fun confirmLogout(isConfirmLogout: Boolean, textMessage: String) {
        if (isConfirmLogout && mActivity != null) {
            if (textMessage.isEmpty()) {
//                mActivity!!.showToast(getString(R.string.logout_message))
            } else {
                mActivity!!.showToast(getString(R.string.session_expired))
            }
            signalR?.chatHubConnection?.stop()
            PrefUtils.setLogin(mActivity!!, 0)
            PrefUtils.setDoctorData(mActivity!!, null)
            PrefUtils.setChoData(mActivity!!, null)
            PrefUtils.setPatientUserData(mActivity!!, null)
            mActivity!!.startActivity(Intent(mActivity!!, SplashInfoActivity::class.java))
            mActivity!!.finish()
        }
    }

}