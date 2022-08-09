package com.volumetree.newswasthyaingitopd.repository.login

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.volumetree.newswasthyaingitopd.model.requestData.cho.SingInRequest
import com.volumetree.newswasthyaingitopd.model.requestData.doctor.VerifyDoctorOTPRequest
import com.volumetree.newswasthyaingitopd.model.requestData.patient.SendOTPRequest
import com.volumetree.newswasthyaingitopd.model.requestData.patient.VerifyOTPRequest
import com.volumetree.newswasthyaingitopd.model.responseData.cho.ChoProfileResponse
import com.volumetree.newswasthyaingitopd.model.responseData.doctor.DoctorOTPResponse
import com.volumetree.newswasthyaingitopd.model.responseData.doctor.DoctorProfileResponse
import com.volumetree.newswasthyaingitopd.model.responseData.patient.OTPResponse
import com.volumetree.newswasthyaingitopd.model.responseData.patient.VerifyOtpResponse
import com.volumetree.newswasthyaingitopd.retrofit.ChoServices
import com.volumetree.newswasthyaingitopd.retrofit.CustomCB
import com.volumetree.newswasthyaingitopd.retrofit.DoctorServices
import com.volumetree.newswasthyaingitopd.retrofit.PatientServices
import com.volumetree.newswasthyaingitopd.utils.isNetworkConnected

open class LoginRepositoryImpl(
    private val retrofitService: PatientServices,
    private val choServices: ChoServices,
    private val doctorServices: DoctorServices,
    val context: Context
) :
    LoginRepository {

    override fun sendOTP(strMobile: String): MutableLiveData<OTPResponse> {
        val otpLiveData = MutableLiveData<OTPResponse>()
        if (context.isNetworkConnected()) {
            val sendOTPRequest = SendOTPRequest(strMobile)
            retrofitService.sendOTP(sendOTPRequest)
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        otpLiveData.postValue(bodyResponse as OTPResponse)
                    }
                }, context))
        }
        return otpLiveData
    }

    override fun sendDoctorOTP(strMobile: String): MutableLiveData<DoctorOTPResponse> {
        val otpLiveData = MutableLiveData<DoctorOTPResponse>()
        if (context.isNetworkConnected()) {
            val sendOTPRequest =
                com.volumetree.newswasthyaingitopd.model.requestData.doctor.SendOTPRequest(strMobile)
            doctorServices.sendOTP(sendOTPRequest)
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        otpLiveData.postValue(bodyResponse as DoctorOTPResponse)
                    }
                }, context))
        }
        return otpLiveData
    }

    override fun verifyOTP(verifyOTPRequest: VerifyOTPRequest): MutableLiveData<VerifyOtpResponse.VerifyOtpResponse> {
        val otpLiveData = MutableLiveData<VerifyOtpResponse.VerifyOtpResponse>()
        if (context.isNetworkConnected()) {
            retrofitService.verifyOTP(verifyOTPRequest)
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        otpLiveData.postValue(bodyResponse as VerifyOtpResponse.VerifyOtpResponse)
                    }
                }, context))
        }
        return otpLiveData
    }

    override fun verifyDoctorOTP(verifyOTPRequest: VerifyDoctorOTPRequest): MutableLiveData<DoctorProfileResponse.DoctorProfileResponse> {
        val otpLiveData = MutableLiveData<DoctorProfileResponse.DoctorProfileResponse>()
        if (context.isNetworkConnected()) {
            doctorServices.verifyOTP(verifyOTPRequest)
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        otpLiveData.postValue(bodyResponse as DoctorProfileResponse.DoctorProfileResponse)
                    }
                }, context))
        }
        return otpLiveData
    }

    override fun signInCHO(singInRequest: SingInRequest): MutableLiveData<ChoProfileResponse.ChoProfileResponse> {
        val responseLiveData = MutableLiveData<ChoProfileResponse.ChoProfileResponse>()
        if (context.isNetworkConnected()) {
            choServices.signInCHO(singInRequest)
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        responseLiveData.postValue(bodyResponse as ChoProfileResponse.ChoProfileResponse)
                    }
                }, context))
        }
        return responseLiveData
    }


}