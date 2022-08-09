package com.volumetree.newswasthyaingitopd.fragments.patient

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.activities.ChatCallActivity
import com.volumetree.newswasthyaingitopd.application.App
import com.volumetree.newswasthyaingitopd.databinding.FragmentWaitingRoom2Binding
import com.volumetree.newswasthyaingitopd.enums.UserTypes
import com.volumetree.newswasthyaingitopd.model.requestData.cho.CancelTokenRequest
import com.volumetree.newswasthyaingitopd.model.requestData.cho.ChangeQueueRequest
import com.volumetree.newswasthyaingitopd.model.responseData.cho.ConsultationOnlineDoctorResponse
import com.volumetree.newswasthyaingitopd.utils.*
import com.volumetree.newswasthyaingitopd.viewmodel.MasterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class WaitingFragment2 : Fragment(), View.OnClickListener, SignalR.eventUpdate {

    companion object {
        var isBackFromWaiting = false
    }

    private var _binding: FragmentWaitingRoom2Binding? = null
    private val binding get() = _binding!!
    private val masterViewModel: MasterViewModel by viewModel()
    private val args: WaitingFragment2Args by navArgs()

    private val timer = object : CountDownTimer(Constants.TIMER_INTERVAL * 1000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            binding.tvCountDownTime.text = (millisUntilFinished / 1000).toString()
        }

        override fun onFinish() {
            manageTimeOutOrDeny(false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWaitingRoom2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.showBottomNavigationView(false)
        binding.tvActionbarTitle.text = getString(R.string.waiting_room)
        setUpDocumentList()
        binding.imgBack.setOnClickListener(this)
        binding.tvExistRoom.setOnClickListener(this)
        binding.btnConnectNow.setOnClickListener(this)
        binding.btnConnectNow.isSelected = true
        setupDoctorData()
        setupPatientInfo()
        timer.start()
    }

    private fun setupPatientInfo() {
        val patientData = args.patientData
        if (patientData != null) {
            binding.tvPatientName.text = "${patientData.firstname} ${patientData.lastname}"
            binding.tvToken.text = patientData.tokenNumber
            binding.id.text = patientData.crNumber
            binding.patientAge.text = patientData.age
            binding.tvGender.text = patientData.genderId.getGenderNameFromId()
        }
    }

    private fun setupDoctorData() {
        if (args.consultationData != null) {
            val consultationData = args.consultationData
            binding.imgProfile.loadImageURL(
                binding.root.context,
                doctor = true,
                imagePath = consultationData?.model?.doctorimage ?: ""
            )
            binding.tvDrType.text = consultationData?.model?.speciality
            binding.tvDrName.text = consultationData?.model?.doctor_Name
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        timer.cancel()
    }

    private fun setUpDocumentList() {
        binding.rvDocuments.setVerticalLayoutManager(requireActivity())
//        binding.rvDocuments.adapter = UploadedDocumentListAdapter(documents, false)
    }

    override fun onClick(clickedView: View) {
        when (clickedView.id) {
            R.id.imgBack -> {
                onBack()
            }
            R.id.tvExistRoom -> {
                CommonDialog.Companion.ShowExitConfirmationDialog(::onBack)
                    .show(parentFragmentManager, "")
            }
            R.id.btnConnectNow -> {
                binding.tvConnecting.visibility = View.VISIBLE
                binding.btnConnectNow.text = getString(R.string.connecting)
                binding.btnConnectNow.isEnabled = false
                ((requireActivity().application) as App).setupSignalR(this)
            }
        }
    }

    private fun onBack() {
        masterViewModel.cancelDeleteToken(
            CancelTokenRequest(
                doctorId = args.consultationData?.model?.doctor_id ?: 0,
                patientInfoId = args.consultationData?.patientInfoId ?: 0,
                consultationId = App.consultationId
            )
        ).observeOnce(viewLifecycleOwner) { cancelTokenData ->
            if (cancelTokenData.success) {
                isBackFromWaiting = true
                findNavController().popBackStack()
            }
        }

    }

    override fun generatedUserId() {
        Log.e("generatedUserId", "generatedUserId")
        App.signalR?.callRequest(
            SignalR.SenderReceiverModel(
                fromId = PrefUtils.getChoData(requireActivity())?.memberId.toString(),
                fromType = PrefUtils.getLoginUserType(requireActivity()),
                toId = args.consultationData?.model?.doctor_id.toString(),
                toType = UserTypes.DOCTOR.type,
                message = "ConsultationCall",
                type = 1,
                senderId = "",
                receiverId = "",
                consultationId = App.consultationId
            )
        )
    }

    override fun chatMessageReceived(message: SignalR.ChatSenderReceiverModel) {
    }

    override fun endCallFromDoctor(message: SignalR.ChatSenderReceiverModel) {
    }

    override fun consultationReceived(
        senderReceiverModel: SignalR.SenderReceiverModel,
        consultationReceived: SignalR.ConsultationReceived
    ) {
    }

    override fun acceptCall(message: SignalR.MessageModelSignalRReceived) {

        try {
            timer.cancel()
        } catch (e: Exception) {
        }
        val intent = Intent(
            requireActivity(),
            ChatCallActivity::class.java
        )
        intent.putExtra(Constants.MESSAGE_DATA, message)
            .putExtra(Constants.TO_TYPE, UserTypes.DOCTOR.type)
            .putExtra(Constants.TO_DOCTOR_NAME, args.consultationData?.model?.doctor_Name)
            .putExtra(Constants.DOCTOR_PROFILE_PIC, args.consultationData?.model?.doctorimage)
            .putExtra(Constants.TO_DOCTOR_ID, args.consultationData?.model?.doctor_id)
            .putExtra(Constants.CONSULTATION_ID, App.consultationId)
        startActivity(intent)
    }

    override fun endCall() {
        manageTimeOutOrDeny(true)
    }

    private fun manageTimeOutOrDeny(isDeny: Boolean) {
        requireActivity().runOnUiThread {
            try {
                timer.cancel()
            } catch (e: Exception) {
            }
            if (isDeny) {
                requireActivity().showToast("Call denied by doctor")
            } else {
                requireActivity().showToast("Timeout")
            }

            CommonDialog.Companion.ShowSelectAnotherTryAgainWaitingListDialog(
                ::onAnother,
                ::onTryAgain
            )
                .show(parentFragmentManager, "")
        }
    }

    private fun onTryAgain() {
        masterViewModel.changePatientQueue(
            ChangeQueueRequest(
                patientInfoId = args.consultationData?.patientInfoId ?: 0,
                doctorId = args.consultationData?.model?.doctor_id ?: 0
            )
        )
            .observeOnce(viewLifecycleOwner) { queueData ->
                if (queueData.success) {
                    openWaitingRoom(args.consultationData)
                }
            }
    }

    private fun openWaitingRoom(doctorData: ConsultationOnlineDoctorResponse?) {
        if (doctorData != null) {
            findNavController().popBackStack()
        }
    }

    private fun onAnother() {
        binding.imgBack.performClick()
    }

    override fun syncReceived(message: SignalR.SenderReceiverModel) {
    }


}