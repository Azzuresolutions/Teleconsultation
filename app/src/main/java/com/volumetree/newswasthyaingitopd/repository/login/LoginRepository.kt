package com.volumetree.newswasthyaingitopd.repository.login

import androidx.lifecycle.MutableLiveData
import com.volumetree.newswasthyaingitopd.model.requestData.cho.SingInRequest
import com.volumetree.newswasthyaingitopd.model.requestData.doctor.VerifyDoctorOTPRequest
import com.volumetree.newswasthyaingitopd.model.requestData.patient.VerifyOTPRequest
import com.volumetree.newswasthyaingitopd.model.responseData.cho.ChoProfileResponse
import com.volumetree.newswasthyaingitopd.model.responseData.doctor.DoctorOTPResponse
import com.volumetree.newswasthyaingitopd.model.responseData.doctor.DoctorProfileResponse
import com.volumetree.newswasthyaingitopd.model.responseData.patient.OTPResponse
import com.volumetree.newswasthyaingitopd.model.responseData.patient.VerifyOtpResponse

interface LoginRepository {
    fun sendOTP(strMobile: String): MutableLiveData<OTPResponse>
    fun verifyOTP(verifyOTPRequest: VerifyOTPRequest): MutableLiveData<VerifyOtpResponse.VerifyOtpResponse>

    fun signInCHO(singInRequest: SingInRequest): MutableLiveData<ChoProfileResponse.ChoProfileResponse>

    fun sendDoctorOTP(strMobile: String): MutableLiveData<DoctorOTPResponse>
    fun verifyDoctorOTP(verifyOTPRequest: VerifyDoctorOTPRequest): MutableLiveData<DoctorProfileResponse.DoctorProfileResponse>
}