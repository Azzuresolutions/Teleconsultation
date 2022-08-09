package com.volumetree.newswasthyaingitopd.fragments.patient

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
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
import com.volumetree.newswasthyaingitopd.application.App.Companion.consultationId
import com.volumetree.newswasthyaingitopd.databinding.FragmentWaitingRoomBinding
import com.volumetree.newswasthyaingitopd.fragments.common.AllergyFragment
import com.volumetree.newswasthyaingitopd.fragments.doctor.AllergyListFragment
import com.volumetree.newswasthyaingitopd.fragments.doctor.VitalListFragment
import com.volumetree.newswasthyaingitopd.interfaces.CreateCaseListener
import com.volumetree.newswasthyaingitopd.interfaces.PicDocumentSelector
import com.volumetree.newswasthyaingitopd.model.requestData.cho.*
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
import java.util.*


class WaitingFragment : Fragment(), View.OnClickListener, CreateCaseListener, PicDocumentSelector {

    private var _binding: FragmentWaitingRoomBinding? = null
    private val binding get() = _binding!!
    private val args: WaitingFragmentArgs by navArgs()
    private val choViewModel: ChoViewModel by viewModel()
    private val masterViewModel: MasterViewModel by viewModel()

    private lateinit var consultationModel: ConsultationModel
    private lateinit var patientInfo: PatientConsultationModelResponse
    private lateinit var caseData: DraftConsultationResponse

    private var lastSelectedAllergy: AllergyData? = null
    private var lastDuration: DurationData? = null
    private var lastSeverity: SeverityData? = null
    private var lastSelectedVital: VitalData? = null
    private var lastSelectedProblem: AllergyData? = null

    private var selectedAllergies: ArrayList<LstConsultationAllergyModel> = ArrayList()
    private var selectedProblems: ArrayList<AllergyData> = ArrayList()
    private var selectedVitals: ArrayList<LstConsultationTestResultsModel> = ArrayList()
    private val selectedDocuments = ArrayList<SelectedDocData>()

