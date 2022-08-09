package com.volumetree.newswasthyaingitopd.fragments.cho

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.adapters.CommonStringListAdapter
import com.volumetree.newswasthyaingitopd.adapters.UploadedDocumentListAdapter
import com.volumetree.newswasthyaingitopd.application.App
import com.volumetree.newswasthyaingitopd.databinding.FragmentCreateCaseBinding
import com.volumetree.newswasthyaingitopd.fragments.common.AllergyFragment
import com.volumetree.newswasthyaingitopd.fragments.doctor.AllergyListFragment
import com.volumetree.newswasthyaingitopd.fragments.doctor.VitalListFragment
import com.volumetree.newswasthyaingitopd.fragments.patient.PreviewCase
import com.volumetree.newswasthyaingitopd.interfaces.CreateCaseListener
import com.volumetree.newswasthyaingitopd.interfaces.PicDocumentSelector
import com.volumetree.newswasthyaingitopd.model.requestData.cho.*
import com.volumetree.newswasthyaingitopd.model.responseData.cho.ChoPatientData
import com.volumetree.newswasthyaingitopd.model.responseData.cho.DraftConsultationResponse
import com.volumetree.newswasthyaingitopd.model.responseData.comman.AllergyData
import com.volumetree.newswasthyaingitopd.model.responseData.comman.DurationData
import com.volumetree.newswasthyaingitopd.model.responseData.comman.SeverityData
import com.volumetree.newswasthyaingitopd.model.responseData.comman.VitalData
import com.volumetree.newswasthyaingitopd.model.responseData.patient.SelectedDocData
import com.volumetree.newswasthyaingitopd.utils.*
import com.volumetree.newswasthyaingitopd.viewmodel.ChoViewModel
import com.volumetree.newswasthyaingitopd.viewmodel.MasterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateCaseFragment : Fragment(), View.OnClickListener, CreateCaseListener,
    PicDocumentSelector {

    private var _binding: FragmentCreateCaseBinding? = null
    private val binding get() = _binding!!
    private val masterViewModel: MasterViewModel by viewModel()
    private val choViewModel: ChoViewModel by viewModel()
    private val selectedDocuments = ArrayList<SelectedDocData>()
    private var uploadedDocumentListAdapter: UploadedDocumentListAdapter? = null

    private val args: CreateCaseFragmentArgs by navArgs()

    private var selectedAllergies: ArrayList<LstConsultationAllergyModel> = ArrayList()
    private var selectedProblems: ArrayList<AllergyData> = ArrayList()
    private var selectedVitals: ArrayList<LstConsultationTestResultsModel> = ArrayList()

    private var lastSelectedAllergy: AllergyData? = null
    private var lastDuration: DurationData? = null
    private var lastSeverity: SeverityData? = null
    private var lastSelectedVital: VitalData? = null
    private var lastSelectedProblem: AllergyData? = null
    private var selectedPatientData: ChoPatientData? = null


    private lateinit var consultationModel: ConsultationModel
    private lateinit var patientInfo: PatientConsultationModelResponse
    private lateinit var caseData: DraftConsultationResponse

    companion object {
        var isFromDraft: Boolean = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ViewGroup? {
        _binding = FragmentCreateCaseBinding.inflate(inflater, container, false)
        isFromDraft = false
        return _binding?.root
    }

    private fun setUpDocumentList() {
        binding.rvDocuments.setHorizontalLayoutManager(requireActivity())
        uploadedDocumentListAdapter = UploadedDocumentListAdapter(selectedDocuments, true)
        binding.rvDocuments.adapter = uploadedDocumentListAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.showBottomNavigationView(false)
        binding.layToolbar.tvActionbarTitle.text = getString(R.string.create_a_case)
        binding.layToolbar.laySaveDraft.visibility = View.VISIBLE
        binding.layToolbar.imgBack.setOnClickListener(this)
        setUpDocumentList()
        binding.layAllergyTitle.setOnClickListener(this)
        binding.layGeneralExaminationTitle.setOnClickListener(this)
        binding.layPrescriptionTitle.setOnClickListener(this)
        binding.layProblemTitle.setOnClickListener(this)
        binding.layDiagnosticsTitle.setOnClickListener(this)
        binding.layMedicalRecordTitle.setOnClickListener(this)
        binding.layUploadImages.setOnClickListener(this)
        binding.layToolbar.laySaveDraft.setOnClickListener(this)

        binding.etDuration.setOnClickListener(this)
        binding.etSeverity.setOnClickListener(this)
        binding.etProblem.setOnClickListener(this)
        binding.tvDiagnosticsValue.setOnClickListener(this)
        binding.etFrequency.setOnClickListener(this)
        binding.etEnterAllergy.setOnClickListener(this)
        binding.btnDiabetes.setOnClickListener(this)
        binding.btnAlcoholIntake.setOnClickListener(this)
        binding.btnHypertension.setOnClickListener(this)
        binding.btnSmoking.setOnClickListener(this)
        binding.btnYes.setOnClickListener(this)
        binding.btnNo.setOnClickListener(this)
        binding.btnMaybe.setOnClickListener(this)
        binding.btnAddToList.setOnClickListener(this)
        binding.btnAddVitalToList.setOnClickListener(this)
        binding.btnAddMember.setOnClickListener(this)
        binding.tvPatientName.setOnClickListener(this)
        binding.btnViewAddedAllergy.setOnClickListener(this)
        binding.btnPreview.setOnClickListener(this)
        binding.btnViewAddedVital.setOnClickListener(this)
        binding.btnAddPrescriptionToList.setOnClickListener(this)
        binding.btnConsultNow.setOnClickListener(this)
        binding.btnConsultNow.enableDisable(true)
        selectedPatientData = args.patientData ?: return
        setupPatientData()

        if (args.consultationId > 0) {
            binding.layToolbar.tvActionbarTitle.text = getString(R.string.update_a_case)
            binding.layToolbar.laySaveDraft.visibility = View.INVISIBLE
            binding.tvPatientConsultation.visibility = View.GONE
            binding.tvPatientName.visibility = View.GONE
            binding.layOr.visibility = View.GONE
            binding.btnAddMember.visibility = View.GONE
            getConsultationData(args.consultationId)
        }
    }

    private fun getConsultationData(consultationId: Int) {
        choViewModel.getConsultation(consultationId)
            .observeOnce(viewLifecycleOwner) { consultationData ->
                this.caseData = consultationData
                if (this.caseData.model != null) {
                    setupCaseData()
                }
            }
    }


    private fun setupPatientData() {
        val patientData = selectedPatientData ?: return
        val name = "${patientData.firstName} ${patientData.lastName}"
        binding.tvSelectedPatientName.text = name
        binding.tvPatientId.text = patientData.patientInfoId.toString()
        if (patientData.age > 0) {
            binding.tvPatientAgeGender.text =
                "${patientData.age.getAge()}, ${patientData.genderId.getGenderNameFromId()}"
        } else {
            binding.tvPatientAgeGender.text =
                "${patientData.dob.getAge()}, ${patientData.genderId.getGenderNameFromId()}"
        }
        binding.tvMobile.text = patientData.mobile.ifEmpty {
            "N/A"
        }
        binding.tvPatientName.text = name
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(clickedView: View) {
        when (clickedView.id) {
            R.id.tvPatientName -> {
                choViewModel.showFamilyMemberDialog(
                    requireActivity(),
                    args.patientData!!, ::onFamilyMemberSelect
                )
            }
            R.id.btnAddMember -> {
                AddFamilyMemberChoFragment(
                    args.patientData?.patientInfoId ?: 0,
                    null,
                    ::onFamilyMemberUpdate
                ).show(
                    parentFragmentManager,
                    Constants.FAMILY_MEMBER
                )
            }
            R.id.laySaveDraft -> {
                draftConsultation(isDraft = true)
            }
            R.id.btnConsultNow -> {
                draftConsultation()
            }
            R.id.layUploadImages -> {
                if (uploadedDocumentListAdapter!!.docList.size == Constants.MAX_DOCUMENT) {
                    requireActivity().showToast(getString(R.string.more_than_five_document))
                } else {
                    CommonDialog.Companion.ShowImagePicker(
                        true, requireActivity(), this, this, 1,
                        isMultiple = true
                    )
                        .show(parentFragmentManager, Constants.FILE_PICKER)
                }
            }
            R.id.layAllergyTitle -> {
                if (binding.layAllergyDetails.visibility == View.VISIBLE) {
                    binding.layAllergyDetails.visibility = View.GONE
                } else {
                    binding.layAllergyDetails.visibility = View.VISIBLE
                }
                binding.layAllergyDetails.invalidate()
            }
            R.id.layGeneralExaminationTitle -> {
                if (binding.layGeneralExaminationDetails.visibility == View.VISIBLE) {
                    binding.layGeneralExaminationDetails.visibility = View.GONE
                } else {
                    binding.layGeneralExaminationDetails.visibility = View.VISIBLE
                }
            }
            R.id.layPrescriptionTitle -> {
                if (binding.layPrescriptionDetails.visibility == View.VISIBLE) {
                    binding.layPrescriptionDetails.visibility = View.GONE
                } else {
                    binding.layPrescriptionDetails.visibility = View.VISIBLE
                }
            }


            R.id.layProblemTitle -> {
                if (binding.layProblemDetails.visibility == View.VISIBLE) {
                    binding.layProblemDetails.visibility = View.GONE
                } else {
                    binding.layProblemDetails.visibility = View.VISIBLE
                }
            }
            R.id.layDiagnosticsTitle -> {
                if (binding.layDiagnosticsDetails.visibility == View.VISIBLE) {
                    binding.layDiagnosticsDetails.visibility = View.GONE
                } else {
                    binding.layDiagnosticsDetails.visibility = View.VISIBLE
                }
            }
            R.id.layMedicalRecordTitle -> {
                if (binding.layMedicalRecordDetails.visibility == View.VISIBLE) {
                    binding.layMedicalRecordDetails.visibility = View.GONE
                } else {
                    binding.layMedicalRecordDetails.visibility = View.VISIBLE
                }
            }
            R.id.etFrequency -> {
                showFrequencyDialog()
            }
            R.id.btnDiabetes -> {
                binding.btnDiabetes.isSelected = if (binding.btnDiabetes.isSelected) {
                    binding.btnDiabetes.setTextColor(
                        ContextCompat.getColor(
                            requireActivity(),
                            R.color.edittext_hint
                        )
                    )
                    false
                } else {
                    binding.btnDiabetes.setTextColor(
                        ContextCompat.getColor(
                            requireActivity(),
                            R.color.white
                        )
                    )
                    true
                }
            }
            R.id.btnSmoking -> {
                binding.btnSmoking.isSelected = if (binding.btnSmoking.isSelected) {
                    binding.btnSmoking.setTextColor(
                        ContextCompat.getColor(
                            requireActivity(),
                            R.color.edittext_hint
                        )
                    )
                    false
                } else {
                    binding.btnSmoking.setTextColor(
                        ContextCompat.getColor(
                            requireActivity(),
                            R.color.white
                        )
                    )
                    true
                }
            }
            R.id.btnAlcoholIntake -> {
                binding.btnAlcoholIntake.isSelected = if (binding.btnAlcoholIntake.isSelected) {
                    binding.btnAlcoholIntake.setTextColor(
                        ContextCompat.getColor(
                            requireActivity(),
                            R.color.edittext_hint
                        )
                    )
                    false
                } else {
                    binding.btnAlcoholIntake.setTextColor(
                        ContextCompat.getColor(
                            requireActivity(),
                            R.color.white
                        )
                    )
                    true
                }
            }
            R.id.btnHypertension -> {
                binding.btnHypertension.isSelected = if (binding.btnHypertension.isSelected) {
                    binding.btnHypertension.setTextColor(
                        ContextCompat.getColor(
                            requireActivity(),
                            R.color.edittext_hint
                        )
                    )
                    false
                } else {
                    binding.btnHypertension.setTextColor(
                        ContextCompat.getColor(
                            requireActivity(),
                            R.color.white
                        )
                    )
                    true
                }
            }
            R.id.btnYes -> {
                binding.btnYes.selectGender(
                    requireActivity(),
                    binding.btnMaybe,
                    binding.btnNo
                )
            }
            R.id.btnNo -> {
                binding.btnNo.selectGender(
                    requireActivity(),
                    binding.btnYes,
                    binding.btnMaybe
                )
            }
            R.id.btnMaybe -> {
                binding.btnMaybe.selectGender(
                    requireActivity(),
                    binding.btnYes,
                    binding.btnNo
                )
            }
            R.id.imgBack -> {
                findNavController().popBackStack()
            }
            R.id.btnAddVitalToList -> {
                if (checkVitalValidation()) {
                    selectedVitals.add(
                        LstConsultationTestResultsModel(
                            name = lastSelectedVital?.name ?: "",
                            categoryName = lastSelectedVital?.categoryName ?: "",
                            units = lastSelectedVital?.units ?: "",
                            testId = lastSelectedVital?.testId ?: 0,
                            result = binding.etResult.getTextFromEt()
                        )
                    )
                    binding.btnViewAddedVital.visibility = View.VISIBLE

                    lastSelectedVital = null
                    vitalClick(lastSelectedVital, null)
                }
            }
            R.id.btnAddToList -> {
                if (checkAllergyValidation()) {
                    selectedAllergies.add(
                        LstConsultationAllergyModel(
                            name = lastSelectedAllergy?.term ?: "",
                            isStill = if (binding.btnYes.isSelected) {
                                "0"
                            } else if (binding.btnNo.isSelected) {
                                "1"
                            } else if (binding.btnMaybe.isSelected) {
                                "2"
                            } else {
                                "0"
                            },
                            consultationAllergyId = "0",
                            severityTypeId = lastSeverity?.allergySeverityId ?: 0,
                            durationId = lastDuration?.allergyDurationId ?: 0,
                            allergyDuration = lastDuration?.allergyDuration ?: "0",
                            code = lastSelectedAllergy?.id ?: "0",
                            allergySeverityName = lastSeverity?.allergySeverityName ?: ""
                        )
                    )
                    binding.btnViewAddedAllergy.visibility = View.VISIBLE
                    lastSelectedAllergy = null
                    lastSeverity = null
                    lastDuration = null
                    severityClick(lastSeverity, null)
                    durationClick(lastDuration, null)
                    onAllergySelected(lastSelectedAllergy)
                    binding.btnYes.unSelectButton(
                        requireActivity(),
                        binding.btnNo,
                        binding.btnMaybe
                    )
                }
            }

            R.id.btnPreview -> {
                PreviewCase(
                    selectedAllergies,
                    selectedVitals,
                    selectedProblems,
                    uploadedDocumentListAdapter!!.docList,
                    binding.etQueryDesc.getTextFromEt(),
                    binding.etAdditionProblem.getTextFromEt(),
                    binding.etAdditionAlerrgy.getTextFromEt(),
                    binding.etAdditionExamination.getTextFromEt(),
                    binding.btnSmoking.isSelected,
                    binding.btnAlcoholIntake.isSelected,
                    binding.btnHypertension.isSelected,
                    binding.btnDiabetes.isSelected,
                    selectedPatientData,
                    null,
                    null,
                    true
                ).show(
                    parentFragmentManager,
                    Constants.PREVIEW_CASE
                )
            }
            R.id.btnViewAddedAllergy -> {
                AllergyListFragment(selectedAllergies, this).show(
                    parentFragmentManager,
                    Constants.SELECTED_ALLERGIES
                )
            }

            R.id.btnViewAddedVital -> {
                VitalListFragment(selectedVitals, this).show(
                    parentFragmentManager,
                    Constants.SELECTED_VITALS
                )
            }
            R.id.btnAddPrescriptionToList -> {
                showPrescriptionList()
            }

            R.id.etEnterAllergy -> {
                AllergyFragment(1, this).show(parentFragmentManager, Constants.ALLERGY)
            }

            R.id.etDuration -> {
                masterViewModel.showDurationDialog(requireActivity(), ::durationClick)
            }
            R.id.etSeverity -> {
                masterViewModel.showSeverityDialog(requireActivity(), ::severityClick)
            }
            R.id.etProblem -> {
                AllergyFragment(2, this).show(parentFragmentManager, Constants.PROBLEM)
            }
            R.id.tvDiagnosticsValue -> {
                masterViewModel.showVitals(requireActivity(), ::vitalClick)
            }
        }
    }

    private fun onFamilyMemberSelect(familyMemberData: ChoPatientData, dialog: Dialog, type: Int) {
        dialog.dismiss()
        if (type == 1) {
            selectedPatientData = familyMemberData
            setupPatientData()
        } else if (type == 2) {
            AddFamilyMemberChoFragment(
                selectedPatientData?.patientInfoId ?: 0,
                familyMemberData, ::onFamilyMemberUpdate
            ).show(
                parentFragmentManager,
                Constants.FAMILY_MEMBER
            )
        }
    }

    private fun onFamilyMemberUpdate(addedOrUpdateStatus: Int, choPatientData: ChoPatientData?) {
        if (choPatientData != null) {
            if (addedOrUpdateStatus == 2 && selectedPatientData?.patientInfoId != choPatientData.patientInfoId) {
                return
            }
            selectedPatientData = choPatientData
            setupPatientData()
        }
        parentFragmentManager.dismissFragmentDialog(Constants.FAMILY_MEMBER)
    }

    private fun checkVitalValidation(): Boolean {
        return if (lastSelectedVital == null) {
            requireActivity().showToast("Please select vitals")
            false
        } else if (binding.etResult.getTextFromEt() == "") {
            requireActivity().showToast("Please enter vital result")
            false
        } else {
            if (selectedVitals.any { it.testId == lastSelectedVital!!.testId }) {
                requireActivity().showToast(getString(R.string.test_allready_added))
                false
            } else {
                true
            }
        }
    }

    private fun checkAllergyValidation(): Boolean {
        return if (lastSelectedAllergy == null) {
            requireActivity().showToast("Please select allergy")
            false
        } else if (lastDuration == null) {
            requireActivity().showToast("Please select duration")
            false
        } else if (lastSeverity == null) {
            requireActivity().showToast("Please select severity")
            false
        } else if (!binding.btnYes.isSelected && !binding.btnNo.isSelected && !binding.btnMaybe.isSelected) {
            requireActivity().showToast("Please select still have allergy")
            false
        } else {
            true
        }
    }

    private fun draftConsultation(isDraft: Boolean = false) {
        if (checkValidation()) {
            choViewModel.draftConsultation(getCreateCaseObject())
                .observeOnce(viewLifecycleOwner) { draftConsultationResponse ->
                    if (isDraft) {
                        showDraftMessageDialog()
                    } else {
                        App.consultationId =
                            draftConsultationResponse.model?.consultationModel?.consultationId ?: 0
                        findNavController().navigate(
                            CreateCaseFragmentDirections.actionSelectDr(
                                draftConsultationResponse.doctorID.toString(),selectedPatientData?.patientInfoId?:0
                            )
                        )
                    }
                }
        }
    }

    private fun showDraftMessageDialog() {
        CommonDialog.showDraftRecordSuccess(
            requireActivity(),
            ::okDraft,
        )
    }

    private fun okDraft() {
        isFromDraft = true
        findNavController().popBackStack()
    }

    private fun checkValidation(): Boolean {
        val isValid = when {
            binding.etQueryDesc.getTextFromEt().isEmpty() -> {
                requireActivity().showToast(getString(R.string.please_enter_query))
                false
            }

            else -> {
                true
            }
        }
        return isValid
    }

    private fun getCreateCaseObject(): DraftConsultationRequest {
        val lstConsultationMedicineModel = ArrayList<LstConsultationMedicineModel>()
        val lstConsultationMessageModel = ArrayList<LstConsultationMessageModel>()


        val lstConsultationImagesModel = ArrayList<LstConsultationImagesModel>()
        uploadedDocumentListAdapter?.docList?.forEach { selectedDocData ->
            val lstConsultationImages = LstConsultationImagesModel()
            lstConsultationImages.fileFlag = selectedDocData.fileFlag
            lstConsultationImages.fileName = selectedDocData.imgName
            lstConsultationImages.filePath = selectedDocData.base64
            lstConsultationImagesModel.add(lstConsultationImages)
        }

        val patientData = selectedPatientData ?: return DraftConsultationRequest()

        val consultationModel = ConsultationModel()
        if (args.consultationId > 0) {
            consultationModel.consultationId = args.consultationId
        }
        consultationModel.patientInfoId = patientData.patientInfoId.toString()
        consultationModel.patientName = "${patientData.firstName} ${patientData.lastName}"
        consultationModel.gender = patientData.genderId.getGenderNameFromId()
        consultationModel.PatientDOB = patientData.dob
        consultationModel.mobileNumber = patientData.mobile
        consultationModel.StateId = patientData.stateId.toString()
        consultationModel.patientAddress = patientData.addressLine1
        consultationModel.CRNumber = ""
        consultationModel.physicalExamination = ""
        consultationModel.systemicExamination = ""
        consultationModel.additionalMedicine = ""
        consultationModel.isDiabetic = binding.btnDiabetes.isSelected
        consultationModel.isAlcoholic = binding.btnAlcoholIntake.isSelected
        consultationModel.isSmoker = binding.btnSmoking.isSelected
        consultationModel.isHypertension = binding.btnHypertension.isSelected
        consultationModel.additionalAllergy = binding.etAdditionAlerrgy.getTextFromEt()
        consultationModel.additionalProblem = binding.etAdditionProblem.getTextFromEt()
        consultationModel.dob = patientData.dob
        consultationModel.gerneralExamination = binding.etAdditionExamination.getTextFromEt()
        consultationModel.genderId = patientData.genderId.toString()
        consultationModel.queryDesc = binding.etQueryDesc.getTextFromEt()

        val patientConsultationModel = PatientConsultationModel()
        patientConsultationModel.patientInfoId = patientData.patientInfoId.toString()
        patientConsultationModel.patientFirstName = patientData.firstName
        patientConsultationModel.patientLastName = patientData.lastName
        patientConsultationModel.patientGenderId = patientData.genderId.toString()
        patientConsultationModel.PatientDOB = patientData.dob
        patientConsultationModel.patientMobile = patientData.dob
        patientConsultationModel.StateId = patientData.stateId.toString()
        patientConsultationModel.patientAddress = patientData.addressLine1

        val lstConsultationAllergyModel = ArrayList<LstConsultationAllergyModel>()
        selectedAllergies.forEach { allergyData ->
            lstConsultationAllergyModel.add(
                LstConsultationAllergyModel(
                    name = allergyData.name,
                    isStill = allergyData.isStill,
                    consultationAllergyId = allergyData.consultationAllergyId,
                    severityTypeId = allergyData.severityTypeId,
                    durationId = allergyData.durationId,
                    allergyDuration = allergyData.allergyDuration,
                    code = allergyData.code,
                    allergySeverityName = allergyData.allergySeverityName
                )
            )
        }

        val lstConsultationProblemsModel = ArrayList<LstConsultationProblemsModel>()
        selectedProblems.forEach { problemData ->
            lstConsultationProblemsModel.add(
                LstConsultationProblemsModel(
                    name = problemData.term,
                    consultationProblemId = "0",
                    code = problemData.id
                )
            )
        }

        val lstConsultationTestResultsModel = ArrayList<LstConsultationTestResultsModel>()
        selectedVitals.forEach { vitalData ->
            lstConsultationTestResultsModel.add(
                LstConsultationTestResultsModel(
                    name = vitalData.name,
                    units = vitalData.units,
                    testId = vitalData.testId,
                    result = binding.etResult.getTextFromEt()
                )
            )
        }

        val messageModel = LstConsultationMessageModel()
        messageModel.message = binding.etQueryDesc.getTextFromEt()
        messageModel.provisionalDiagnosis = ""
        messageModel.requestTo = 0
        messageModel.consultationId = 0
        messageModel.consultationMessageId = 0
        lstConsultationMessageModel.add(messageModel)

        return DraftConsultationRequest(
            consultationModel = consultationModel,
            patientConsultationModel = patientConsultationModel,
            lstConsultationAllergyModel = lstConsultationAllergyModel,
            lstConsultationImagesModel = lstConsultationImagesModel,
            lstConsultationProblemsModel = lstConsultationProblemsModel,
            lstConsultationMedicineModel = lstConsultationMedicineModel,
            lstConsultationMessageModel = lstConsultationMessageModel,
            lstConsultationTestResultsModel = lstConsultationTestResultsModel
        )
    }

    private fun vitalClick(vitalData: VitalData?, dialog: Dialog?) {
        if (vitalData == null) {
            binding.tvDiagnosticsValue.text = ""
            binding.tvResultUnit.text = ""
            binding.etResult.setText("")
        } else {
            lastSelectedVital = vitalData
            binding.tvDiagnosticsValue.text = lastSelectedVital!!.categoryName
            binding.tvResultUnit.text = lastSelectedVital!!.units
            dialog?.dismiss()
        }
    }

    private fun severityClick(severityData: SeverityData?, dialog: Dialog?) {
        if (severityData == null) {
            binding.etSeverity.text = ""
        } else {
            lastSeverity = severityData
            binding.etSeverity.text = lastSeverity!!.allergySeverityName
            dialog?.dismiss()
        }

    }

    private fun durationClick(durationData: DurationData?, dialog: Dialog?) {
        if (durationData == null) {
            binding.etDuration.text = ""
        } else {
            lastDuration = durationData
            binding.etDuration.text = lastDuration!!.allergyDuration
            dialog?.dismiss()
        }
    }

    private fun showPrescriptionList() {

//        binding.prescriptionTitle.visibility = View.VISIBLE
//        binding.rvPrescription.visibility = View.VISIBLE
//        binding.rvPrescription.setVerticalLayoutManager(requireActivity())
//        val hospitalName = ArrayList<String>()
//        hospitalName.add("QID")
//        hospitalName.add("SOS")
//        hospitalName.add("RDS")
//        binding.rvPrescription.adapter = PrescriptionListAdapter(hospitalName)
    }

    private lateinit var frequencyDialog: Dialog
    private fun showFrequencyDialog() {
        frequencyDialog = Dialog(requireActivity())
        frequencyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        frequencyDialog.window?.setBackgroundDrawableResource(R.color.transparent)
        frequencyDialog.setContentView(R.layout.layout_list_dialog)
        val rvDialogList =
            frequencyDialog.findViewById<RecyclerView>(R.id.rvDialogList)

        frequencyDialog.setCancelable(true)
        frequencyDialog.setCanceledOnTouchOutside(true)
        frequencyDialog.show()
        rvDialogList.setVerticalLayoutManager(requireActivity())
        val hospitalName = ArrayList<String>()
        hospitalName.add("BD")
        hospitalName.add("HS")
        hospitalName.add("OS")
        hospitalName.add("QID")
        hospitalName.add("SOS")
        hospitalName.add("RDS")
        rvDialogList.adapter = CommonStringListAdapter(hospitalName, ::frequencyClick)
    }

    private fun frequencyClick(frequencyName: String) {
        binding.etFrequency.text = frequencyName
        frequencyDialog.dismiss()
    }


    override fun onProfilePicSelected(selectedDocData: SelectedDocData, type: Int) {
        if (selectedDocuments.size < Constants.MAX_DOCUMENT) {
            selectedDocuments.add(selectedDocData)
            uploadedDocumentListAdapter?.notifyItemInserted(selectedDocuments.size - 1)
        }
        parentFragmentManager.dismissFragmentDialog(Constants.FILE_PICKER)

    }

    override fun onAllergySelected(allergyData: AllergyData?) {
        parentFragmentManager.dismissFragmentDialog(Constants.ALLERGY)
        if (allergyData == null) {
            binding.etEnterAllergy.text = ""
        } else {
            lastSelectedAllergy = allergyData
            binding.etEnterAllergy.text = lastSelectedAllergy!!.term
        }
    }

    override fun onProblemSelected(allergyData: AllergyData?) {
        parentFragmentManager.dismissFragmentDialog(Constants.PROBLEM)
        if (allergyData != null) {
            lastSelectedProblem = allergyData
            binding.etProblem.text = lastSelectedProblem!!.term
            selectedProblems.add(allergyData)
            addTag()
        }
    }

    override fun onAllergyUpdated(allergies: ArrayList<LstConsultationAllergyModel>) {
        parentFragmentManager.dismissFragmentDialog(Constants.SELECTED_ALLERGIES)
        selectedAllergies = allergies
        if (selectedAllergies.isEmpty()) {
            binding.btnViewAddedAllergy.visibility = View.GONE
        } else if (selectedAllergies.isNotEmpty()) {
            binding.btnViewAddedAllergy.visibility = View.VISIBLE
        }
    }

    override fun onVitalUpdate(vitals: ArrayList<LstConsultationTestResultsModel>) {
        parentFragmentManager.dismissFragmentDialog(Constants.SELECTED_VITALS)
        selectedVitals = vitals
        if (selectedVitals.isEmpty()) {
            binding.btnViewAddedVital.visibility = View.GONE
        } else if (selectedVitals.isNotEmpty()) {
            binding.btnViewAddedVital.visibility = View.VISIBLE
        }
    }

    override fun onDiagnosisSelected(diagnosis: AllergyData?) {
    }

    override fun onMedicineSelected(allergyData: AllergyData?) {
    }

    private fun addTag() {
        binding.tagViewProblems.removeAllViews()

        selectedProblems.forEach { problemData ->
            val layoutInflater = LayoutInflater.from(requireActivity())
            val chip = layoutInflater.inflate(
                R.layout.chip_layout,
                binding.tagViewProblems,
                false
            ) as Chip

            chip.text = problemData.term
            chip.tag = problemData.id

            chip.setOnCloseIconClickListener { view ->
                binding.tagViewProblems.removeView(view)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    selectedProblems.removeIf { it.id == view.tag.toString() }
                }
            }
            chip.setChipBackgroundColorResource(R.color.opt_button_active)
            binding.tagViewProblems.addView(chip)

        }
    }

    private fun setupCaseData() {
        setupPatientDataForDraftConsultation()
        setupExaminationData()
        setupAllergy()
        setupProblem()
        setupUploadedDoc()
        setupVitals()
        binding.etQueryDesc.setText(consultationModel.queryDesc)
    }

    private fun setupPatientDataForDraftConsultation() {
        binding.btnDiabetes.isSelected = false
        binding.btnSmoking.isSelected = false
        binding.btnHypertension.isSelected = false
        binding.btnAddToList.isSelected = false
        consultationModel = this.caseData.model?.consultationModel!!
        patientInfo = this.caseData.model?.patientConsultationModel!!
        val name = consultationModel.patientName
        binding.tvSelectedPatientName.text = name
        binding.tvPatientId.text = consultationModel.patientInfoId
        binding.tvPatientAgeGender.text =
            "${patientInfo.patientAge.getAge()}, ${
                consultationModel.genderId.toInt().getGenderNameFromId()
            }"
        binding.tvMobile.text = patientInfo.patientMobile.ifEmpty { "N/A" }
    }

    private fun setupUploadedDoc() {
        selectedDocuments.clear()
        caseData.model!!.lstConsultationImagesModel.forEach {

            selectedDocuments.add(
                SelectedDocData(
                    bitmap = null,
                    imgName = it.fileName,
                    base64 = it.filePath,
                    "",
                    fileFlag = it.fileFlag
                )
            )
        }
        setUpDocumentList()
    }

    private fun setupProblem() {
        val lstConsultationProblemsModel = caseData.model?.lstConsultationProblemsModel
        selectedProblems.clear()
        binding.etAdditionProblem.setText(consultationModel.additionalProblem)
        if (lstConsultationProblemsModel != null && lstConsultationProblemsModel.isNotEmpty()) {
            lstConsultationProblemsModel.forEach { problemData ->
                selectedProblems.add(
                    AllergyData(
                        term = problemData.name,
                        id = problemData.consultationProblemId
                    )
                )
            }
            addTag()
        }
    }

    private fun setupVitals() {
        val testData = caseData.model?.lstConsultationTestResultsModel
        if (testData != null) {
            if (testData.isNotEmpty()) {
                binding.btnViewAddedVital.visibility = View.VISIBLE
                selectedVitals = testData
            }
        }
    }

    private fun setupAllergy() {
        binding.etAdditionAlerrgy.setText(consultationModel.additionalAllergy)
        if (caseData.model?.lstConsultationAllergyModel != null) {
            selectedAllergies = caseData.model?.lstConsultationAllergyModel!!
        }
        if (selectedAllergies.isNotEmpty()) {
            binding.btnViewAddedAllergy.visibility = View.VISIBLE
        }
    }

    private fun setupExaminationData() {
        binding.etAdditionExamination.setText(consultationModel.gerneralExamination)
        if (consultationModel.isDiabetic) {
            binding.btnDiabetes.isSelected = if (binding.btnDiabetes.isSelected) {
                binding.btnDiabetes.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.edittext_hint
                    )
                )
                false
            } else {
                binding.btnDiabetes.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.white
                    )
                )
                true
            }

        }

        if (consultationModel.isHypertension) {
            binding.btnHypertension.isSelected = if (binding.btnHypertension.isSelected) {
                binding.btnHypertension.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.edittext_hint
                    )
                )
                false
            } else {
                binding.btnHypertension.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.white
                    )
                )
                true
            }
        }

        if (consultationModel.isSmoker) {
            binding.btnSmoking.isSelected = if (binding.btnSmoking.isSelected) {
                binding.btnSmoking.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.edittext_hint
                    )
                )
                false
            } else {
                binding.btnSmoking.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.white
                    )
                )
                true
            }

        }

        if (consultationModel.isAlcoholic) {
            binding.btnAlcoholIntake.isSelected = if (binding.btnAlcoholIntake.isSelected) {
                binding.btnAlcoholIntake.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.edittext_hint
                    )
                )
                false
            } else {
                binding.btnAlcoholIntake.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.white
                    )
                )
                true
            }
        }
    }
}