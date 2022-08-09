package com.volumetree.newswasthyaingitopd.fragments.cho

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.databinding.FragmentChoEditProfileBinding
import com.volumetree.newswasthyaingitopd.interfaces.PicDocumentSelector
import com.volumetree.newswasthyaingitopd.model.requestData.cho.UpdateChoRequest
import com.volumetree.newswasthyaingitopd.model.requestData.cho.UploadProfilePicRequest
import com.volumetree.newswasthyaingitopd.model.responseData.cho.ChoProfileResponse
import com.volumetree.newswasthyaingitopd.model.responseData.comman.CityModelData
import com.volumetree.newswasthyaingitopd.model.responseData.comman.DistrictModelData
import com.volumetree.newswasthyaingitopd.model.responseData.patient.SelectedDocData
import com.volumetree.newswasthyaingitopd.utils.*
import com.volumetree.newswasthyaingitopd.viewmodel.MasterViewModel
import com.volumetree.newswasthyaingitopd.viewmodel.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditChoProfileFragment : Fragment(), View.OnClickListener, PicDocumentSelector {

    private var _binding: FragmentChoEditProfileBinding? = null
    private val binding get() = _binding!!
    private var lastSelectedCity: CityModelData? = null
    private var lastSelectedDistrict: DistrictModelData? = null
    private var genderId = 0
    private val choViewModel: ProfileViewModel by viewModel()
    private val masterViewModel: MasterViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChoEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.showBottomNavigationView(false)
        binding.btnMale.setOnClickListener(this)
        binding.btnFeMale.setOnClickListener(this)
        binding.btnTg.setOnClickListener(this)
        binding.tvEditProfile.setOnClickListener(this)
        binding.btnProceed.setOnClickListener(this)
        binding.etCity.setOnClickListener(this)
        binding.layUploadImages.setOnClickListener(this)
        binding.imgDelete.setOnClickListener(this)
        setData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    var choData: ChoProfileResponse.ChoProfileData? = null
    private fun setData() {
        choData = PrefUtils.getChoData(requireActivity())
        if (choData != null) {
            binding.tvDrName.text =
                "${choData!!.firstName} ${choData!!.lastName}".capitalizeLetter()
            binding.tvDoctorSpeciality.text = choData!!.specializationName
            binding.etEmail.setText(choData!!.email)
            binding.tvPhoneNumberValue.text = choData!!.mobile
            binding.tvDOBValue.text = choData!!.dob.fromDOBToStr()
            binding.tvSpecializationValue.text = choData!!.specializationName
            binding.tvQualificationValue.text = choData!!.qualificationName
            binding.tvDistrictValue.text = choData!!.districtName
            binding.tvStateValue.text = choData!!.stateName
            binding.tvLoginNameValue.text = choData!!.userName
            binding.imgProfile.loadImageURL(
                requireActivity(),
                choData!!.imagePath,
                isNoCache = true
            )
            if (choData!!.signaturePath == null || choData!!.signaturePath.isEmpty()) {
                binding.laySignature.visibility = View.GONE
            } else {
                binding.laySignature.visibility = View.VISIBLE
                binding.imgSignature.loadImageURL(
                    requireActivity(),
                    choData!!.signaturePath,
                    isNoCache = true
                )
                binding.tvDocName.text = choData!!.signaturePath.getFileName()
            }
            binding.etAddress.setText(choData!!.addressLine1)
            binding.etAddress2.setText(choData!!.addressLine2)
            binding.etPinCode.setText(choData!!.pinCode)
            lastSelectedCity =
                CityModelData(cityName = choData!!.cityName, cityId = choData!!.cityId)
            lastSelectedDistrict = DistrictModelData(
                districtId = choData!!.districtId,
                districtName = choData!!.districtName
            )
            binding.etCity.text = lastSelectedCity!!.cityName
            binding.etMobile.setText(choData!!.mobile)
            genderId = choData!!.genderId

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
        }
    }

    private fun showCityListDialog() {
        if (lastSelectedDistrict == null) {
            requireActivity().showToast(getString(R.string.select_district_first))
            return
        }
        masterViewModel.showCityListDialog(
            lastSelectedDistrict!!.districtId,
            requireActivity(),
            ::cityClick
        )
    }

    private fun cityClick(cityModelData: CityModelData, cityDialog: Dialog) {
        binding.etCity.text = cityModelData.cityName
        lastSelectedCity = cityModelData
        cityDialog.dismiss()
    }

    override fun onClick(clickedView: View) {
        when (clickedView.id) {
            R.id.imgDelete -> {
                binding.laySignature.visibility = View.GONE
            }
            R.id.layUploadImages -> {
                CommonDialog.Companion.ShowImagePicker(
                    false, requireActivity(), this, this, 2
                )
                    .show(parentFragmentManager, Constants.FILE_PICKER)
            }
            R.id.etCity -> {
                showCityListDialog()
            }
            R.id.tvEditProfile -> {
                CommonDialog.Companion.ShowImagePicker(
                    false, requireActivity(), this, this, 1
                )
                    .show(parentFragmentManager, Constants.FILE_PICKER)
            }
            R.id.btnProceed -> {
                updateChoProfile()
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
        }
    }

    private fun updateChoProfile() {
        if (checkValidation()) {
            val updateChoRequest = UpdateChoRequest(
                memberId = choData?.memberId.toString(),
                addressLine1 = binding.etAddress.getTextFromEt(),
                addressLine2 = binding.etAddress2.getTextFromEt(),
                pinCode = binding.etPinCode.getTextFromEt(),
                mobile = binding.etMobile.getTextFromEt(),
                email = binding.etEmail.getTextFromEt(),
                genderID = genderId,
                genderName = genderId.getGenderNameFromId(),
                cityId = lastSelectedCity?.cityId ?: 0
            )
            choViewModel.updateChoProfile(updateChoRequest)
                .observeOnce(viewLifecycleOwner) { choProfileResponse ->
                    if (choProfileResponse.success) {
                        PrefUtils.setChoData(requireActivity(), choProfileResponse.model)
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
            binding.etMobile.text.toString().isEmpty() -> {
                requireActivity().showToast(getString(R.string.enter_mobile_number))
                false
            }
            !binding.etMobile.phoneNotMandatoryValidation() -> {
                requireActivity().showToast(getString(R.string.valid_enter_mobile_number))
                false
            }
            binding.etEmail.getTextFromEt().isEmpty() -> {
                requireActivity().showToast(getString(R.string.enter_email))
                false
            }
            !binding.etEmail.emailValidation() -> {
                requireActivity().showToast(
                    getString(
                        R.string.please_enter_valid_email
                    )
                )
                false
            }
            binding.etCity.text.toString().isEmpty() -> {
                requireActivity().showToast(getString(R.string.please_select_city))
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
        val choData1 = PrefUtils.getChoData(requireActivity())

        if (type == 1) {
            selectedDocData.bitmap?.let { binding.imgProfile.showImageAsBitmap(it) }
            masterViewModel.uploadProfilePic(
                UploadProfilePicRequest(
                    imagePath = selectedDocData.base64,
                    fileFlag = "image/" + selectedDocData.fileFlag,
                    fileName = "${choData1!!.memberId}_profile." + selectedDocData.fileFlag
                )
            ).observeOnce(viewLifecycleOwner) { imageUploadResponse ->
                requireActivity().showToast(imageUploadResponse.message)
                choData1.imagePath = imageUploadResponse.imageURL
                PrefUtils.setChoData(requireActivity(), choData1)
            }
        } else if (type == 2) {
            binding.laySignature.visibility = View.VISIBLE
            selectedDocData.bitmap?.let { binding.imgSignature.showImageAsBitmap(it) }
            binding.tvDocName.text = selectedDocData.imgName
            masterViewModel.uploadSignatureImage(
                UploadProfilePicRequest(
                    imagePath = selectedDocData.base64,
                    fileFlag = "image/" + selectedDocData.fileFlag,
                    fileName = "${choData1!!.memberId}_sign." + selectedDocData.fileFlag
                )
            ).observeOnce(viewLifecycleOwner) { imageUploadResponse ->
                requireActivity().showToast(imageUploadResponse.message)
                choData1.signaturePath = imageUploadResponse.imageURL
                PrefUtils.setChoData(requireActivity(), choData1)
            }
        }
        parentFragmentManager.dismissFragmentDialog(Constants.FILE_PICKER)
    }
}