package com.volumetree.newswasthyaingitopd.fragments.patient

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.FilenameUtils
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.adapters.UploadedDocumentListAdapter
import com.volumetree.newswasthyaingitopd.databinding.FragmentFindDoctorBinding
import com.volumetree.newswasthyaingitopd.interfaces.SelectDoctorAndSpecializationListener
import com.volumetree.newswasthyaingitopd.model.requestData.patient.TokenImageData
import com.volumetree.newswasthyaingitopd.model.requestData.patient.TokenRegistrationRequest
import com.volumetree.newswasthyaingitopd.model.responseData.cho.ConsultationOnlineDoctorResponse
import com.volumetree.newswasthyaingitopd.model.responseData.patient.DoctorSpecializationData
import com.volumetree.newswasthyaingitopd.model.responseData.patient.SelectedDocData
import com.volumetree.newswasthyaingitopd.model.responseData.patient.SpecialityData
import com.volumetree.newswasthyaingitopd.utils.*
import com.volumetree.newswasthyaingitopd.viewmodel.PatientViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.InputStream


class FindDoctorFragment : Fragment(), View.OnClickListener, SelectDoctorAndSpecializationListener {
    private val selectedDocuments = ArrayList<SelectedDocData>()
    private var _binding: FragmentFindDoctorBinding? = null
    private val binding get() = _binding!!
    private var selectedSpeciality: SpecialityData? = null
    private var selectSpecializedDr: DoctorSpecializationData? = null
    private var lastSelected = 3
    private var uploadedDocumentListAdapter: UploadedDocumentListAdapter? = null

    private val patientViewModel: PatientViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFindDoctorBinding.inflate(inflater, container, false)
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
        binding.layUploadImages.setOnClickListener(this)
        binding.btnConsultNow.setOnClickListener(this)
        binding.layActionBar.imgBack.setOnClickListener(this)

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
            binding.etSelectDoctor.text = selectSpecializedDr!!.name
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
                lastSelected = 1
                selectedDocuments.clear()
                binding.btnConsultNow.enableDisable(true)
                binding.laySpecialOPD.visibility = View.GONE
                binding.layUploadImages.visibility = View.VISIBLE
                binding.rvDocuments.visibility = View.VISIBLE
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
                lastSelected = 2
                selectedDocuments.clear()
                binding.btnConsultNow.enableDisable(false)
                binding.laySpecialOPD.visibility = View.VISIBLE
                binding.layUploadImages.visibility = View.VISIBLE
                binding.rvDocuments.visibility = View.VISIBLE
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
            R.id.layUploadImages -> {
                if (selectedDocuments.size == 5) {
                    requireActivity().showToast(getString(R.string.more_than_five_document))
                } else {
                    startIntent()
                }
            }
            R.id.btnConsultNow -> {
                callTokenRegistrationAPI()
//                Common.Companion.CustomBottomSheetDialogFragment(::onConfirmation)
//                    .show(parentFragmentManager, "")
            }
        }
    }

    private fun startIntent() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }

    private fun showSpecializedDoctor() {
        if (selectedSpeciality == null) {
            requireActivity().showToast(getString(R.string.select_specialization_first))
            return
        }
        SpecializedDoctorFragment(
            selectedSpeciality!!.specialityId,
            selectedSpeciality!!.specialityName,
            this
        ).show(parentFragmentManager, Constants.SELECT_DOCTOR)
    }

    private fun showSpeciality() {
        SpecializationFragment(this).show(parentFragmentManager, Constants.SELECT_SPECIALIZATION)
    }

    private fun setUpDocumentList() {
        binding.rvDocuments.setVerticalLayoutManager(requireActivity())
        uploadedDocumentListAdapter = UploadedDocumentListAdapter(selectedDocuments, true)
        binding.rvDocuments.adapter = uploadedDocumentListAdapter
    }

    private fun onConfirmation() {
        callTokenRegistrationAPI()
    }

    private fun callTokenRegistrationAPI() {
        val patientInfoId = PrefUtils.getPatientUserData(requireActivity())!!.patientInfoId
        val imageList: ArrayList<TokenImageData> = ArrayList()
        if (selectedDocuments.isNotEmpty()) {
            selectedDocuments.forEach { selectedDocData ->
                val base64 = selectedDocData.bitmap?.getBase64()
                if (base64 != null) {
                    imageList.add(TokenImageData(base64))
                }
            }
        }
        val tokenRegistrationRequest = TokenRegistrationRequest(
            patientInfoId = patientInfoId.toString(),
            opdTypeId = lastSelected.toString(),
            specialityId = (selectedSpeciality?.specialityId ?: 0).toString(),
            doctorId = (selectSpecializedDr?.id
                ?: 0).toString(),
            imageList
        )
        patientViewModel.registrationToken(tokenRegistrationRequest)
            .observeOnce(viewLifecycleOwner) { tokenRegistrationData ->
                if (tokenRegistrationData.success) {
                    CommonDialog.showTokenDialog(
                        requireActivity(),
                        ConsultationOnlineDoctorResponse(),
                        ::onTokenConfirmation
                    )
                }
            }
    }

    private fun onTokenConfirmation(consultationOnlineDoctorResponse: ConsultationOnlineDoctorResponse) {
        if (consultationOnlineDoctorResponse.success) {
//            findNavController().navigate(FindDoctorFragmentDirections.actionWaitingRoom())
        }
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data != null && data.data != null) {
                    val imageStream: InputStream? =
                        requireActivity().contentResolver?.openInputStream(data.data!!)
                    if (selectedDocuments.isEmpty()) {
                        setUpDocumentList()
                    }

                    val selectedImage = BitmapFactory.decodeStream(imageStream)
                    val path = data.data!!.path
                    if (path != null) {
                        selectedDocuments.add(
                            SelectedDocData(
                                bitmap = selectedImage,
                                imgName = path.substring(path.lastIndexOf("/") + 1),
                                "", "",
                                fileFlag = FilenameUtils.getExtension(path)
                            )
                        )
                    }
                    uploadedDocumentListAdapter?.notifyItemInserted(selectedDocuments.size - 1)
                    val encodedImage: String? = selectedImage.getBase64()
                    encodedImage?.let { Log.d("encodedImage", it) }
                }
            }
        }

    override fun onSpecialization(specialityData: SpecialityData?) {
        if (specialityData != null) {
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
            binding.etSelectDoctor.text = selectSpecializedDr!!.name
            if (selectedSpeciality != null) {
                binding.btnConsultNow.enableDisable(true)
            }
        }
        parentFragmentManager.dismissFragmentDialog(Constants.SELECT_DOCTOR)
    }


}