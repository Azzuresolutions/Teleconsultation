package com.volumetree.newswasthyaingitopd.fragments.doctor

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.adapters.PrescriptionListAdapter
import com.volumetree.newswasthyaingitopd.application.App
import com.volumetree.newswasthyaingitopd.databinding.FragmentDoctorCallPrescriptionBinding
import com.volumetree.newswasthyaingitopd.enums.UserTypes
import com.volumetree.newswasthyaingitopd.fragments.common.AllergyFragment
import com.volumetree.newswasthyaingitopd.interfaces.CreateCaseListener
import com.volumetree.newswasthyaingitopd.model.requestData.cho.*
import com.volumetree.newswasthyaingitopd.model.responseData.cho.DraftConsultationResponse
import com.volumetree.newswasthyaingitopd.model.responseData.comman.AllergyData
import com.volumetree.newswasthyaingitopd.utils.*
import com.volumetree.newswasthyaingitopd.viewmodel.DoctorViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DoctorCallPrescription(
    val caseData: DraftConsultationResponse,
    private val toUserId: String
) :
    Fragment(),
    View.OnClickListener, CreateCaseListener {

    private var _binding: FragmentDoctorCallPrescriptionBinding? = null
    private val binding get() = _binding!!

    private val selectedMedicineList = ArrayList<LstConsultationMedicineModel>()
    private var lastSelectedMedicine: LstConsultationMedicineModel? = null
    private var lastSelectedDiagnosis: LstConsultationDiagnosisModel? = null

    private var medicineAdapter: PrescriptionListAdapter? = null
    private val doctorViewModel: DoctorViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoctorCallPrescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.showBottomNavigationView(false)


        if (toUserId.isEmpty()) {
            binding.btnSync.visibility = View.GONE
        }
        binding.etProvisionalDiagnosis.setOnClickListener(this)
        binding.btnAddPrescriptionToList.setOnClickListener(this)
        binding.etMedicine.setOnClickListener(this)
        binding.etFrequency.setOnClickListener(this)
        binding.etNoOfDays.setOnClickListener(this)
        binding.etHowLong.setOnClickListener(this)
        binding.etDose.setOnClickListener(this)
        binding.etDoseType.setOnClickListener(this)
        binding.btnSend.setOnClickListener(this)
        binding.btnSync.setOnClickListener(this)
        binding.btnWronglyAddress.setOnClickListener(this)
        binding.btnInadequate.setOnClickListener(this)
        binding.btnCovid19.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(clickedView: View) {
        when (clickedView.id) {
            R.id.btnWronglyAddress -> {
                binding.btnWronglyAddress.isSelected = if (binding.btnWronglyAddress.isSelected) {
                    binding.btnWronglyAddress.setTextColor(
                        ContextCompat.getColor(
                            requireActivity(),
                            R.color.edittext_hint
                        )
                    )
                    false
                } else {
                    binding.btnWronglyAddress.setTextColor(
                        ContextCompat.getColor(
                            requireActivity(),
                            R.color.white
                        )
                    )
                    true
                }
            }
            R.id.btnInadequate -> {
                binding.btnInadequate.isSelected = if (binding.btnInadequate.isSelected) {
                    binding.btnInadequate.setTextColor(
                        ContextCompat.getColor(
                            requireActivity(),
                            R.color.edittext_hint
                        )
                    )
                    false
                } else {
                    binding.btnInadequate.setTextColor(
                        ContextCompat.getColor(
                            requireActivity(),
                            R.color.white
                        )
                    )
                    true
                }
            }
            R.id.btnCovid19 -> {
                binding.btnCovid19.isSelected = if (binding.btnCovid19.isSelected) {
                    binding.btnCovid19.setTextColor(
                        ContextCompat.getColor(
                            requireActivity(),
                            R.color.edittext_hint
                        )
                    )
                    false
                } else {
                    binding.btnCovid19.setTextColor(
                        ContextCompat.getColor(
                            requireActivity(),
                            R.color.white
                        )
                    )
                    true
                }
            }
            R.id.etMedicine -> {
                AllergyFragment(3, this).show(parentFragmentManager, Constants.MEDICINE)
            }
            R.id.etProvisionalDiagnosis -> {
                AllergyFragment(4, this).show(parentFragmentManager, Constants.DIAGNOSIS)
            }
            R.id.layPrescriptionTitle -> {
                if (binding.layPrescriptionDetails.visibility == View.VISIBLE) {
                    binding.layPrescriptionDetails.visibility = View.GONE
                } else {
                    binding.layPrescriptionDetails.visibility = View.VISIBLE
                }
            }
            R.id.etHowLong -> {
                requireActivity().showStringListDialog(Constants.doseDurationType, ::onHowLong)
            }
            R.id.etDose -> {
                requireActivity().showStringListDialog(Constants.doseQuantity, ::onDoseQuantity)

            }
            R.id.etDoseType -> {
                requireActivity().showStringListDialog(Constants.doseType, ::onDoseType)

            }

            R.id.etFrequency -> {
                requireActivity().showStringListDialog(Constants.medicineFrequency, ::onFrequency)
            }
            R.id.etNoOfDays -> {
                requireActivity().showStringListDialog(Constants.doseNoOfDay, ::onNoOfDays)

            }
            R.id.imgBack -> {
                findNavController().popBackStack()
            }
            R.id.btnSend -> {
                if (checkPrescriptionValidation()) {
                    submitPrescription(createPrescriptionRequest(true), true)
                }
            }
            R.id.btnSync -> {
                if (checkPrescriptionValidation()) {
                    submitPrescription(createPrescriptionRequest(false), false)
                }
            }
            R.id.btnAddPrescriptionToList -> {
                if (checkMedicineValidation()) {
                    showPrescriptionList()
                }
            }
        }
    }

    private fun submitPrescription(
        createPrescriptionRequest: InsertConsultationRequest,
        isSend: Boolean
    ) {
        doctorViewModel.insertResponseConsultation(createPrescriptionRequest)
            .observeOnce(viewLifecycleOwner) {
                requireActivity().showToast(it.message)
                if (it.success) {
                    if (toUserId.isEmpty()) {
                        requireActivity().onBackPressed()
                    } else {
                        App.signalR?.syncFromDoctor(
                            SignalR.SenderReceiverModel(
                                senderId = PrefUtils.getDoctorData(requireActivity())!!.memberId.toString(),
                                receiverId = toUserId,
                                message = "SyncFromDoctor",
                                fromId = PrefUtils.getDoctorData(requireActivity())!!.memberId.toString(),
                                toId = toUserId,
                                type = 1,
                                fromType = UserTypes.DOCTOR.type,
                                toType = UserTypes.CHO.type,
                                consultationId = caseData.model.consultationModel.consultationId
                            )
                        )
                        if (isSend) {
                            App.signalR?.endCallFromCHOOrDoctor(
                                "EndCallFromDoctor",
                                SignalR.SenderReceiverModel(
                                    fromId = PrefUtils.getDoctorData(requireActivity())?.memberId.toString(),
                                    fromType = PrefUtils.getLoginUserType(requireActivity()),
                                    toId = toUserId,
                                    toType = UserTypes.CHO.type,
                                    message = "EndCallFromDoctor",
                                    type = 1,
                                    senderId = PrefUtils.getDoctorData(requireActivity())?.memberId.toString(),
                                    receiverId = toUserId,
                                    consultationId = caseData.model.consultationModel.consultationId
                                )
                            )
                            requireActivity().onBackPressed()
                        }
                    }
                }
            }
    }

    private fun checkPrescriptionValidation(): Boolean {
        return if (binding.etGeneralAdvice.getTextFromEt().isEmpty()) {
            requireActivity().showToast("Please add general advice")
            false
        } else {
            true
        }
    }

    private fun createPrescriptionRequest(isSend: Boolean): InsertConsultationRequest {
        val lstConsultationMedicineModel = ArrayList<LstConsultationMedicineModel>()
        val lstConsultationDiagnosisModel = ArrayList<LstConsultationDiagnosisModel>()
        val consultationModel = caseData.model.consultationModel

        consultationModel.isWronglySent = binding.btnWronglyAddress.isSelected
        consultationModel.isInadequate = binding.btnInadequate.isSelected
        consultationModel.isCovid = binding.btnCovid19.isSelected
        consultationModel.additionalDiagnosis = binding.etAdditionalDiagnosis.getTextFromEt()
        consultationModel.additionalMedicine = binding.etAdditionalMedicine.getTextFromEt()
        consultationModel.consultationId = caseData.model.consultationModel.consultationId
        consultationModel.patientInfoId = caseData.model.consultationModel.patientInfoId
        consultationModel.patientName = caseData.model.consultationModel.patientName
        consultationModel.requestFrom = caseData.model.consultationModel.requestFrom
        consultationModel.requestTo = caseData.model.consultationModel.requestTo
        consultationModel.advice = binding.etGeneralAdvice.getTextFromEt()
        if (isSend) {
            consultationModel.statusId = 3
        }
        selectedMedicineList.forEach { medicineData ->
            lstConsultationMedicineModel.add(
                LstConsultationMedicineModel(
                    dosageType = medicineData.dosageType,
                    durationtype = medicineData.durationtype,
                    durationvalue = medicineData.durationvalue,
                    frequency = medicineData.frequency,
                    quant = medicineData.quant,
                    rxId = medicineData.rxId,
                    rxName = medicineData.rxName
                )
            )
        }

        val lstConsultationMessageModel = LstConsultationMessageModel()
        lstConsultationMessageModel.message = binding.etGeneralAdvice.getTextFromEt()
        lstConsultationMessageModel.provisionalDiagnosis =
            binding.etProvisionalDiagnosis.text.toString()
        lstConsultationMessageModel.consultationMessageId =
            caseData.model.consultationModel.consultationId
        lstConsultationMessageModel.requestTo = caseData.model.consultationModel.requestTo
        lstConsultationMessageModel.consultationId =
            caseData.model.consultationModel.consultationId

        if (lastSelectedDiagnosis != null) {
            lstConsultationDiagnosisModel.add(
                LstConsultationDiagnosisModel(
                    name = lastSelectedDiagnosis!!.name,
                    code = lastSelectedDiagnosis!!.code
                )
            )
            caseData.model.lstConsultationDiagnosisModel = lstConsultationDiagnosisModel
        }
        caseData.model.lstConsultationMessageModel.add(lstConsultationMessageModel)
        caseData.model.lstConsultationMedicineModel = lstConsultationMedicineModel
        return InsertConsultationRequest(
            lstConsultationMedicineModel = lstConsultationMedicineModel,
            lstConsultationDiagnosisModel = lstConsultationDiagnosisModel,
            consultationMessageModel = lstConsultationMessageModel,
            consultationModel = consultationModel
        )
    }

    private fun onNoOfDays(noOfDay: String, dialog: Dialog?) {
        dialog?.dismiss()
        binding.etNoOfDays.text = noOfDay
    }

    private fun onFrequency(frequency: String, dialog: Dialog?) {
        dialog?.dismiss()
        binding.etFrequency.text = frequency
    }

    private fun onDoseType(doseType: String, dialog: Dialog?) {
        dialog?.dismiss()
        binding.etDoseType.text = doseType
    }

    private fun onDoseQuantity(doseQuantity: String, dialog: Dialog?) {
        dialog?.dismiss()
        binding.etDose.text = doseQuantity
    }

    private fun onHowLong(howLong: String, dialog: Dialog?) {
        dialog?.dismiss()
        binding.etHowLong.text = howLong
    }

    private fun checkMedicineValidation(): Boolean {
        return if (lastSelectedMedicine == null) {
            requireActivity().showToast(getString(R.string.please_add_medicine))
            false
        } else if (binding.etHowLong.text.isEmpty()) {
            requireActivity().showToast(getString(R.string.please_select_how_long))
            false
        } else if (binding.etNoOfDays.text.isEmpty()) {
            requireActivity().showToast(getString(R.string.please_select_no_days))
            false
        } else if (binding.etFrequency.text.isEmpty()) {
            requireActivity().showToast(getString(R.string.please_select_frequency))
            false
        } else if (binding.etDoseType.text.isEmpty()) {
            requireActivity().showToast(getString(R.string.please_select_dose_type))
            false
        } else if (binding.etDose.text.isEmpty()) {
            requireActivity().showToast(getString(R.string.please_select_dose))
            false
        } else {
            true
        }
    }

    private fun showPrescriptionList() {
        selectedMedicineList.add(
            LstConsultationMedicineModel(
                durationtype = binding.etHowLong.text.toString(),
                durationvalue = binding.etNoOfDays.text.toString(),
                frequency = binding.etFrequency.text.toString(),
                dosageType = binding.etDoseType.text.toString(),
                quant = binding.etDose.text.toString(),
                rxId = lastSelectedMedicine?.rxId ?: "",
                rxName = lastSelectedMedicine?.rxName ?: ""
            )
        )

        if (medicineAdapter != null) {
            medicineAdapter!!.notifyDataSetChanged()
        } else {
            binding.prescriptionTitle.visibility = View.VISIBLE
            binding.rvPrescription.visibility = View.VISIBLE
            binding.rvPrescription.setVerticalLayoutManager(requireActivity())
            medicineAdapter = PrescriptionListAdapter(selectedMedicineList, ::onDeleteMedicine)
            binding.rvPrescription.adapter = medicineAdapter
        }

        binding.etHowLong.text = ""
        binding.etNoOfDays.text = ""
        binding.etFrequency.text = ""
        binding.etDoseType.text = ""
        binding.etDose.text = ""
        binding.etMedicine.text = ""
        lastSelectedMedicine = null
    }

    private fun onDeleteMedicine(positionToRemove: Int) {
        selectedMedicineList.removeAt(positionToRemove)
        if (selectedMedicineList.isEmpty()) {
            binding.prescriptionTitle.visibility = View.GONE
            binding.rvPrescription.visibility = View.GONE
        } else {
            binding.prescriptionTitle.visibility = View.VISIBLE
            binding.rvPrescription.visibility = View.VISIBLE
        }
    }

    override fun onAllergySelected(allergyData: AllergyData?) {
    }

    override fun onProblemSelected(allergyData: AllergyData?) {
    }

    override fun onMedicineSelected(allergyData: AllergyData?) {
        parentFragmentManager.dismissFragmentDialog(Constants.MEDICINE)
        if (allergyData == null) {
            binding.etMedicine.text = ""
        } else {
            lastSelectedMedicine =
                LstConsultationMedicineModel(rxName = allergyData.term, rxId = allergyData.id)
            binding.etMedicine.text = lastSelectedMedicine!!.rxName
        }
    }

    override fun onAllergyUpdated(allergies: ArrayList<LstConsultationAllergyModel>) {
    }

    override fun onVitalUpdate(vitals: ArrayList<LstConsultationTestResultsModel>) {
    }

    override fun onDiagnosisSelected(diagnosis: AllergyData?) {
        parentFragmentManager.dismissFragmentDialog(Constants.DIAGNOSIS)
        if (diagnosis != null) {
            lastSelectedDiagnosis =
                LstConsultationDiagnosisModel(
                    name = diagnosis.term,
                    code = diagnosis.id
                )
            binding.etProvisionalDiagnosis.text = diagnosis.term
        }
    }


}