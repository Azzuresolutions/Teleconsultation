package com.volumetree.newswasthyaingitopd.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.volumetree.newswasthyaingitopd.model.requestData.cho.SingInRequest
import com.volumetree.newswasthyaingitopd.model.requestData.doctor.VerifyDoctorOTPRequest
import com.volumetree.newswasthyaingitopd.model.requestData.patient.VerifyOTPRequest
import com.volumetree.newswasthyaingitopd.model.responseData.cho.ChoProfileResponse
import com.volumetree.newswasthyaingitopd.model.responseData.doctor.DoctorOTPResponse
import com.volumetree.newswasthyaingitopd.model.responseData.doctor.DoctorProfileResponse
import com.volumetree.newswasthyaingitopd.model.responseData.patient.OTPResponse
import com.volumetree.newswasthyaingitopd.model.responseData.patient.VerifyOtpResponse
import com.volumetree.newswasthyaingitopd.repository.login.LoginRepository

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    fun sendOTP(strMobile: String): MutableLiveData<OTPResponse> {
        return loginRepository.sendOTP(strMobile)
    }

    fun verifyOTP(verifyOTPRequest: VerifyOTPRequest): MutableLiveData<VerifyOtpResponse.VerifyOtpResponse> {
        return loginRepository.verifyOTP(verifyOTPRequest)
    }

    fun signInCHO(singInRequest: SingInRequest): MutableLiveData<ChoProfileResponse.ChoProfileResponse> {
        return loginRepository.signInCHO(singInRequest)
    }

    fun sendDoctorOTP(strMobile: String): MutableLiveData<DoctorOTPResponse> {
        return loginRepository.sendDoctorOTP(strMobile)
    }

    fun verifyDoctorOTP(verifyOTPRequest: VerifyDoctorOTPRequest): MutableLiveData<DoctorProfileResponse.DoctorProfileResponse> {
        return loginRepository.verifyDoctorOTP(verifyOTPRequest)
    }

}