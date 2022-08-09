package com.volumetree.newswasthyaingitopd.fragments.doctor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.databinding.FragmentDoctorEditProfileBinding
import com.volumetree.newswasthyaingitopd.interfaces.PicDocumentSelector
import com.volumetree.newswasthyaingitopd.model.requestData.cho.UploadProfilePicRequest
import com.volumetree.newswasthyaingitopd.model.requestData.doctor.UpdateDoctorRequest
import com.volumetree.newswasthyaingitopd.model.responseData.patient.SelectedDocData
import com.volumetree.newswasthyaingitopd.utils.*
import com.volumetree.newswasthyaingitopd.viewmodel.DoctorViewModel
import com.volumetree.newswasthyaingitopd.viewmodel.MasterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class EditDoctorProfileFragment : Fragment(), View.OnClickListener, PicDocumentSelector {

    private var _binding: FragmentDoctorEditProfileBinding? = null
    private val binding get() = _binding!!
    private var genderId = 0

    private val doctorViewModel: DoctorViewModel by viewModel()
    private val masterViewModel: MasterViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoctorEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.showBottomNavigationView(false)
        binding.tvUploadProfilePic.setOnClickListener(this)
        binding.layUploadImages.setOnClickListener(this)
        binding.btnMale.setOnClickListener(this)
        binding.btnFeMale.setOnClickListener(this)
        binding.btnTg.setOnClickListener(this)
        binding.btnProceed.setOnClickListener(this)
        binding.imgDelete.setOnClickListener(this)
        setData()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setData() {
        val doctorProfileData = PrefUtils.getDoctorData(requireActivity())
        if (doctorProfileData != null) {
            binding.tvDrName.text =
                "${doctorProfileData.firstName} ${doctorProfileData.lastName}".capitalizeLetter()
            binding.tvDoctorSpeciality.text = doctorProfileData.specializationName
            binding.tvEmailValue.text = doctorProfileData.email
            binding.tvPhoneNumberValue.text = doctorProfileData.mobile
            binding.tvDOBValue.text = doctorProfileData.dob.fromDOBToStr()
            binding.tvSpecializationValue.text = doctorProfileData.specializationName
            binding.tvQualificationValue.text = doctorProfileData.qualificationName
            binding.etRegistrationNumber.setText(doctorProfileData.registrationNumber)
            binding.etExperience.setText(doctorProfileData.doctor_exp)
            binding.etPinCode.setText(doctorProfileData.pinCode)
            binding.etAddress.setText(doctorProfileData.addressLine1)
            genderId = doctorProfileData.genderId

            when (genderId) {
                1 -> {
                    binding.btnMale.performClick()
                }
                2 -> {
                    binding.btnFeMale.performClick()
                }
                3 -> {
                    binding.btnTg.performClick()
                }
                4 -> {
                    binding.btnMale.performClick()
                }
            }

            binding.imgProfile.loadImageURL(
                requireActivity(),
                doctorProfileData.imagePath,
                isNoCache = true,
                doctor = true
            )
            if (doctorProfileData.signaturePath == null || doctorProfileData.signaturePath.isEmpty()) {
                binding.laySignature.visibility = View.GONE
            } else {
                binding.laySignature.visibility = View.VISIBLE
                binding.imgSignature.loadImageURL(
                    requireActivity(),
                    doctorProfileData.signaturePath,
                    isNoCache = true
                )
                binding.tvDocName.text = doctorProfileData.signaturePath.getFileName()
            }
        }
    }

    override fun onClick(clickedView: View) {
        when (clickedView.id) {
            R.id.tvUploadProfilePic -> {
                CommonDialog.Companion.ShowImagePicker(
                    false, requireActivity(), this, this, 1
                )
                    .show(parentFragmentManager, Constants.FILE_PICKER)
            }
            R.id.layUploadImages -> {
                CommonDialog.Companion.ShowImagePicker(
                    false, requireActivity(), this, this, 2
                )
                    .show(parentFragmentManager, Constants.FILE_PICKER)
            }
            R.id.btnMale -> {
                genderId = 1
                binding.btnMale.selectGender(
                    requireActivity(),
                    binding.btnFeMale,
                    binding.btnTg
                )
            }
            R.id.btnFeMale -> {
                genderId = 2
                binding.btnFeMale.selectGender(
                    requireActivity(),
                    binding.btnMale,
                    binding.btnTg
                )
            }
            R.id.btnTg -> {
                genderId = 3
                binding.btnTg.selectGender(
                    requireActivity(),
                    binding.btnFeMale,
                    binding.btnMale
                )
            }
            R.id.btnProceed -> {
                updateDoctorProfile()
            }
            R.id.imgDelete->{
                binding.laySignature.visibility = View.GONE
            }
        }

    }

    private fun updateDoctorProfile() {
        if (checkValidation()) {
            doctorViewModel.updateDoctorProfile(
                UpdateDoctorRequest(
                    memberId = PrefUtils.getDoctorData(requireActivity())?.memberId.toString(),
                    addressLine1 = binding.etAddress.getTextFromEt(),
                    pinCode = binding.etPinCode.getTextFromEt(),
                    registrationNumber = binding.etRegistrationNumber.getTextFromEt(),
                    doctor_Exp = binding.etExperience.getTextFromEt(),
                    genderID = genderId,
                    genderName = genderId.getGenderNameFromId(),
                    imagePath = ""
                )
            ).observeOnce(viewLifecycleOwner) { updateProfileData ->
                if (updateProfileData.success) {
                    requireActivity().showToast(updateProfileData.message)
                    PrefUtils.setDoctorData(requireActivity(), updateProfileData.model)
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun checkValidation(): Boolean {
        val isValid: Boolean = when {

            genderId == 0 -> {
                requireActivity().showToast(getString(R.string.select_gender))
                false
            }

            binding.etAddress.getTextFromEt().isEmpty() -> {
                requireActivity().showToast(getString(R.string.please_enter_address))
                false
            }

            binding.etPinCode.pinCodeValidation() -> {
                requireActivity().showToast(getString(R.string.please_enter_pincode))
                false
            }

            else -> {
                true
            }
        }
        return isValid

    }

    override fun onProfilePicSelected(selectedDocData: SelectedDocData, type: Int) {
        val doctorProfileData = PrefUtils.getDoctorData(requireActivity())

        if (type == 1) {
            selectedDocData.bitmap?.let { binding.imgProfile.showImageAsBitmap(it) }
            masterViewModel.uploadProfilePic(
                UploadProfilePicRequest(
                    imagePath = selectedDocData.base64,
                    fileFlag = "image/" + selectedDocData.fileFlag,
                    fileName = "${doctorProfileData!!.memberId}_profile." + selectedDocData.fileFlag
                )
            ).observeOnce(viewLifecycleOwner) { imageUploadResponse ->
                requireActivity().showToast(imageUploadResponse.message)
                doctorProfileData.imagePath = imageUploadResponse.imageURL
                PrefUtils.setDoctorData(requireActivity(), doctorProfileData)
            }
        } else if (type == 2) {
            binding.laySignature.visibility = View.VISIBLE
            selectedDocData.bitmap?.let { binding.imgSignature.showImageAsBitmap(it) }
            binding.tvDocName.text = selectedDocData.imgName
            masterViewModel.uploadSignatureImage(
                UploadProfilePicRequest(
                    imagePath = selectedDocData.base64,
                    fileFlag = "image/" + selectedDocData.fileFlag,
                    fileName = "${doctorProfileData!!.memberId}_sign." + selectedDocData.fileFlag
                )
            ).observeOnce(viewLifecycleOwner) { imageUploadResponse ->
                requireActivity().showToast(imageUploadResponse.message)
                doctorProfileData.signaturePath = imageUploadResponse.imageURL
                PrefUtils.setDoctorData(requireActivity(), doctorProfileData)
            }
        }
        parentFragmentManager.dismissFragmentDialog(Constants.FILE_PICKER)
    }
}