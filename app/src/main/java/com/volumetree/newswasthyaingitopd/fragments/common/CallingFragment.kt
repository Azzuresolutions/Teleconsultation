package com.volumetree.newswasthyaingitopd.fragments.common

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
import com.volumetree.newswasthyaingitopd.databinding.FragmentCallingBinding
import com.volumetree.newswasthyaingitopd.enums.UserTypes
import com.volumetree.newswasthyaingitopd.model.requestData.cho.CancelTokenRequest
import com.volumetree.newswasthyaingitopd.model.requestData.cho.ChangeQueueRequest
import com.volumetree.newswasthyaingitopd.model.responseData.cho.ConsultationOnlineDoctorData
import com.volumetree.newswasthyaingitopd.model.responseData.cho.ConsultationOnlineDoctorResponse
import com.volumetree.newswasthyaingitopd.utils.*
import com.volumetree.newswasthyaingitopd.viewmodel.MasterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CallingFragment : Fragment(), View.OnClickListener, SignalR.eventUpdate {

    private var _binding: FragmentCallingBinding? = null
    private val binding get() = _binding

    private val args: CallingFragmentArgs by navArgs()

    private var doctorData: ConsultationOnlineDoctorData = ConsultationOnlineDoctorData()

    private val masterViewModel: MasterViewModel by viewModel()

    private val timer = object : CountDownTimer(Constants.TIMER_INTERVAL * 1000, 1000) {
        override fun onTick(millisUntilFinished: Long) {

        }

        override fun onFinish() {
            onEndCallManageUI(false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCallingBinding.inflate(inflater, container, false)
        return binding?.root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.imgBack?.setOnClickListener(this)
        binding?.tvTryAgain?.setOnClickListener(this)
        binding?.btnCallAnotherDoctor?.setOnClickListener(this)

        activity?.showBottomNavigationView(false)

        doctorData = args.doctorData?.model!!
        ((requireActivity().application) as App).setupSignalR(this)
        if (!args.specialityName.contains("General hub")) {
            binding?.tvCallingTitle?.text = getString(R.string.calling_a_doctor_in_speciality_hub)
            binding?.tvCallingDesc?.text =
                "We are finding you a available\ndoctor in ${args.specialityName} please wait for a while"
        }
//        binding?.imgDr?.loadImageURL(requireActivity(), doctorData.doctorimage, doctor = true)

        timer.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        timer.cancel()
    }

    override fun onClick(clickedView: View) {
        when (clickedView.id) {
            R.id.imgBack -> {
                findNavController().popBackStack()
            }
            R.id.tvTryAgain -> {
                onEndCallManageUI(true)
            }
            R.id.btnCallAnotherDoctor -> {
                cancelToken()
            }
        }
    }

    private fun openWaitingRoom(doctorData: ConsultationOnlineDoctorResponse?) {
        if (doctorData != null) {
            findNavController().navigate(
                CallingFragmentDirections.actionWaitingRoom(
                    doctorData
                )
            )
        }
    }

    private fun cancelToken() {
        masterViewModel.cancelDeleteToken(
            CancelTokenRequest(
                doctorId = doctorData.doctor_id,
                patientInfoId = args.doctorData?.patientInfoId ?: 0,
                consultationId = App.consultationId
            )
        ).observeOnce(viewLifecycleOwner) {
            requireActivity().showToast(it.message)
            if (it.success) {
                binding?.imgBack?.performClick()
            }
        }
    }

    override fun generatedUserId() {
        Log.e("generatedUserId", "generatedUserId")
        App.signalR?.callRequest(
            SignalR.SenderReceiverModel(
                fromId = PrefUtils.getChoData(requireActivity())?.memberId.toString(),
                fromType = PrefUtils.getLoginUserType(requireActivity()),
                toId = doctorData.doctor_id.toString(),
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
        Log.e("", "")
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
            .putExtra(Constants.TO_DOCTOR_NAME, args.doctorData?.model?.doctor_Name)
            .putExtra(Constants.DOCTOR_PROFILE_PIC, args.doctorData?.model?.doctorimage)
            .putExtra(Constants.TO_DOCTOR_ID, args.doctorData?.model?.doctor_id)
            .putExtra(Constants.CONSULTATION_ID, App.consultationId)
        startActivity(intent)
    }

    override fun endCall() {
        requireActivity().runOnUiThread {
            onEndCallManageUI(false)
        }
    }

    private fun onEndCallManageUI(isDeny: Boolean) {
        requireActivity().runOnUiThread {
            masterViewModel.changePatientQueue(
                ChangeQueueRequest(
                    patientInfoId = args.doctorData?.patientInfoId ?: 0,
                    doctorId = args.doctorData?.model?.doctor_id ?: 0
                )
            )
                .observeOnce(viewLifecycleOwner) { queueData ->
                    if (queueData.success) {
                        if (isDeny) {
                            openWaitingRoom(args.doctorData)
                        }
                    }
                }
            binding?.layCalling?.visibility = View.GONE
            binding?.layCallDenyByDoctor?.visibility = View.VISIBLE
            timer.cancel()
        }
    }

    override fun syncReceived(message: SignalR.SenderReceiverModel) {
    }

    override fun endCallFromDoctor(message: SignalR.ChatSenderReceiverModel) {

    }

}