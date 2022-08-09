package com.volumetree.newswasthyaingitopd.fragments.cho

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.application.App
import com.volumetree.newswasthyaingitopd.databinding.FragmentSelectDrChoBinding
import com.volumetree.newswasthyaingitopd.fragments.patient.SpecializationFragment
import com.volumetree.newswasthyaingitopd.fragments.patient.SpecializedDoctorFragment
import com.volumetree.newswasthyaingitopd.interfaces.SelectDoctorAndSpecializationListener
import com.volumetree.newswasthyaingitopd.model.responseData.cho.ConsultationOnlineDoctorResponse
import com.volumetree.newswasthyaingitopd.model.responseData.patient.DoctorSpecializationData
import com.volumetree.newswasthyaingitopd.model.responseData.patient.SpecialityData
import com.volumetree.newswasthyaingitopd.utils.*
import com.volumetree.newswasthyaingitopd.viewmodel.MasterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectDoctorFragment : Fragment(), View.OnClickListener,
    SelectDoctorAndSpecializationListener {
    private var _binding: FragmentSelectDrChoBinding? = null
    private val binding get() = _binding!!
    private var selectedSpeciality: SpecialityData? = null
    private var selectSpecializedDr: DoctorSpecializationData? = null
    private var lastSelected = 3

    private val args: SelectDoctorFragmentArgs by navArgs()
    private val masterViewModel: MasterViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectDrChoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.showBottomNavigationView(false)
        sharedElementEnterTransition =
            TransitionInflater.from(this.context).inflateTransition(R.transition.change_bounds)

        binding.layActionBar.tvActionbarTitle.text = getString(R.string.find_me_right_doctor)
        binding.btnSpecialOPD.setOnClickListener(this)
        binding.btnGeneralOPD.setOnClickListener(this)
        binding.etChooseSpeciality.setOnClickListener(this)
        binding.etSelectDoctor.setOnClickListener(this)
        binding.btnConsultNow.setOnClickListener(this)
        binding.layActionBar.imgBack.setOnClickListener(this)

        binding.btnGeneralOPD.performClick()
        manageOldData()

    }

    private fun manageOldData() {
        if (lastSelected == 1) {
            binding.btnGeneralOPD.performClick()
        } else if (lastSelected == 2) {
            binding.btnSpecialOPD.performClick()
        }

        if (selectedSpeciality != null) {
            binding.etChooseSpeciality.text = selectedSpeciality!!.specialityName
        }
        if (selectSpecializedDr != null) {
            binding.etSelectDoctor.text = selectSpecializedDr!!.doctor_Name
        }
        if (selectedSpeciality != null) {
            binding.btnConsultNow.enableDisable(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(clickedView: View) {
        when (clickedView.id) {
            R.id.imgBack -> {
                findNavController().popBackStack()
            }
            R.id.btnGeneralOPD -> {
                selectedSpeciality = null
                binding.etChooseSpeciality.text = ""
                binding.tvSelectSpeciality.visibility = View.GONE
                binding.etChooseSpeciality.visibility = View.GONE
                lastSelected = 1
                binding.btnConsultNow.enableDisable(true)
                binding.btnGeneralOPD.isSelected = true
                binding.btnSpecialOPD.isSelected = false
                binding.btnGeneralOPD.setTextColor(
                    ContextCompat.getColor(requireActivity(), R.color.white)

                )
                binding.btnSpecialOPD.setTextColor(
                    ContextCompat.getColor(requireActivity(), R.color.black)
                )
            }
            R.id.btnSpecialOPD -> {
                binding.tvSelectSpeciality.visibility = View.VISIBLE
                binding.etChooseSpeciality.visibility = View.VISIBLE
                lastSelected = 2
                binding.btnConsultNow.enableDisable(false)
                binding.btnGeneralOPD.isSelected = false
                binding.btnSpecialOPD.isSelected = true
                binding.btnGeneralOPD.setTextColor(
                    ContextCompat.getColor(requireActivity(), R.color.black)
                )
                binding.btnSpecialOPD.setTextColor(
                    ContextCompat.getColor(requireActivity(), R.color.white)
                )
            }
            R.id.etChooseSpeciality -> {
                showSpeciality()
            }
            R.id.etSelectDoctor -> {
                showSpecializedDoctor()
            }
            R.id.btnConsultNow -> {
                callTokenRegistrationAPI()
            }
        }
    }

    private fun showSpecializedDoctor() {
        if (lastSelected == 2 && selectedSpeciality == null) {
            requireActivity().showToast(getString(R.string.select_specialization_first))
            return
        }
        SpecializedDoctorFragment(
            selectedSpeciality?.specialityId ?: 0,
            selectedSpeciality?.specialityName ?: "General",
            this
        ).show(parentFragmentManager, Constants.SELECT_DOCTOR)
    }

    private fun showSpeciality() {
        SpecializationFragment(this).show(parentFragmentManager, Constants.SELECT_SPECIALIZATION)
    }

    private fun callTokenRegistrationAPI() {
        masterViewModel.consultationOnlineDoctor(
            consultationId = App.consultationId.toString(),
            patientInfoId = PrefUtils.getChoData(requireActivity())?.memberId.toString(),
            if (selectedSpeciality == null) {
                0
            } else {
                selectedSpeciality!!.specialityId
            },
            doctorId = selectSpecializedDr?.doctor_id ?: "0"
        )
            .observeOnce(viewLifecycleOwner) { draftConsultationResponse ->
                draftConsultationResponse.patientInfoId = args.patientInfoId
                if (draftConsultationResponse.queueNo == 1) {
                    findNavController().navigate(
                        SelectDoctorFragmentDirections.actionCalling(
                            selectedSpeciality?.specialityName ?: "General hub",
                            draftConsultationResponse,
                        )
                    )
                } else if (draftConsultationResponse.queueNo > 0) {
                    CommonDialog.showTokenDialog(
                        requireActivity(),
                        draftConsultationResponse,
                        ::onTokenConfirmation
                    )
                }

            }
    }

    private fun onTokenConfirmation(consultationResponse: ConsultationOnlineDoctorResponse) {
        findNavController().navigate(
            SelectDoctorFragmentDirections.actionWaitingRoom(
                consultationResponse
            )
        )
    }

    override fun onSpecialization(specialityData: SpecialityData?) {
        if (specialityData != null) {
            selectSpecializedDr = null
            binding.etSelectDoctor.text = ""
            binding.tvDoctorStatus.visibility = View.GONE
            selectedSpeciality = specialityData
            binding.etChooseSpeciality.text = selectedSpeciality!!.specialityName
            if (selectedSpeciality != null) {
                binding.btnConsultNow.enableDisable(true)
            }
        }
        parentFragmentManager.dismissFragmentDialog(Constants.SELECT_SPECIALIZATION)
    }

    override fun onSpecialDoctor(doctorSpecializationData: DoctorSpecializationData?) {
        if (doctorSpecializationData != null) {
            selectSpecializedDr = doctorSpecializationData
            binding.etSelectDoctor.text = selectSpecializedDr!!.doctor_Name
            if (selectedSpeciality != null) {
                binding.btnConsultNow.enableDisable(true)
            }
            binding.tvDoctorStatus.visibility = View.VISIBLE
            if (selectSpecializedDr!!.doctor_Status == "online" || selectSpecializedDr!!.doctor_Status == "Online") {
                binding.tvDoctorStatus.text =
                    "Dr. ${selectSpecializedDr!!.doctor_Name} is ${selectSpecializedDr!!.doctor_Status} you can start your consultation directly."
            } else {
                binding.tvDoctorStatus.text =
                    "Dr. ${selectSpecializedDr!!.doctor_Name} is ${selectSpecializedDr!!.doctor_Status} right now, you have to generate token to get consultation."
            }
        }
        parentFragmentManager.dismissFragmentDialog(Constants.SELECT_DOCTOR)

    }


}