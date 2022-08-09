package com.volumetree.newswasthyaingitopd.activities

import android.Manifest
import android.app.PictureInPictureParams
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Rational
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.peoplelink.instapeer.ActionCallBack
import com.peoplelink.instapeer.InstaListener
import com.peoplelink.instapeer.InstaSDK
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.activities.ChatCallActivity.Companion.consultDoctorName
import com.volumetree.newswasthyaingitopd.activities.ChatCallActivity.Companion.lastConsultationId
import com.volumetree.newswasthyaingitopd.activities.ChatCallActivity.Companion.lastConsultationTime
import com.volumetree.newswasthyaingitopd.activities.cho.ChoDashBoardActivity
import com.volumetree.newswasthyaingitopd.activities.doctor.DoctorDashBoardActivity
import com.volumetree.newswasthyaingitopd.application.App
import com.volumetree.newswasthyaingitopd.application.App.Companion.signalR
import com.volumetree.newswasthyaingitopd.databinding.FragmentChatCallBinding
import com.volumetree.newswasthyaingitopd.enums.UserTypes
import com.volumetree.newswasthyaingitopd.fragments.common.CallFragment
import com.volumetree.newswasthyaingitopd.fragments.common.ChatFragment
import com.volumetree.newswasthyaingitopd.fragments.doctor.DoctorRxFragment
import com.volumetree.newswasthyaingitopd.interfaces.VCConnectListener
import com.volumetree.newswasthyaingitopd.utils.*
import com.volumetree.newswasthyaingitopd.utils.Constants.Companion.CONSULTATION_ID
import com.volumetree.newswasthyaingitopd.utils.Constants.Companion.DOCTOR_PROFILE_PIC
import com.volumetree.newswasthyaingitopd.utils.Constants.Companion.IS_CONSULTATION_HISTORY
import com.volumetree.newswasthyaingitopd.utils.Constants.Companion.MESSAGE_DATA
import com.volumetree.newswasthyaingitopd.utils.Constants.Companion.TO_DOCTOR_ID
import com.volumetree.newswasthyaingitopd.utils.Constants.Companion.TO_DOCTOR_NAME
import com.volumetree.newswasthyaingitopd.viewmodel.ChoViewModel
import com.volumetree.newswasthyaingitopd.viewmodel.DoctorViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.webrtc.SurfaceViewRenderer
import java.util.*

