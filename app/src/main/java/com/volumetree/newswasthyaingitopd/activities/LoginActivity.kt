package com.volumetree.newswasthyaingitopd.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.databinding.ActivityLoginBinding
import com.volumetree.newswasthyaingitopd.enums.UserTypes
import com.volumetree.newswasthyaingitopd.utils.*
import com.volumetree.newswasthyaingitopd.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity(), TextWatcher {
    private lateinit var loginBinding: ActivityLoginBinding
    private var isValidMobile = false
    private val loginViewModel: LoginViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        loginBinding.etCountryCode.addTextChangedListener(this)
        loginBinding.etMobileNumber.addTextChangedListener(this)
        loginBinding.backLay.imgBack.setOnClickListener {
            onBackPressed()
        }
        loginBinding.btnContinue.setOnClickListener {
            if (isValidMobile) {
                val mobileNo = loginBinding.etMobileNumber.getTextFromEt()
                if (PrefUtils.getLoginUserType(this@LoginActivity) == UserTypes.DOCTOR.type
                ) {
                    loginViewModel.sendDoctorOTP(mobileNo).observeOnce(this) { otpResponse ->
                        if (otpResponse.success) {
                            this@LoginActivity.showToast(otpResponse.message)
                            PrefUtils.setDoctorInstituteData(this, otpResponse.model)
                            startActivity(
                                Intent(
                                    this,
                                    OtpActivity::class.java
                                ).putExtra(
                                    Constants.LOGIN_MOBILE,
                                    loginBinding.etMobileNumber.text.toString()
                                ).putExtra(
                                    Constants.LOGIN_OTP,
                                    otpResponse.token
                                )
                            )
                        }
                    }
                } else {
                    loginViewModel.sendOTP(mobileNo).observeOnce(this) { otpResponse ->
                        if (otpResponse.success) {
                            this@LoginActivity.showToast(otpResponse.message)
                            startActivity(
                                Intent(
                                    this,
                                    OtpActivity::class.java
                                ).putExtra(
                                    Constants.LOGIN_MOBILE,
                                    loginBinding.etMobileNumber.text.toString()
                                ).putExtra(
                                    Constants.LOGIN_OTP,
                                    otpResponse.token
                                )
                            )
                        }
                    }

                }

            } else {
                if (loginBinding.etMobileNumber.getTextFromEt().length < 10) {
                    showToast(getString(R.string.enter_ten_digit_number))
                }
            }
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        checkMobileValidation()
    }

    override fun afterTextChanged(p0: Editable?) {

    }


    private fun checkMobileValidation() {
        val countryCode = loginBinding.etCountryCode.text.toString()
        val mobileNumber = loginBinding.etMobileNumber.text.toString()
        isValidMobile = if (countryCode.isNotEmpty() && mobileNumber.isNotEmpty()) {
            if (!countryCode.startsWith("+")) {
                false
            } else {
                mobileNumber.length == 10
            }
        } else {
            false
        }
        loginBinding.btnContinue.isSelected = isValidMobile
    }
}