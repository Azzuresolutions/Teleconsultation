package com.volumetree.newswasthyaingitopd.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.application.App
import com.volumetree.newswasthyaingitopd.databinding.FragmentChatCallBinding
import com.volumetree.newswasthyaingitopd.enums.UserTypes
import com.volumetree.newswasthyaingitopd.fragments.common.ChatCallFragment
import com.volumetree.newswasthyaingitopd.fragments.common.ChatFragment.Companion.chatList
import com.volumetree.newswasthyaingitopd.utils.*
import com.volumetree.newswasthyaingitopd.utils.Constants.Companion.CONSULTATION_ID
import com.volumetree.newswasthyaingitopd.utils.Constants.Companion.DOCTOR_PROFILE_PIC
import com.volumetree.newswasthyaingitopd.utils.Constants.Companion.IS_CONSULTATION_HISTORY
import com.volumetree.newswasthyaingitopd.utils.Constants.Companion.MESSAGE_DATA
import com.volumetree.newswasthyaingitopd.utils.Constants.Companion.TO_DOCTOR_ID
import com.volumetree.newswasthyaingitopd.utils.Constants.Companion.TO_DOCTOR_NAME
import com.volumetree.newswasthyaingitopd.utils.Constants.Companion.TO_TYPE
import com.volumetree.newswasthyaingitopd.viewmodel.ChoViewModel
import com.volumetree.newswasthyaingitopd.viewmodel.DoctorViewModel
import com.volumetree.newswasthyaingitopd.viewmodel.MasterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class ChatCallActivity : AppCompatActivity() {

    private var chatFragment: ChatCallFragment? = null
    private val P2PTAG: String = "ChatCall"
    var binding: FragmentChatCallBinding? = null
    var message: SignalR.MessageModelSignalRReceived? = null

    var toUserId: String = ""
    var remoteUserName: String = ""
    private var doctorProfilePic: String = ""
    private var consultationId: Int = 0

    private val choViewModel: ChoViewModel by viewModel()
    private val doctorViewModel: DoctorViewModel by viewModel()
    private val masterViewModel: MasterViewModel by viewModel()

    companion object {
        var lastConsultationId = 0
        var consultDoctorName = ""
        var lastConsultationTime = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentChatCallBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        (application as App).setupEvent()

        message = intent.getParcelableExtra(MESSAGE_DATA)
        remoteUserName = intent.getStringExtra(TO_DOCTOR_NAME).toString()
        doctorProfilePic = intent.getStringExtra(DOCTOR_PROFILE_PIC).toString()
        toUserId = intent.getIntExtra(TO_DOCTOR_ID, 0).toString()
        consultationId = intent.getIntExtra(CONSULTATION_ID, 0)
        consultDoctorName = remoteUserName

        binding!!.tvActionbarTitle.text = remoteUserName
//        binding!!.imgDr.loadImageURL(this, doctor = true, imagePath = doctorProfilePic)
        lastConsultationId = 0

        if (PrefUtils.getLoginUserType(this) == UserTypes.DOCTOR.type) {
        } else {
            getDoctorDetails(toUserId)
        }

        if (!intent.getBooleanExtra(IS_CONSULTATION_HISTORY, false)) {
            startActivity(
                Intent(this, ChatCallActivity2::class.java)
                    .putExtra(MESSAGE_DATA, message)
                    .putExtra(TO_TYPE, intent.getIntExtra(TO_TYPE, 0))
                    .putExtra(TO_DOCTOR_NAME, remoteUserName)
                    .putExtra(DOCTOR_PROFILE_PIC, doctorProfilePic)
                    .putExtra(TO_DOCTOR_ID, toUserId)
                    .putExtra(CONSULTATION_ID, consultationId)
            )
        }
        onChats()
    }

    private fun getDoctorDetails(toUserId: String) {
        masterViewModel.getDoctorProfileById(toUserId.toInt())
            .observeOnce(this) { doctorData ->
                if (doctorData.success) {
                    try {
                        runOnUiThread {
                            binding?.tvActionbarTitle?.text = doctorData.model.doctor_Name
                            binding?.imgDr?.loadImageURL(
                                this@ChatCallActivity,
                                doctor = true,
                                imagePath = doctorData.model.doctorimage
                            )
                        }
                    } catch (e: Exception) {
                        Log.d("", "")
                    }
                }
            }
    }

    override fun onResume() {
        super.onResume()
        when (App.last_status) {
            0 -> {
                onChats()
            }
            1 -> {
                onPrescription()
            }
            2 -> {
                onViewCaseHistory()
            }
        }
    }

    public fun onEndCall() {
        startActivity(
            Intent(this, RatingActivity::class.java).putExtra(
                "remoteUserName",
                remoteUserName
            ).putExtra("consultationId", consultationId.toString())
                .putExtra("toUserId", toUserId)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
        finish()
    }

    private fun onChats() {
        if (chatFragment == null) {
            chatFragment = ChatCallFragment(
                consultationId,
                toUserId,
                remoteUserName,
                intent.getIntExtra(TO_TYPE, 0),
                intent.getBooleanExtra(IS_CONSULTATION_HISTORY, false)
            )
            switchFragment(chatFragment!!, "chatFragment")
        } else {
            chatFragment?.updateCurrentFragment(0)
        }
    }

    private fun onPrescription() {
        chatFragment?.updateCurrentFragment(1)
    }

    private fun onViewCaseHistory() {
        chatFragment?.updateCurrentFragment(2)
    }

    //Call Fragment
    private fun switchFragment(fragment: Fragment, tag: String) {
        Log.d(
            P2PTAG,
            "switch Fragment UI Shown"
        )
        binding?.fragmentContainer?.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment, tag)
            .commitAllowingStateLoss()
    }

    fun chatMessageReceived(message: SignalR.ChatSenderReceiverModel) {
        chatList.add(message)
        chatFragment?.updateChatList()
    }

    var isEndCall = false
    var endCallData: SignalR.ChatSenderReceiverModel? = null

    fun endCallFromDoctor(message: SignalR.ChatSenderReceiverModel) {
        isEndCall = true
        endCallData = message
        if (!isPrescriptionShowed) {
            runOnUiThread {
//            endCallAndFreeDoctor(endCallData!!)
                choViewModel.getConsultation(consultationId)
                    .observeOnce(this) { prescriptionData ->
                        isPrescriptionShowed = true
                        this.dialogPrescriptionViewSync(
                            prescriptionData,
                            ::onPrescriptionDismiss
                        )
                    }
            }
        }
    }

    var isPrescriptionShowed = false
    fun syncReceived(message: SignalR.SenderReceiverModel) {
        runOnUiThread {
            lastConsultationTime = Calendar.getInstance().time.formatDate()
            lastConsultationId = message.consultationId
            message.consultationId.let {
                choViewModel.getConsultation(it).observeOnce(this) { prescriptionData ->
                    isPrescriptionShowed = true
                    this.dialogPrescriptionViewSync(
                        prescriptionData,
                        ::onPrescriptionDismiss
                    )
                }
            }
        }
    }

    override fun onBackPressed() {
        if (intent.getBooleanExtra(IS_CONSULTATION_HISTORY, false)) {
            super.onBackPressed()
        }
//        else {
//            if (endCallData != null) {
//                endCallAndFreeDoctor(endCallData!!)
//            }
//        }
    }

    private fun onPrescriptionDismiss() {
        isPrescriptionShowed = false
        if (isEndCall) {
            endCallAndFreeDoctor(endCallData!!)
        }
    }

    private fun endCallAndFreeDoctor(message: SignalR.ChatSenderReceiverModel) {
        runOnUiThread {
            onEndCall()
//            doctorViewModel.updateOnlineDoctorsDenyEndCall(
//                ConsultationId = message.consultationId ?: 0,
//                PatientInfoId = PrefUtils.getChoData(this)!!.memberId,
//                DoctorID = message.senderId.toInt()
//            ).observeOnce(this) { availabilityResponse ->
//                onEndCall()
//            }
        }
    }
}