    private var uploadedDocumentListAdapter: UploadedDocumentListAdapter? = null
    var timerObj: Timer = Timer()
    private var timerTaskObj: TimerTask = object : TimerTask() {
        override fun run() {
            requireActivity().runOnUiThread {
                checkAndUpdateQueueData()
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWaitingRoomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.showBottomNavigationView(false)
        binding.tvActionbarTitle.text = getString(R.string.waiting_room)
        if (WaitingFragment2.isBackFromWaiting) {
            WaitingFragment2.isBackFromWaiting = false
            findNavController().popBackStack()
        }
        manageClick()
        setupDoctorData()
//        checkAndUpdateQueueData()
        
    }

    private fun manageClick() {
        binding.imgBack.setOnClickListener(this)
        binding.tvExistRoom.setOnClickListener(this)
        binding.layAllergyTitle.setOnClickListener(this)
        binding.layGeneralExaminationTitle.setOnClickListener(this)
        binding.layPrescriptionTitle.setOnClickListener(this)
        binding.layProblemTitle.setOnClickListener(this)
        binding.layDiagnosticsTitle.setOnClickListener(this)
        binding.layMedicalRecordTitle.setOnClickListener(this)
        binding.layUploadImages.setOnClickListener(this)
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
        binding.btnViewAddedAllergy.setOnClickListener(this)
        binding.btnViewAddedVital.setOnClickListener(this)
        binding.btnAddPrescriptionToList.setOnClickListener(this)
        binding.btnSend.setOnClickListener(this)
    }

    private fun setupDoctorData() {
//        consultationData = args.consultationData
//        binding.tvToken.text = consultationData.token
        getCaseDetails()

        masterViewModel.getDoctorProfileById(args.consultationData.model.doctor_id)
            .observeOnce(viewLifecycleOwner) { doctorData ->
                if (doctorData.success) {
                    val doctorSpecializationData = doctorData.model
                    binding.tvDrOnlineState.manageOnlineOfflineStatusWithColor(
                        binding.root.context,
                        doctorSpecializationData.doctor_Status
                    )
                    binding.imgDr.loadImageURL(
                        binding.root.context,
                        doctor = true,
                        imagePath = doctorSpecializationData.doctorimage
                    )

                    binding.tvRating.text = doctorSpecializationData.doctor_rating
                    binding.tvExperience.text = doctorSpecializationData.experience.ifEmpty {
                        binding.tvExperience.visibility = View.GONE
                        binding.imgExperienceDot.visibility = View.GONE
                        ""
                    } + " yrs exp"

                    binding.tvDrName.text = doctorSpecializationData.doctor_Name
                    args.consultationData.model.doctor_Name = doctorSpecializationData.doctor_Name
                    args.consultationData.model.doctorimage = doctorSpecializationData.doctorimage
                    args.consultationData.model.speciality =
                        doctorSpecializationData.specialityName
                }
            }
    }

    private fun getCaseDetails() {
        choViewModel.getConsultation(consultationId)
            .observeOnce(viewLifecycleOwner) { consultationData ->
                this.caseData = consultationData
                if (this.caseData.model != null) {
                    setupCaseData()
                }
            }

    }

    private fun checkAndUpdateQueueData() {
        args.consultationData.model.let { doctorData ->
            choViewModel.getPatientQueueListByDoctor(doctorData.doctor_id)
                .observeOnce(viewLifecycleOwner) { doctorQueueData ->

                    if (doctorQueueData.success && doctorQueueData.model?._list != null
                        && doctorQueueData.model._list.isNotEmpty()
                    ) {
                        val queueList =
                            doctorQueueData.model._list.filter { it.consultationId == consultationId.toString() }
                        if (queueList.isNotEmpty()) {
                            binding.tvQueueCount.text = queueList[0].waitingQueueId.toString()
                            binding.tvQueue.text = Html.fromHtml(
                                "Your current queue number is ${queueList[0].waitingQueueId} \n<font color='#99FFFFFF'> Please keep patience...</font>"
                            )
                            binding.waitingSeekBar.progress =
                                ((queueList[0].waitingQueueId) / doctorQueueData.model._list.size) * 100

                            if (queueList[0].waitingQueueId == 1) {
                                findNavController().navigate(
                                    WaitingFragmentDirections.actionWaitingRoom2(
                                        args.consultationData,
                                        queueList[0]
                                    )
                                )
                            }
                        }
                    }

                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
            R.id.btnSend -> {
                draftConsultation()
            }
            R.id.layUploadImages -> {
                if (selectedDocuments.size == Constants.MAX_DOCUMENT) {
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
                binding.btnDiabetes.isSelected =
                    if (binding.btnDiabetes.isSelected) {
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
                binding.btnSmoking.isSelected =
                    if (binding.btnSmoking.isSelected) {
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
                binding.btnAlcoholIntake.isSelected =
                    if (binding.btnAlcoholIntake.isSelected) {
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
                binding.btnHypertension.isSelected =
                    if (binding.btnHypertension.isSelected) {
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

    private fun onBack() {
        masterViewModel.cancelDeleteToken(
            CancelTokenRequest(
                doctorId = args.consultationData.model.doctor_id,
                patientInfoId = args.consultationData.patientInfoId,
                consultationId = consultationId
            )
        ).observeOnce(viewLifecycleOwner) { cancelTokenData ->
            if (cancelTokenData.success) {
                findNavController().popBackStack()
            }
        }

    }

    private fun setupCaseData() {
        setupPatientData()
        setupExaminationData()
        setupAllergy()
        setupProblem()
        setupUploadedDoc()
        setupVitals()
        binding.etQueryDesc.setText(consultationModel.queryDesc)
    }

    private fun setupPatientData() {
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

    private fun setupExaminationData() {
        binding.etAdditionExamination.setText(consultationModel.gerneralExamination)
        if (consultationModel.isDiabetic) {
            binding.btnDiabetes.performClick()
        }

        if (consultationModel.isHypertension) {
            binding.btnHypertension.performClick()
        }

        if (consultationModel.isSmoker) {
            binding.btnSmoking.performClick()
        }

        if (consultationModel.isAlcoholic) {
            binding.btnAlcoholIntake.performClick()
        }
    }

    private fun setupUploadedDoc() {
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

    private fun setUpDocumentList() {
        binding.rvDocuments.setHorizontalLayoutManager(requireActivity())
        uploadedDocumentListAdapter = UploadedDocumentListAdapter(selectedDocuments, true)
        binding.rvDocuments.adapter = uploadedDocumentListAdapter
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

    private fun vitalClick(vitalData: VitalData?, dialog: Dialog?) {
        if (vitalData == null) {
            binding.tvDiagnosticsValue.text = ""
            binding.etResult.hint = ""
            binding.etResult.setText("")
        } else {
            lastSelectedVital = vitalData
            binding.tvDiagnosticsValue.text = lastSelectedVital!!.categoryName
            binding.etResult.hint = lastSelectedVital!!.units
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

    private fun draftConsultation() {
        if (checkValidation()) {
            choViewModel.updateConsultation(getCreateCaseObject())
                .observeOnce(viewLifecycleOwner) { draftConsultationResponse ->
                    requireActivity().showToast(draftConsultationResponse.message)
                    getCaseDetails()
                }
        }
    }

    private fun getCreateCaseObject(): DraftConsultationRequest2 {
        val lstConsultationMedicineModel = ArrayList<LstConsultationMedicineModel>()
        val lstConsultationMessageModel = ArrayList<LstConsultationMessageModel>()

        val consultationModel = ConsultationModel()
        consultationModel.consultationId = consultationId
        consultationModel.patientInfoId = this.consultationModel.patientInfoId
        consultationModel.patientName = this.consultationModel.patientName
        consultationModel.gender = this.consultationModel.genderId.toInt().getGenderNameFromId()
        consultationModel.genderId = this.consultationModel.genderId
        consultationModel.PatientDOB = this.consultationModel.dob
        consultationModel.mobileNumber = this.patientInfo.patientMobile
        consultationModel.StateId = this.consultationModel.StateId
        consultationModel.patientAddress = this.patientInfo.patientAddress
        consultationModel.CRNumber = ""
        consultationModel.physicalExamination = this.consultationModel.gerneralExamination
        consultationModel.systemicExamination = ""
        consultationModel.additionalMedicine = ""
        consultationModel.isDiabetic = binding.btnDiabetes.isSelected
        consultationModel.isAlcoholic = binding.btnAlcoholIntake.isSelected
        consultationModel.isHypertension = binding.btnHypertension.isSelected
        consultationModel.isSmoker = binding.btnSmoking.isSelected
        consultationModel.additionalAllergy =
            binding.etAdditionAlerrgy.getTextFromEt()
        consultationModel.additionalDiagnosis = ""
        consultationModel.additionalProblem =
            binding.etAdditionProblem.getTextFromEt()
        consultationModel.dob = this.consultationModel.dob
        consultationModel.queryDesc = binding.etQueryDesc.getTextFromEt()
        consultationModel.gerneralExamination =
            binding.etAdditionExamination.getTextFromEt()

        val patientConsultationModel = PatientConsultationModel2()
        patientConsultationModel.consultationId = consultationId.toString()
        patientConsultationModel.patientInfoId = this.consultationModel.patientInfoId
        patientConsultationModel.patientFirstName = this.consultationModel.patientName
        patientConsultationModel.patientLastName = this.consultationModel.patientName
        patientConsultationModel.patientGenderId = this.consultationModel.genderId
        patientConsultationModel.PatientDOB = this.consultationModel.dob
        patientConsultationModel.patientMobile = this.patientInfo.patientMobile
        patientConsultationModel.StateId = this.consultationModel.StateId
        patientConsultationModel.patientAddress = this.patientInfo.patientAddress
        patientConsultationModel.crNumber = ""

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
                    categoryName = vitalData.categoryName.ifEmpty { vitalData.name },
                    units = vitalData.units ?: "",
                    testId = vitalData.testId,
                    result = vitalData.result
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

        val lstConsultationImagesModel = ArrayList<LstConsultationImagesModel>()
        if (isImageUploaded) {
            Log.d("UploadedImageSize", uploadedDocumentListAdapter?.docList?.size.toString())
            uploadedDocumentListAdapter?.docList?.forEach { selectedDocData ->
                val lstConsultationImages = LstConsultationImagesModel()
                lstConsultationImages.fileFlag = selectedDocData.fileFlag
                lstConsultationImages.fileName = selectedDocData.imgName
                lstConsultationImages.filePath = selectedDocData.base64
                lstConsultationImagesModel.add(lstConsultationImages)
            }
        }

        return DraftConsultationRequest2(
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

    override fun onAllergySelected(allergyData: AllergyData?) {
        if (allergyData == null) {
            binding.etEnterAllergy.text = ""
        } else {
            parentFragmentManager.dismissFragmentDialog(Constants.ALLERGY)
            lastSelectedAllergy = allergyData
            binding.etEnterAllergy.text = lastSelectedAllergy!!.term
        }
    }

    override fun onProblemSelected(allergyData: AllergyData?) {
        if (allergyData != null) {
            parentFragmentManager.dismissFragmentDialog(Constants.PROBLEM)
            lastSelectedProblem = allergyData
            binding.etProblem.text = lastSelectedProblem!!.term
            selectedProblems.add(allergyData)
            addTag()
        }
    }

    override fun onMedicineSelected(allergyData: AllergyData?) {
        TODO("Not yet implemented")
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

    private var isImageUploaded = false
    override fun onProfilePicSelected(selectedDocData: SelectedDocData, type: Int) {
        if (!isImageUploaded) {
            isImageUploaded = true
            selectedDocuments.clear()
            setUpDocumentList()
        }
        if (selectedDocuments.size < Constants.MAX_DOCUMENT) {
            selectedDocuments.add(selectedDocData)
            uploadedDocumentListAdapter?.notifyItemInserted(selectedDocuments.size - 1)
        }
        parentFragmentManager.dismissFragmentDialog(Constants.FILE_PICKER)
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

    override fun onResume() {
        super.onResume()
        try {
            timerObj = Timer()
            timerObj.schedule(timerTaskObj, 0, 10000)
        } catch (e: Exception) {
        }
    }

    override fun onPause() {
        super.onPause()
        try {
            timerObj.cancel()
        } catch (e: Exception) {
        }
    }
}