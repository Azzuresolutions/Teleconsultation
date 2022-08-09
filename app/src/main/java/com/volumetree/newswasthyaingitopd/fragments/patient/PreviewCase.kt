package com.volumetree.newswasthyaingitopd.fragments.patient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.adapters.DiagnosticsListAdapter
import com.volumetree.newswasthyaingitopd.adapters.UploadedDocumentListAdapter
import com.volumetree.newswasthyaingitopd.databinding.FragmentCasePreviewBinding
import com.volumetree.newswasthyaingitopd.model.requestData.cho.ConsultationModel
import com.volumetree.newswasthyaingitopd.model.requestData.cho.LstConsultationAllergyModel
import com.volumetree.newswasthyaingitopd.model.requestData.cho.LstConsultationTestResultsModel
import com.volumetree.newswasthyaingitopd.model.requestData.cho.PatientConsultationModelResponse
import com.volumetree.newswasthyaingitopd.model.responseData.cho.ChoPatientData
import com.volumetree.newswasthyaingitopd.model.responseData.comman.AllergyData
import com.volumetree.newswasthyaingitopd.model.responseData.patient.SelectedDocData
import com.volumetree.newswasthyaingitopd.utils.getAge
import com.volumetree.newswasthyaingitopd.utils.getGenderNameFromId
import com.volumetree.newswasthyaingitopd.utils.setHorizontalLayoutManager
import com.volumetree.newswasthyaingitopd.utils.setVerticalLayoutManager

class PreviewCase(
    private val selectedAllergies: ArrayList<LstConsultationAllergyModel>,
    private val selectedVitals: ArrayList<LstConsultationTestResultsModel>,
    private val selectedProblems: ArrayList<AllergyData>,
    private val selectedDocuments: ArrayList<SelectedDocData>,
    private val queryDesc: String,
    private val additionProblem: String,
    private val additionalAllergy: String,
    private val additionExamination: String,
    private val isSmoker: Boolean,
    private val isAlcoholic: Boolean,
    private val isHypertension: Boolean,
    private val isDiabetic: Boolean,
    private val patientData: ChoPatientData?,
    private val consultationModel: ConsultationModel?,
    private val patientInfo: PatientConsultationModelResponse?,
    private val showTitle: Boolean = false
) :
    DialogFragment(), View.OnClickListener {

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    private var _binding: FragmentCasePreviewBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCasePreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.layToolbar.imgBack.setOnClickListener(this)
        binding.layToolbar.tvActionbarTitle.text = getString(R.string.preview)
        if (!showTitle) {
            binding.layToolbar.titleLay.visibility = View.GONE
            dismiss()
        }
        setupData()
    }

    private fun setupData() {
        if (patientData != null) {
            val patientData = patientData
            val name = "${patientData.firstName} ${patientData.lastName}"
            binding.tvSelectedPatientName.text = name
            binding.tvPatientId.text = patientData.patientInfoId.toString()
            binding.tvPatientAgeGender.text =
                "${patientData.age.getAge()}, ${patientData.genderId.getGenderNameFromId()}"
            binding.tvMobile.text = patientData.mobile
        } else if (consultationModel != null) {
            val name = consultationModel.patientName
            binding.tvSelectedPatientName.text = name
            binding.tvPatientId.text = consultationModel.patientInfoId
            binding.tvPatientAgeGender.text =
                "${patientInfo?.patientAge?.getAge()}, ${
                    consultationModel.genderId.toInt().getGenderNameFromId()
                }"
            binding.tvMobile.text = patientInfo?.patientMobile?.ifEmpty { "N/A" } ?: "N/A"
        }
        binding.etQueryDesc.text = queryDesc.ifEmpty { getString(R.string.na) }
        binding.etAdditionProblem.text = additionProblem.ifEmpty { getString(R.string.na) }
        binding.etAdditionAlerrgy.text = additionalAllergy.ifEmpty { getString(R.string.na) }
        binding.etAdditionExamination.text = additionExamination.ifEmpty { getString(R.string.na) }

        binding.btnSmoking.visibility = if (isSmoker) {
            View.VISIBLE
        } else {
            View.GONE
        }
        binding.btnHypertension.visibility = if (isHypertension) {
            View.VISIBLE
        } else {
            View.GONE
        }
        binding.btnDiabetes.visibility = if (isDiabetic) {
            View.VISIBLE
        } else {
            View.GONE
        }
        binding.btnAlcoholIntake.visibility = if (isAlcoholic) {
            View.VISIBLE
        } else {
            View.GONE
        }

        setUpDocumentList()
        setAllergyText()
        setProblemText()
        setVitalText()
        setupDiagnostics()
    }

    private fun setupDiagnostics() {
        binding.rvDiagnostics.setVerticalLayoutManager(requireActivity())
        binding.rvDiagnostics.adapter = DiagnosticsListAdapter(selectedVitals)
    }

    private fun setVitalText() {
        selectedVitals
    }

    private fun setProblemText() {
        val joinToString = selectedProblems.joinToString(separator = ", ", transform = { it.term })
        binding.tvProblem.text = joinToString.ifEmpty { getString(R.string.na) }
    }

    private fun setAllergyText() {
        val strAllergy = StringBuilder()
        selectedAllergies.forEach { allerrgy ->
            strAllergy.append("\n${allerrgy.name}(${allerrgy.allergyDuration}, ${allerrgy.allergySeverityName})")
        }
        binding.etAllergy.text = strAllergy.toString().ifEmpty { getString(R.string.na) }

    }

    private fun setUpDocumentList() {
        binding.rvDocuments.visibility = View.VISIBLE
        binding.rvDocuments.setHorizontalLayoutManager(requireActivity())
        binding.rvDocuments.adapter = UploadedDocumentListAdapter(this.selectedDocuments, false)
    }

    override fun onClick(clickedView: View) {
        when (clickedView.id) {
            R.id.imgBack -> {
                dismiss()
            }
        }
    }
}