class ChatCallActivity2 : AppCompatActivity(), VCConnectListener,
    ConnectivityReceiver.ConnectivityReceiverListener,
    InstaListener {

    var callFragment: CallFragment? = null
    var audioManager: AudioManager? = null
    private val P2PTAG: String = "ChatCall"
    private var localRenderer: SurfaceViewRenderer? = null
    private var remoteRenderer: SurfaceViewRenderer? = null
    private var topLayout: ConstraintLayout? = null
    private var bottomOptions: LinearLayout? = null
    var p2PClient: InstaSDK? = null
    var binding: FragmentChatCallBinding? = null
    private var loggedIn = false
    var isP2PConnected = false
    var earlyJoin = false
    var isSignalRConnected = false

    private var myId: String? = null
    private var myIdConnection: String? = null
    private var peerId: String? = null
    private var peerIdConnection: String? = null
    var message: SignalR.MessageModelSignalRReceived? = null

    var toUserId: String = ""
    var remoteUserName: String = ""
    var doctorProfilePic: String = ""
    var consultationId: Int = 0

    private var mPictureInPictureParamsBuilder: PictureInPictureParams.Builder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentChatCallBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        message = intent.getParcelableExtra(MESSAGE_DATA)
        remoteUserName = intent.getStringExtra(TO_DOCTOR_NAME).toString()
        doctorProfilePic = intent.getStringExtra(DOCTOR_PROFILE_PIC).toString()
        toUserId = intent.getStringExtra(TO_DOCTOR_ID).toString()
        consultationId = intent.getIntExtra(CONSULTATION_ID, 0)
        consultDoctorName = remoteUserName

        binding!!.tvActionbarTitle.text = remoteUserName
        binding!!.imgDr.loadImageURL(this, doctor = true, imagePath = doctorProfilePic)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mPictureInPictureParamsBuilder = PictureInPictureParams.Builder()
        }

        setupSignalR()
    }

    private fun openPIP() {
        mPictureInPictureParamsBuilder?.setAspectRatio(Rational(300, 500))
        mPictureInPictureParamsBuilder?.let { enterPictureInPictureMode(it.build()) }
    }

    private fun setupSignalR() {

        message = intent.getParcelableExtra(MESSAGE_DATA)

        if (message?.fromId == "0" || message?.fromId == "") {
            message?.fromId = toUserId
        }
        initP2PConnection(
            selfId = signalR?.chatHubConnection?.connectionId.toString(),
            toId = message?.fromId ?: toUserId,
            earlyJoin = false,
            ReceiverId = when (PrefUtils.getLoginUserType(this)) {
                UserTypes.DOCTOR.type -> {
                    PrefUtils.getDoctorData(this)?.memberId.toString()
                }
                UserTypes.PATIENT.type -> {
                    PrefUtils.getPatientUserData(this)?.patientInfoId.toString()
                }
                UserTypes.CHO.type -> {
                    PrefUtils.getChoData(this)?.memberId.toString()
                }
                else -> {
                    ""
                }
            },
            SenderId = toUserId
        )
    }

    override fun onReady(
        localRenderer: SurfaceViewRenderer,
        remoteRenderer: SurfaceViewRenderer,
        topLayout: ConstraintLayout,
        bottomOptions: LinearLayout
    ) {
//        Handler(Looper.getMainLooper()).postDelayed({
        runOnUiThread {
            updateSurfaceView(
                localRenderer,
                remoteRenderer,
                topLayout,
                bottomOptions,
            )
        }
//        }, 500)
    }

    private fun updateSurfaceView(
        localRenderer: SurfaceViewRenderer,
        remoteRenderer: SurfaceViewRenderer,
        topLayout: ConstraintLayout,
        bottomOptions: LinearLayout
    ) {
        try {
            Log.d(P2PTAG, "onReady VC Assign local and remote surface render")
            audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audioManager!!.mode = AudioManager.MODE_IN_COMMUNICATION
            audioManager!!.isSpeakerphoneOn = true

            this.localRenderer = localRenderer
            this.remoteRenderer = remoteRenderer
            this.topLayout = topLayout
            this.bottomOptions = bottomOptions
            InstaSDK.initialise(
                this@ChatCallActivity2,
                this.localRenderer,
                this.remoteRenderer,
                false,
                false
            )

            loggedIn = true

            runOnUiThread {
                try {
                    isP2PConnected = true

                    if (earlyJoin) {
                        Log.d(P2PTAG, "Early join - so no camera :: $earlyJoin")
//                        requestVC(myIdConnection, peerIdConnection, myId, peerId);
                    } else {
                        Log.d(P2PTAG, "Late Join share camera :: $earlyJoin")
                        shareCamera()
                    }
                } catch (e: Exception) {
                    Log.e(P2PTAG, "onReady request Error: " + e.message)
                }
            }
        } catch (e: Exception) {
            Log.e(P2PTAG, "onReady catch err::" + e.message.toString())
        }
    }

    override fun onEndCall() {
        when (PrefUtils.getLoginUserType(this)) {
            UserTypes.CHO.type -> {
                signalR?.endCallFromCHOOrDoctor(
                    "EndCallFromCHO",
                    SignalR.SenderReceiverModel(
                        fromId = PrefUtils.getChoData(this)?.memberId.toString(),
                        fromType = PrefUtils.getLoginUserType(this),
                        toId = toUserId,
                        toType = UserTypes.DOCTOR.type,
                        message = "EndCallFromCHO",
                        type = 1,
                        senderId = "",
                        receiverId = "",
                        consultationId = consultationId
                    )
                )
                openRatingActivity()
            }
            UserTypes.DOCTOR.type -> {
                signalR?.endCallFromCHOOrDoctor(
                    "EndCallFromDoctor",
                    SignalR.SenderReceiverModel(
                        fromId = PrefUtils.getDoctorData(this)?.memberId.toString(),
                        fromType = PrefUtils.getLoginUserType(this),
                        toId = toUserId,
                        toType = UserTypes.CHO.type,
                        message = "EndCallFromDoctor",
                        type = 1,
                        senderId = "",
                        receiverId = "",
                        consultationId = consultationId
                    )
                )
                leaveConference()
            }
        }
    }

    private fun openRatingActivity() {

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

    override fun onChats() {
        openPIP()
        App.last_status = 0
    }

    override fun onShowRXForDoctor() {
        DoctorRxFragment.toUserId = toUserId
        DoctorRxFragment.remoteUserName = remoteUserName
        switchFragment(DoctorRxFragment())
    }

    override fun onPrescription() {
        openPIP()
        App.last_status = 1
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        if (isInPictureInPictureMode) {
            localRenderer?.visibility = View.GONE
            topLayout?.visibility = View.GONE
            bottomOptions?.visibility = View.GONE
        } else {
            localRenderer?.visibility = View.VISIBLE
            topLayout?.visibility = View.VISIBLE
            bottomOptions?.visibility = View.VISIBLE
        }

    }

    override fun onViewCaseHistory() {
        openPIP()
        App.last_status = 2
    }

    private fun shareCamera() {
        try {
            if (!TextUtils.isEmpty(peerId)) {
                Log.d(
                    P2PTAG,
                    "Share Camera Call"
                )
                try {
                    InstaSDK.makeCall(peerId, object : ActionCallBack {
                        override fun onSuccess(result: String) {
                            Log.d(
                                P2PTAG,
                                "makeCall onSuccess: $result"
                            )
                        }

                        override fun onFailure(error: String) {
                            Log.d(
                                P2PTAG,
                                "makeCall onFailure: $error"
                            )
                        }
                    })

                    callFragment?.onPublished(true, p2PClient) //audioManager,
                    if (!earlyJoin) {
                        Log.d(
                            P2PTAG,
                            "onPublishRequest - VideoCallConnected"
                        )

                    }
                } catch (e: java.lang.Exception) {
                    Log.e(
                        P2PTAG,
                        "shareCamera request error ::" + e.message.toString()
                    )

                    //AppGlobal.mFirebaseCrashlytics.getInstance().setCustomKey(P2PTAG, "shareCamera request catch err::" + e.getMessage().toString());
                }
            } else {
                Log.d(
                    P2PTAG,
                    "share camera Peer Not Specified"
                )
                showToast("Peer Not Specified")
            }
        } catch (e: java.lang.Exception) {
            Log.e(
                P2PTAG,
                "P2P: share camera error :: " + e.message
            )

            //AppGlobal.mFirebaseCrashlytics.getInstance().setCustomKey(P2PTAG, "P2P: share camera catch err:: " + e.getMessage());
        }
    }

    private fun initP2PConnectionMain() {
        try {
            if (p2PClient == null) {
                try {
                    p2PClient = InstaSDK()
                    Log.d(
                        P2PTAG,
                        "P2P InstaVC SDK Intialized"
                    )

                    InstaSDK.instaListener(this)
                    Log.d(
                        P2PTAG,
                        "P2P InstaVC SDK instaListener :: "
                    )

                    InstaSDK.connectServer(
                        resources.getString(R.string.peerInsta),
                        myId,
                        object : ActionCallBack {
                            override fun onSuccess(result: String) {
                                Log.d(
                                    P2PTAG,
                                    "connect onSuccess: $result"
                                )
                                runOnUiThread {
                                    Log.d(
                                        P2PTAG,
                                        "connect onSuccess UI: $result"
                                    )
                                    Log.d(
                                        P2PTAG,
                                        "P2P InstaVC Client Details after Connect Request :: $p2PClient"
                                    )
                                    requestPermission()
                                }
                            }

                            override fun onFailure(error: String) {
                                Log.d(
                                    P2PTAG,
                                    "initP2PConnectionMain InstaVC connect server onFailure: $error"
                                )
                            }
                        })
                } catch (e: java.lang.Exception) {
                    Log.e(
                        P2PTAG,
                        "P2P InstaVC initP2PConnectionMain P2PClient catch err:: " + e.message
                    )
                }
            }
        } catch (e: java.lang.Exception) {
            Log.e(
                P2PTAG,
                "P2P InstaVC initP2PConnectionMain catch err:: " + e.message
            )
        }
    }

    private fun requestPermission() {
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.MODIFY_AUDIO_SETTINGS
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        onConnectSucceed()
                    } else {

                        this@ChatCallActivity2.showToast(
                            "Required Permissions Not Granted"
                        )
                        return
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken
                ) {
                    this@ChatCallActivity2.showToast(
                        "Required Permissions Not Granted"
                    )
                    token.continuePermissionRequest()
                    return
                }
            })
            .withErrorListener { error -> Log.e("Dexter", "There was an error: $error") }.check()
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        try {
            if (isConnected) {
                if (!isSignalRConnected) {
                    signalR?.startChatHubConnection()
                }
            } else {
                this.showToast(getString(R.string.no_internet_connection))
            }
        } catch (e: Exception) {
            Log.e("NetworkIssue", e.message.toString())
        }
    }

    override fun offerReceived(s: String) {
        Log.d(
            P2PTAG,
            "offerReceived :: $s"
        )
    }

    override fun onFinished() {
        Log.d(
            P2PTAG,
            "P2P: onFinished Called when p2PClient disconnect() called from leave conference"
        )

        //Device AudioManager Settings
        audioManagerModeNormal()

    }

    override fun remoteUserDisconnected() {
        Log.d(
            P2PTAG,
            "P2P: remote User video Disconnected"
        )
    }

    // if audiomanager off then in case back to queue ring works on speakerOff
    //Device AudioManager Settings
    private fun audioManagerModeNormal() {

        try {
            if (audioManager != null) {
                if (audioManager!!.mode == AudioManager.MODE_IN_COMMUNICATION
                    && audioManager!!.isSpeakerphoneOn
                ) {
                    audioManager!!.mode = AudioManager.MODE_NORMAL
                    audioManager!!.isSpeakerphoneOn = false
                    Log.d(
                        P2PTAG,
                        "audioManager MODE_NORMAL :: " + audioManager!!.isSpeakerphoneOn
                    )
                }
            }
        } catch (e: java.lang.Exception) {
            Log.e(
                P2PTAG,
                "audioManager audio Manager catch err::" + e.message
            )
        }
    }

    private fun onConnectSucceed() {
        try {
            Log.d(
                P2PTAG,
                "CallFragment onConnectSucceed called invoke Fragment"
            )
            if (callFragment == null) {
                callFragment = CallFragment(remoteUserName)
            }
            switchFragment(callFragment!!)
        } catch (e: java.lang.Exception) {
            Log.e(
                P2PTAG,
                "CallFragment onConnectSucceed Fragment catch err:: " + e.message
            )
        }
    }

    private fun switchFragment(fragment: Fragment) {
        Log.d(
            P2PTAG,
            "switch Fragment UI Shown"
        )
        binding?.fragmentContainer?.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
            .commitAllowingStateLoss()
    }

    private fun leaveConference() {
        Log.d(
            P2PTAG,
            "LeaveConnference Called..."
        )
        try {
            if (loggedIn || isP2PConnected) {
                //Updated 30June2020
                //if call ended then dispose connectedParticipant and superConsultationResponseModel

                if (p2PClient != null) {
                    Log.d(
                        P2PTAG,
                        "LeaveConnference p2PClient - Disconnect Requested"
                    )
                    //It will call disconnect() to send remoteuser disconnect message and leave() for clearing all p2pclient instances
                    InstaSDK.disconnect()
                    p2PClient = null
                }
                if (callFragment != null) {
                    callFragment = null
                }
                if (!TextUtils.isEmpty(peerId)) {
                    peerId = ""
                    peerIdConnection = ""
                }
                if (!TextUtils.isEmpty(myId)) {
                    myId = ""
                    myIdConnection = ""
                }
                if (localRenderer != null) {
                    localRenderer!!.release()
                }
                if (remoteRenderer != null) {
                    remoteRenderer!!.release()
                }
                isP2PConnected =
                    false
                loggedIn = false
            }
        } catch (e: java.lang.Exception) {
            p2PClient = null
            callFragment = null
            peerId = ""
            myId = ""
            if (localRenderer != null) {
                localRenderer!!.release()
            }
            if (remoteRenderer != null) {
                remoteRenderer!!.release()
            }
            isP2PConnected =
                false
            loggedIn = false
            Log.e(
                P2PTAG,
                "Leave Conference Exception :: " + e.message
            )
        }
        if (PrefUtils.getLoginUserType(this) == UserTypes.CHO.type) {
            startActivity(
                Intent(this, ChoDashBoardActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
        } else if (PrefUtils.getLoginUserType(this) == UserTypes.DOCTOR.type) {
            startActivity(
                Intent(this, DoctorDashBoardActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
        }
        finish()
    }

    private fun initP2PConnection(
        selfId: String,
        toId: String,
        earlyJoin: Boolean,
        ReceiverId: String,
        SenderId: String
    ) {
        try {

            myIdConnection =
                if (selfId != signalR?.chatHubConnection?.connectionId) {
                    signalR?.chatHubConnection?.connectionId
                } else {
                    selfId
                }
            myId = ReceiverId
            peerId = SenderId
            peerIdConnection = toId //.substring(0,4);
            Log.d(
                P2PTAG,
                "initP2PConnection - MyID: $myId, MyIDConnetion: $myIdConnection, PeerId: $peerId, peerIdConnection: $peerIdConnection"
            )
            this.earlyJoin = earlyJoin
            initP2PConnectionMain()
        } catch (e: java.lang.Exception) {
            Log.e(
                P2PTAG,
                "initP2PConnection catch err:: " + e.message
            )
        }
    }

    override fun onBackPressed() {
        if (intent.getBooleanExtra(IS_CONSULTATION_HISTORY, false)) {
            super.onBackPressed()
        } else {
            leaveConference()
        }
    }

    var isEndCall = false
    var endCallData: SignalR.ChatSenderReceiverModel? = null
    var isPrescriptionShowed = false
    private val choViewModel: ChoViewModel by viewModel()
    private val doctorViewModel: DoctorViewModel by viewModel()

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


    private fun onPrescriptionDismiss() {
        isPrescriptionShowed = false
        if (isEndCall) {
            endCallAndFreeDoctor(endCallData!!)
        }
    }

    private fun endCallAndFreeDoctor(message: SignalR.ChatSenderReceiverModel) {
        runOnUiThread {
            openRatingActivity()

//            doctorViewModel.updateOnlineDoctorsDenyEndCall(
//                ConsultationId = message.consultationId ?: 0,
//                PatientInfoId = PrefUtils.getChoData(this)!!.memberId,
//                DoctorID = message.senderId.toInt()
//            ).observeOnce(this) { availabilityResponse ->
//                openRatingActivity()
//            }
        }
    }

    fun endCallFromDoctor(message: SignalR.ChatSenderReceiverModel) {
        isEndCall = true
        endCallData = message
        if (!isPrescriptionShowed) {
//            endCallAndFreeDoctor(endCallData!!)
            runOnUiThread {
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

    fun chatMessageReceived(message: SignalR.ChatSenderReceiverModel) {
        ChatFragment.chatList.add(message)
    }

}

