package com.volumetree.newswasthyaingitopd.fragments.cho

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.databinding.FragmentCreatePatientChoBinding
import com.volumetree.newswasthyaingitopd.interfaces.PicDocumentSelector
import com.volumetree.newswasthyaingitopd.model.requestData.cho.AddPatientRequest
import com.volumetree.newswasthyaingitopd.model.responseData.cho.ChoPatientResponse
import com.volumetree.newswasthyaingitopd.model.responseData.comman.*
import com.volumetree.newswasthyaingitopd.model.responseData.patient.RelationData
import com.volumetree.newswasthyaingitopd.model.responseData.patient.SelectedDocData
import com.volumetree.newswasthyaingitopd.utils.*
import com.volumetree.newswasthyaingitopd.viewmodel.ChoViewModel
import com.volumetree.newswasthyaingitopd.viewmodel.MasterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddNewPatientChoFragment : Fragment(), View.OnClickListener, PicDocumentSelector {

    private var _binding: FragmentCreatePatientChoBinding? = null
    private val binding get() = _binding!!
    private val choViewModel: ChoViewModel by viewModel()
    private val masterViewModel: MasterViewModel by viewModel()

    private var lastSelectRelation: RelationData? = null
    private var lastMaritalStatus: MaritalData? = null
    private var lastBloodGroupData: BloodGroupData? = null
    private var genderId = 0
    private var lastSelectedDistrict: DistrictModelData? = null
    private var lastSelectedCity: CityModelData? = null
    private var lastSelectedBlock: BlockModelData? = null
    private val args: AddNewPatientChoFragmentArgs by navArgs()
    private var selectProfilePic: SelectedDocData? = null

    private var selectedYears = -1
    private var selectedMonths = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePatientChoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ChoHomeFragment.isAddMoreNewPatient = false
        activity?.showBottomNavigationView(false)
        binding.btnRegister.enableDisable(true)
        if (args.patientId != "0") {
            binding.layToolbar.tvActionbarTitle.text = getString(R.string.update_profile)
        } else {
            binding.layToolbar.tvActionbarTitle.text = getString(R.string.add_new_patient)
        }
        binding.layToolbar.imgBack.setOnClickListener(this)

        binding.btnRegister.setOnClickListener(this)
        binding.tvPatientRelation.setOnClickListener(this)
        binding.etYears.setOnClickListener(this)
        binding.etMonths.setOnClickListener(this)
        binding.btnMale.setOnClickListener(this)
        binding.btnFeMale.setOnClickListener(this)
        binding.btnTg.setOnClickListener(this)
        binding.etDistrict.setOnClickListener(this)
        binding.etCity.setOnClickListener(this)
        binding.etBlock.setOnClickListener(this)
        binding.etBloodGroup.setOnClickListener(this)
        binding.etMaritalStatus.setOnClickListener(this)
        binding.btnUploadProfilePicture.setOnClickListener(this)

        setupExistingData()
    }


    private fun setupExistingData() {
        if (args.patientId != "0") {
            val patientId = args.patientId
            choViewModel.getPatientProfile(patientId)
                .observeOnce(viewLifecycleOwner) { patientDataResponse ->
                    binding.btnRegister.text = getString(R.string.update)
                    val patientData = patientDataResponse.model
                    binding.etFirstName.setText(patientData.firstName)
                    binding.etLastName.setText(patientData.lastName)
                    binding.etEmail.setText(patientData.email)
                    binding.etMobile.setText(patientData.mobile)
                    binding.tvPatientRelation.text = patientData.guardian_Title
                    binding.etGuardianName.setText(patientData.guardian_Name)
                    binding.etAddress.setText(patientData.addressLine1)
                    binding.etAddress1.setText(patientData.addressLine2)
                    binding.etMaritalStatus.text = patientData.marital_Status
                    binding.etPinCode.setText(patientData.pincode)

                    selectedMonths = patientData.monthId
                    selectedYears = patientData.ageId
                    binding.etMonths.text = selectedMonths.toString()
                    binding.etYears.text = selectedYears.toString()
                    binding.imgPatientPic.loadImageURL(
                        binding.root.context,
                        patientData.imagePath,
                        user = true
                    )

                    lastSelectedDistrict = DistrictModelData(
                        districtName = patientData.districtName,
                        districtId = patientData.districtId
                    )

                    lastSelectedDistrict = DistrictModelData(
                        districtName = patientData.districtName,
                        districtId = patientData.districtId
                    )

                    lastSelectedCity =
                        CityModelData(
                            cityId = patientData.cityId,
                            cityName = patientData.cityName
                        )

                    lastBloodGroupData = BloodGroupData(
                        bloodGroupName = patientData.blood_group_Name,
                        bloodGroupId = patientData.blood_group_id
                    )

                    binding.etDistrict.text = patientData.districtName
                    binding.etCity.text = patientData.cityName
                    binding.etBloodGroup.text = patientData.blood_group_Name

                    genderId = patientData.genderId

                    when (patientData.genderId) {
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(clickedView: View) {
        when (clickedView.id) {
            R.id.btnUploadProfilePicture -> {
                CommonDialog.Companion.ShowImagePicker(
                    false, requireActivity(), this, this, 1
                )
                    .show(parentFragmentManager, Constants.FILE_PICKER)
            }

            R.id.imgBack -> {
                findNavController().popBackStack()
            }

            R.id.btnRegister -> {
                if (checkValidation()) {
                    if (args.patientId != "0") {
                        updatePatient()
                    } else {
                        addPatient()
                    }
                }
            }
            R.id.tvPatientRelation -> {
                requireActivity().showRelationDialog(true, ::relationClick)
            }
            R.id.etYears -> {
                requireActivity().showYearsDialog(::yearsSelect)
            }
            R.id.etMonths -> {
                requireActivity().showMonthsDialog(::monthsSelect)

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
            R.id.etDistrict -> {
                showDistrictListDialog()
            }
            R.id.etCity -> {
                showCityListDialog()
            }
            R.id.etBlock -> {
                showBlockList()
            }
            R.id.etBloodGroup -> {
                masterViewModel.showBloodGroupDialog(requireActivity(), ::bloodGroupClick)
            }
            R.id.etMaritalStatus -> {
                masterViewModel.showMaritalStatusDialog(requireActivity(), ::maritalClick)
            }
        }
    }

    private fun yearsSelect(years: String, dialog: Dialog) {
        selectedYears = years.toInt()
        binding.etYears.text = years
        dialog.dismiss()
    }

    private fun monthsSelect(months: String, dialog: Dialog) {
        selectedMonths = months.toInt()
        binding.etMonths.text = months
        dialog.dismiss()
    }

    private fun updatePatient() {

        val updateProfile = AddPatientRequest(
            patientInfoId = args.patientId,
            firstName = binding.etFirstName.getTextFromEt(),
            lastName = binding.etLastName.getTextFromEt(),
            email = binding.etEmail.getTextFromEt(),
            mobile = binding.etMobile.getTextFromEt(),
            genderID = genderId,
            guardian_Title = binding.tvPatientRelation.text.toString(),
            guardian_Name = binding.etGuardianName.getTextFromEt(),
            marital_Status = binding.etMaritalStatus.text.toString(),
            blood_group_id = lastBloodGroupData?.bloodGroupId ?: 0,
            addressLine1 = binding.etAddress.getTextFromEt(),
            addressLine2 = binding.etAddress1.getTextFromEt(),
            districtId = lastSelectedDistrict?.districtId ?: 0,
            cityId = lastSelectedCity?.cityId ?: 0,
            blockId = lastSelectedBlock?.blockId ?: 0,
            pinCode = binding.etPinCode.getTextFromEt(),
            age = (selectedYears * 12) + selectedMonths,
            ageId = selectedYears,
            monthId = selectedMonths
        )
        if (selectProfilePic != null) {
            updateProfile.fileFlag = "image/" + selectProfilePic?.fileFlag.toString()
            updateProfile.fileName = selectProfilePic?.imgName.toString()
            updateProfile.filePath = selectProfilePic?.base64 ?: ""
        }
        choViewModel.updatePatientProfile(updateProfile)
            .observeOnce(viewLifecycleOwner) { choPatientData ->
                if (choPatientData.success) {
                    requireActivity().showToast("Profile updated successfully")
                    findNavController().popBackStack()
                }
            }
    }

    private fun maritalClick(maritalData: MaritalData, dialog: Dialog) {
        lastMaritalStatus = maritalData
        dialog.dismiss()
        binding.etMaritalStatus.text = maritalData.typeName
    }

    private fun bloodGroupClick(bloodGroupData: BloodGroupData, dialog: Dialog) {
        lastBloodGroupData = bloodGroupData
        dialog.dismiss()
        binding.etBloodGroup.text = bloodGroupData.bloodGroupName
    }

    private fun addPatient() {
        val addPatientRequest = AddPatientRequest(
            firstName = binding.etFirstName.getTextFromEt(),
            lastName = binding.etLastName.getTextFromEt(),
            email = binding.etEmail.getTextFromEt(),
            mobile = binding.etMobile.getTextFromEt(),
            genderID = genderId,
            guardian_Title = binding.tvPatientRelation.text.toString(),
            guardian_Name = binding.etGuardianName.getTextFromEt(),
            marital_Status = binding.etMaritalStatus.text.toString(),
            blood_group_id = lastBloodGroupData?.bloodGroupId ?: 0,
            addressLine1 = binding.etAddress.getTextFromEt(),
            addressLine2 = binding.etAddress1.getTextFromEt(),
            districtId = lastSelectedDistrict?.districtId ?: 0,
            cityId = lastSelectedCity?.cityId ?: 0,
            blockId = lastSelectedBlock?.blockId ?: 0,
            pinCode = binding.etPinCode.getTextFromEt(),
            age = (selectedYears * 12) + selectedMonths,
            ageId = selectedYears,
            monthId = selectedMonths,
            profile_Pic = if (selectProfilePic != null) {
                selectProfilePic!!.base64
            } else {
                ""
            }
        )
        if (selectProfilePic != null) {

            addPatientRequest.fileFlag = "image/" + selectProfilePic?.fileFlag.toString()
            addPatientRequest.fileName = selectProfilePic?.imgName.toString()
            addPatientRequest.filePath = selectProfilePic?.base64 ?: ""
        }
        choViewModel.addPatient(addPatientRequest)
            .observeOnce(viewLifecycleOwner) { choPatientData ->
                if (choPatientData.success) {
                    CommonDialog.showPatientAddedSuccessDialog(
                        requireActivity(),
                        ::onCreateCase,
                        ::onAddMore, choPatientData
                    )
                } else {
                    requireActivity().showToast(choPatientData.message)
                }
            }
    }

    private fun onAddMore() {
        ChoHomeFragment.isAddMoreNewPatient = true
        findNavController().popBackStack()
    }

    private fun onCreateCase(choPatientData: ChoPatientResponse) {
        findNavController().navigate(
            AddNewPatientChoFragmentDirections.actionCreateCase(
                choPatientData.model, 0
            )
        )
    }

    private fun relationClick(relationData: RelationData, relationDialog: Dialog) {
        lastSelectRelation = relationData
        binding.tvPatientRelation.text = lastSelectRelation!!.name
        relationDialog.dismiss()
    }

    private fun showDistrictListDialog() {
        masterViewModel.showDistrictListDialog(requireActivity(), ::districtClick)
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
            genderId == 0 -> {
                requireActivity().showToast(getString(R.string.select_gender))
                false
            }

            binding.etYears.text.toString().isEmpty() -> {
                requireActivity().showToast(getString(R.string.please_enter_dob_year))
                false
            }

            binding.etMonths.text.toString().isEmpty() -> {
                requireActivity().showToast(getString(R.string.please_enter_dob_month))
                false
            }

            binding.tvPatientRelation.text.isEmpty() -> {
                requireActivity().showToast(getString(R.string.please_select_relation))
                false
            }

            binding.etGuardianName.getTextFromEt().isEmpty() -> {
                requireActivity().showToast(getString(R.string.please_enter_valid_guardian_name))
                false
            }

            !binding.etMobile.phoneNotMandatoryValidation() -> {
                requireActivity().showToast(getString(R.string.valid_enter_mobile_number))
                false
            }

            !binding.etEmail.emailValidation() -> {
                requireActivity().showToast(getString(R.string.please_enter_valid_email))
                false
            }

            binding.etAddress.getTextFromEt().isEmpty() -> {
                requireActivity().showToast(getString(R.string.please_enter_address))
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

//            binding.etPinCode.getTextFromEt().isEmpty() -> {
//                requireActivity().showToast(getString(R.string.please_enter_pincode))
//                false
//            }
//            binding.etPinCode.pinCodeValidation() -> {
//                requireActivity().showToast(getString(R.string.please_enter_six_digit_pincode))
//                false
//            }


            else -> {
                true
            }
        }
        return isValid

    }

    override fun onProfilePicSelected(selectedDocData: SelectedDocData, type: Int) {
        selectProfilePic = selectedDocData
        selectedDocData.bitmap?.let { binding.imgPatientPic.showImageAsBitmap(it) }
        parentFragmentManager.dismissFragmentDialog(Constants.FILE_PICKER)
    }

}


