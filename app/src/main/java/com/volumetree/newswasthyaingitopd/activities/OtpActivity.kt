package com.volumetree.newswasthyaingitopd.activities

import `in`.aabhasjindal.otptextview.OTPListener
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.core.content.ContextCompat
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.activities.doctor.DoctorIntroActivity
import com.volumetree.newswasthyaingitopd.activities.patient.CreateProfileActivity
import com.volumetree.newswasthyaingitopd.activities.patient.DashBoardActivity
import com.volumetree.newswasthyaingitopd.databinding.ActivityOtpBinding
import com.volumetree.newswasthyaingitopd.enums.UserTypes
import com.volumetree.newswasthyaingitopd.model.requestData.doctor.VerifyDoctorOTPRequest
import com.volumetree.newswasthyaingitopd.model.requestData.patient.VerifyOTPRequest
import com.volumetree.newswasthyaingitopd.utils.Constants
import com.volumetree.newswasthyaingitopd.utils.PrefUtils
import com.volumetree.newswasthyaingitopd.utils.observeOnce
import com.volumetree.newswasthyaingitopd.utils.showToast
import com.volumetree.newswasthyaingitopd.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.DecimalFormat


class OtpActivity : BaseActivity(), View.OnClickListener {

    lateinit var otpBinding: ActivityOtpBinding
    var currentOTP = ""
    private var myTimer: CountDownTimer? = null
    private val loginViewModel: LoginViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        otpBinding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(otpBinding.root)

        otpBinding.tvOtpMobileNumber.text = "+91" + intent.getStringExtra(Constants.LOGIN_MOBILE)
        currentOTP = intent.getStringExtra(Constants.LOGIN_OTP).toString()
        otpBinding.tvSubTitle.text = otpBinding.tvSubTitle.text.toString() + " - " + currentOTP
        otpBinding.btnVerify.setOnClickListener(this)
        otpBinding.tvResend.setOnClickListener(this)
        otpBinding.tvOtpMobileNumber.setOnClickListener(this)
        otpBinding.backLay.imgBack.setOnClickListener(this)
        otpBinding.otpView.otpListener = object : OTPListener {
            override fun onInteractionListener() {
            }

            override fun onOTPComplete(otp: String) {
                if (otp == currentOTP) {
                    otpBinding.otpView.showSuccess()
                    otpBinding.btnVerify.isSelected = true
                    otpBinding.btnVerify.isEnabled = true
                } else {
                    otpBinding.otpView.showError()
                    otpBinding.btnVerify.isSelected = false
                    otpBinding.btnVerify.isEnabled = false
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        startOTPTimer()
    }

    private fun startOTPTimer() {
        myTimer?.cancel()
        myTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                otpBinding.tvTimer.text =
                    "00:${DecimalFormat("00").format(millisUntilFinished / 1000)} ${getString(R.string.sec)}"
            }

            override fun onFinish() {
                otpBinding.tvTimer.text = getString(R.string.zero_secs)
                otpBinding.tvResend.setTextColor(
                    ContextCompat.getColor(this@OtpActivity, R.color.login_title_color)
                )
                otpBinding.tvResend.isEnabled = true
            }
        }
        myTimer?.start()
    }

    override fun onPause() {
        super.onPause()
        myTimer?.cancel()
    }

    override fun onClick(clickedView: View) {
        when (clickedView.id) {
            R.id.btnVerify -> {
                val loginType = PrefUtils.getLoginUserType(this)
                if (loginType == Constants.PATIENT_TYPE) {
                    verifyOTP()
                } else if (loginType == UserTypes.DOCTOR.type) {
                    verifyDoctorOTP()
                }
            }
            R.id.tvResend -> {
                reSendOTP()
            }
            R.id.tvOtpMobileNumber -> {
                onBackPressed()
            }
            R.id.imgBack -> {
                onBackPressed()
            }
        }
    }

    private fun reSendOTP() {
        startOTPTimer()

        otpBinding.tvResend.setTextColor(
            ContextCompat.getColor(
                this@OtpActivity,
                R.color.edittext_hint
            )
        )
        otpBinding.tvResend.isEnabled = false
        loginViewModel.sendOTP(intent.getStringExtra(Constants.LOGIN_MOBILE).toString())
            .observeOnce(this) { otpResponse ->
                if (otpResponse.success) {
                    currentOTP = otpResponse.token
                    otpBinding.tvSubTitle.text =
                        otpBinding.tvSubTitle.text.toString() + " - " + currentOTP
                    this@OtpActivity.showToast(otpResponse.message)
                }
            }
    }

    private fun verifyOTP() {
        loginViewModel.verifyOTP(
            VerifyOTPRequest(
                mobile = intent.getStringExtra(Constants.LOGIN_MOBILE).toString(),
                otp = otpBinding.otpView.otp.toString()
            )
        ).observeOnce(this) { verifyOtpResponse ->
            var intentStart: Intent? = null
            if (verifyOtpResponse.success) {
                PrefUtils.setLoginToken(this@OtpActivity, verifyOtpResponse.token)
                intentStart = if (verifyOtpResponse.model.patientInfoId > 0) {
                    PrefUtils.setLogin(this@OtpActivity, 1)
                    Intent(this, DashBoardActivity::class.java)
                } else {
                    PrefUtils.setLogin(this@OtpActivity, 2)
                    Intent(
                        this,
                        CreateProfileActivity::class.java
                    ).putExtra(
                        Constants.LOGIN_MOBILE,
                        intent.getStringExtra(Constants.LOGIN_MOBILE).toString()
                    )
                }
            }

            if (intentStart != null) {
                intentStart.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intentStart)
                finish()
            }

        }
    }

    private fun verifyDoctorOTP() {

        val doctorInstituteData = PrefUtils.getDoctorInstituteData(this)
        if (doctorInstituteData != null) {
            loginViewModel.verifyDoctorOTP(
                VerifyDoctorOTPRequest(
                    username = intent.getStringExtra(Constants.LOGIN_MOBILE).toString(),
                    otp = otpBinding.otpView.otp.toString(),
                    institutionId = doctorInstituteData.institutionId.toString()
                )
            ).observeOnce(this) { verifyOtpResponse ->
                var intentStart: Intent? = null
                if (verifyOtpResponse.success) {
                    PrefUtils.setLogin(this@OtpActivity, 4)
                    PrefUtils.setLoginToken(this@OtpActivity, verifyOtpResponse.token)
                    PrefUtils.setDoctorData(this@OtpActivity,verifyOtpResponse.model)
                    intentStart = Intent(
                        this,
                        DoctorIntroActivity::class.java
                    )
                }

                if (intentStart != null) {
                    intentStart.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intentStart)
                    finish()
                }

            }
        }
    }
}