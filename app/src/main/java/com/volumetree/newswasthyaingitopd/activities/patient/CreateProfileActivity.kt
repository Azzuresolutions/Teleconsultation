package com.volumetree.newswasthyaingitopd.activities.patient

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.activities.BaseActivity
import com.volumetree.newswasthyaingitopd.databinding.ActivityCreateProfileBinding
import com.volumetree.newswasthyaingitopd.model.requestData.patient.RegistrationRequest
import com.volumetree.newswasthyaingitopd.model.responseData.comman.BlockModelData
import com.volumetree.newswasthyaingitopd.model.responseData.comman.CityModelData
import com.volumetree.newswasthyaingitopd.model.responseData.comman.DistrictModelData
import com.volumetree.newswasthyaingitopd.utils.*
import com.volumetree.newswasthyaingitopd.viewmodel.MasterViewModel
import com.volumetree.newswasthyaingitopd.viewmodel.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class CreateProfileActivity : BaseActivity(), View.OnClickListener {

    private lateinit var createBinding: ActivityCreateProfileBinding
    private lateinit var myCalendar: Calendar
    private var genderId = ""
    private val masterViewModel: MasterViewModel by viewModel()
    private val profileViewModel: ProfileViewModel by viewModel()

    private var lastSelectedDistrict: DistrictModelData? = null
    private var lastSelectedCity: CityModelData? = null
    private var lastSelectedBlock: BlockModelData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createBinding = ActivityCreateProfileBinding.inflate(layoutInflater)
        setContentView(createBinding.root)

        createBinding.layToolbar.tvActionbarTitle.text = title
        createBinding.btnProceed.isSelected = true

        createBinding.btnProceed.setOnClickListener(this)
        createBinding.etDateOfBirth.setOnClickListener(this)
        createBinding.btnMale.setOnClickListener(this)
        createBinding.btnFeMale.setOnClickListener(this)
        createBinding.btnTg.setOnClickListener(this)
        createBinding.etDistrict.setOnClickListener(this)
        createBinding.etCity.setOnClickListener(this)
        createBinding.etBlock.setOnClickListener(this)
    }

    override fun onClick(clickedView: View) {
        when (clickedView.id) {
            R.id.btnProceed -> {
                if (checkValidation()) {
                    registrationAPI()
                }
            }
            R.id.etDateOfBirth -> {
                openDatePicker()
            }
            R.id.btnMale -> {
                genderId = "1"
                createBinding.btnMale.selectGender(
                    this,
                    createBinding.btnFeMale,
                    createBinding.btnTg
                )
            }
            R.id.btnFeMale -> {
                genderId = "2"
                createBinding.btnFeMale.selectGender(
                    this,
                    createBinding.btnMale,
                    createBinding.btnTg
                )
            }
            R.id.btnTg -> {
                genderId = "3"
                createBinding.btnTg.selectGender(
                    this,
                    createBinding.btnFeMale,
                    createBinding.btnMale
                )
            }
            R.id.etDistrict -> {
                masterViewModel.showDistrictListDialog(this, ::districtClick)
            }
            R.id.etCity -> {
                if (lastSelectedDistrict == null) {
                    showToast(getString(R.string.select_district_first))
                    return
                }
                masterViewModel.showCityListDialog(
                    lastSelectedDistrict!!.districtId,
                    this,
                    ::cityClick
                )
            }
            R.id.etBlock -> {
                if (lastSelectedDistrict == null) {
                    showToast(getString(R.string.select_district_first))
                    return
                }
                masterViewModel.showBlockListDialog(
                    lastSelectedDistrict!!.districtId,
                    this,
                    ::blockClick
                )
            }
        }
    }

    private fun registrationAPI() {

        profileViewModel.registration(getRegistrationRequest())
            .observeOnce(this) { registrationData ->
                if (registrationData.success) {
                    showToast(registrationData.message)
                    PrefUtils.setLogin(this, 1)
                    PrefUtils.setLoginToken(this, registrationData.token)
                    startActivity(Intent(this, DashBoardActivity::class.java))
                    finish()
                }
            }

    }

    private fun getRegistrationRequest(): RegistrationRequest {
        return RegistrationRequest(
            firstName = createBinding.etFirstName.getTextFromEt(),
            lastName = createBinding.etLastName.getTextFromEt(),
            AddressLine1 = createBinding.etAddress.getTextFromEt(),
            email = createBinding.etEmail.getTextFromEt(),
            DOB = myCalendar.time.localToServerDate(),
            SourceId = 0,
            CountryId = 0,
            DistrictId = lastSelectedDistrict?.districtId ?: 0,
            CityId = lastSelectedCity?.cityId ?: 0,
            BlockId = lastSelectedBlock?.blockId ?: 0,
            PatientStateId = 19,
            PinCode = createBinding.etPinCode.getTextFromEt(),
            Mobile = intent.getStringExtra(Constants.LOGIN_MOBILE).toString(),
            GenderId = genderId
        )
    }

    private fun openDatePicker() {
        showDatePicker { mCalendar ->
            myCalendar = mCalendar
            createBinding.etDateOfBirth.text = myCalendar.time.formatDate()
        }
    }

    private fun checkValidation(): Boolean {
        val isValid: Boolean = when {
            createBinding.etFirstName.getTextFromEt().isEmpty() -> {
                showToast(getString(R.string.please_enter_firstname))
                false
            }
            createBinding.etLastName.getTextFromEt().isEmpty() -> {
                showToast(getString(R.string.please_enter_lastname))
                false
            }

            !createBinding.etEmail.emailValidation() -> {
                showToast(getString(R.string.please_enter_valid_email))
                false
            }

            createBinding.etDateOfBirth.text.toString().isEmpty() -> {
                showToast(getString(R.string.please_enter_dob))
                false
            }

            genderId.isEmpty() -> {
                showToast(getString(R.string.select_gender))
                false
            }
            createBinding.etDistrict.text.toString().isEmpty() -> {
                showToast(getString(R.string.please_select_district))
                false
            }
            createBinding.etCity.text.toString().isEmpty() -> {
                showToast(getString(R.string.please_select_city))
                false
            }

            createBinding.etAddress.getTextFromEt().isEmpty() -> {
                showToast(getString(R.string.please_enter_address))
                false
            }

            createBinding.etPinCode.pinCodeValidation() -> {
                showToast(getString(R.string.please_enter_pincode))
                false
            }

            else -> {
                true
            }
        }
        return isValid

    }

    private fun districtClick(districtModelData: DistrictModelData, districtDialog: Dialog) {
        createBinding.etDistrict.text = districtModelData.districtName
        createBinding.etCity.text = ""
        createBinding.etBlock.text = ""
        lastSelectedDistrict = districtModelData
        districtDialog.dismiss()
    }

    private fun cityClick(cityModelData: CityModelData, cityDialog: Dialog) {
        createBinding.etCity.text = cityModelData.cityName
        lastSelectedCity = cityModelData
        cityDialog.dismiss()
    }

    private fun blockClick(blockModelData: BlockModelData, blockDialog: Dialog) {
        createBinding.etBlock.text = blockModelData.blockName
        lastSelectedBlock = blockModelData
        blockDialog.dismiss()
    }

}