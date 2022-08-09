package com.volumetree.newswasthyaingitopd.fragments.patient

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.databinding.FragmentEditProfileBinding
import com.volumetree.newswasthyaingitopd.model.requestData.patient.RegistrationRequest
import com.volumetree.newswasthyaingitopd.model.responseData.comman.BlockModelData
import com.volumetree.newswasthyaingitopd.model.responseData.comman.CityModelData
import com.volumetree.newswasthyaingitopd.model.responseData.comman.DistrictModelData
import com.volumetree.newswasthyaingitopd.model.responseData.patient.PatientProfileResponse
import com.volumetree.newswasthyaingitopd.utils.*
import com.volumetree.newswasthyaingitopd.viewmodel.MasterViewModel
import com.volumetree.newswasthyaingitopd.viewmodel.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class EditProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private var myCalendar: Calendar? = null
    private var genderId = ""

    private val masterViewModel: MasterViewModel by viewModel()
    private val profileViewModel: ProfileViewModel by viewModel()

    private var lastSelectedDistrict: DistrictModelData? = null
    private var lastSelectedCity: CityModelData? = null
    private var lastSelectedBlock: BlockModelData? = null
    private var userData: PatientProfileResponse.PatientProfileData? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.showBottomNavigationView(false)
        binding.layToolbar.tvActionbarTitle.text = getString(R.string.edit_profile)
        binding.layToolbar.imgBack.setOnClickListener(this)
        binding.etDateOfBirth.setOnClickListener(this)
        binding.btnMale.setOnClickListener(this)
        binding.btnFeMale.setOnClickListener(this)
        binding.btnTg.setOnClickListener(this)
        binding.btnProceed.setOnClickListener(this)

        binding.etDistrict.setOnClickListener(this)
        binding.etCity.setOnClickListener(this)
        binding.etBlock.setOnClickListener(this)
        getAndSetData()
    }

    private fun getAndSetData() {
        userData = PrefUtils.getPatientUserData(requireActivity())
        myCalendar = Calendar.getInstance()
        if (userData != null) {
            val fromStrToDate = userData!!.dob.fromStrToDate2()
            if (fromStrToDate != null) {
                myCalendar?.set(Calendar.YEAR, fromStrToDate.year)
                myCalendar?.set(Calendar.MONTH, fromStrToDate.month)
                myCalendar?.set(Calendar.DAY_OF_MONTH, fromStrToDate.day)
            }
            lastSelectedDistrict = DistrictModelData(
                districtId = userData!!.districtId,
                districtName = userData!!.districtName
            )

            lastSelectedCity = CityModelData(
                cityId = userData!!.cityId,
                cityName = userData!!.cityName
            )

            lastSelectedBlock = BlockModelData()

            binding.etFirstName.setText(userData!!.firstName)
            binding.etLastName.setText(userData!!.lastName)
            binding.etEmail.setText(userData!!.email)
            binding.etDateOfBirth.text = userData!!.dob.fromStrToDate()
            binding.etDistrict.text = userData!!.districtName
            binding.etCity.text = userData!!.cityName
            binding.etBlock.text = userData!!.blockName
            binding.etAddress.setText(userData!!.addressLine1)
            binding.etPinCode.setText(userData!!.pinCode.toString())
            when (userData!!.genderId) {
                1 -> {
                    binding.btnMale.performClick()
                }
                2 -> {
                    binding.btnFeMale.performClick()
                }
                3 -> {
                    binding.btnTg.performClick()
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
                findNavController().popBackStack()
            }
            R.id.etDateOfBirth -> {
                openDatePicker()
            }
            R.id.btnMale -> {
                genderId = "1"
                binding.btnMale.selectGender(
                    requireActivity(),
                    binding.btnFeMale,
                    binding.btnTg
                )
            }
            R.id.btnFeMale -> {
                genderId = "2"
                binding.btnFeMale.selectGender(
                    requireActivity(),
                    binding.btnMale,
                    binding.btnTg
                )
            }
            R.id.btnTg -> {
                genderId = "3"
                binding.btnTg.selectGender(
                    requireActivity(),
                    binding.btnFeMale,
                    binding.btnMale
                )
            }
            R.id.btnProceed -> {
                if (checkValidation()) {
                    editProfileAPI()
                }
            }
            R.id.etDistrict -> {
                masterViewModel.showDistrictListDialog(requireActivity(), ::districtClick)
            }
            R.id.etCity -> {
                showCityListDialog()
            }
            R.id.etBlock -> {
                showBlockList()
            }
        }
    }

    private fun editProfileAPI() {
        profileViewModel.updatePatientProfile(getRegistrationRequest())
            .observe(this) { registrationData ->
                if (registrationData.success) {
                    requireActivity().showToast(registrationData.message)
                    findNavController().popBackStack()
//                    PrefUtils.setLoginToken(requireActivity(), registrationData.token)
                }
            }
    }

    private fun getRegistrationRequest(): RegistrationRequest {
        return RegistrationRequest(
            firstName = binding.etFirstName.getTextFromEt(),
            lastName = binding.etLastName.getTextFromEt(),
            AddressLine1 = binding.etAddress.getTextFromEt(),
            email = binding.etEmail.getTextFromEt(),
            DOB = myCalendar?.time?.localToServerDate() ?: "",
            SourceId = 0,
            CountryId = 0,
            DistrictId = lastSelectedDistrict?.districtId ?: 0,
            CityId = lastSelectedCity?.cityId ?: 0,
            BlockId = 0,
            PatientStateId = 19,
            PinCode = binding.etPinCode.getTextFromEt(),
            Mobile = userData!!.mobile,
            GenderId = genderId
        )
    }

    private fun openDatePicker() {
        requireActivity().showDatePicker { mCalendar ->
            myCalendar = mCalendar
            binding.etDateOfBirth.text = myCalendar?.time?.formatDate() ?: ""
        }
    }

    private fun checkValidation(): Boolean {
        val isValid: Boolean = when {
            binding.etFirstName.getTextFromEt().isEmpty() -> {
                requireActivity().showToast(getString(R.string.please_enter_firstname))
                false
            }
            binding.etLastName.getTextFromEt().isEmpty() -> {
                requireActivity().showToast(getString(R.string.please_enter_lastname))
                false
            }

            !binding.etEmail.emailValidation() -> {
                requireActivity().showToast(getString(R.string.please_enter_valid_email))
                false
            }

            binding.etDateOfBirth.text.toString().isEmpty() -> {
                requireActivity().showToast(getString(R.string.please_enter_dob))
                false
            }

            genderId.isEmpty() -> {
                requireActivity().showToast(getString(R.string.select_gender))
                false
            }
            binding.etDistrict.text.toString().isEmpty() -> {
                requireActivity().showToast(getString(R.string.please_select_district))
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

    private fun districtClick(districtModelData: DistrictModelData, districtDialog: Dialog) {
        binding.etDistrict.text = districtModelData.districtName
        binding.etCity.text = ""
        binding.etBlock.text = ""
        lastSelectedDistrict = districtModelData
        districtDialog.dismiss()
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

    private fun showBlockList() {
        if (lastSelectedDistrict == null) {
            requireActivity().showToast(getString(R.string.select_district_first))
            return
        }
        masterViewModel.showBlockListDialog(
            lastSelectedDistrict!!.districtId,
            requireActivity(),
            ::blockClick
        )
    }

    private fun blockClick(blockModelData: BlockModelData, blockDialog: Dialog) {
        binding.etBlock.text = blockModelData.blockName
        lastSelectedBlock = blockModelData
        blockDialog.dismiss()
    }

}