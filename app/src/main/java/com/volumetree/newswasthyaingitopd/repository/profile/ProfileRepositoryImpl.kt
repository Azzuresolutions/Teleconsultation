package com.volumetree.newswasthyaingitopd.repository.profile

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.volumetree.newswasthyaingitopd.model.requestData.cho.UpdateChoRequest
import com.volumetree.newswasthyaingitopd.model.requestData.patient.RegistrationRequest
import com.volumetree.newswasthyaingitopd.model.responseData.cho.ChoProfileResponse
import com.volumetree.newswasthyaingitopd.model.responseData.doctor.DoctorProfileResponse
import com.volumetree.newswasthyaingitopd.model.responseData.patient.PatientProfileResponse
import com.volumetree.newswasthyaingitopd.model.responseData.patient.RegistrationResponse
import com.volumetree.newswasthyaingitopd.retrofit.ChoServices
import com.volumetree.newswasthyaingitopd.retrofit.CustomCB
import com.volumetree.newswasthyaingitopd.retrofit.PatientServices
import com.volumetree.newswasthyaingitopd.utils.isNetworkConnected

open class ProfileRepositoryImpl(
    private val retrofitService: PatientServices,
    private val choServices: ChoServices,
    val context: Context
) :
    ProfileRepository {
    override fun registration(registrationRequest: RegistrationRequest): MutableLiveData<RegistrationResponse> {
        val liveData = MutableLiveData<RegistrationResponse>()
        if (context.isNetworkConnected()) {
            retrofitService.registration(registrationRequest)
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        liveData.postValue(bodyResponse as RegistrationResponse)
                    }
                }, context))
        }
        return liveData
    }

    override fun getPatientProfile(): MutableLiveData<PatientProfileResponse.PatientProfileResponse> {
        val liveData = MutableLiveData<PatientProfileResponse.PatientProfileResponse>()
        if (context.isNetworkConnected()) {
            retrofitService.getPatientProfile()
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        liveData.postValue(bodyResponse as PatientProfileResponse.PatientProfileResponse)
                    }
                }, context))
        }
        return liveData
    }

    override fun updatePatientProfile(registrationRequest: RegistrationRequest): MutableLiveData<RegistrationResponse> {
        val liveData = MutableLiveData<RegistrationResponse>()
        if (context.isNetworkConnected()) {
            retrofitService.updatePatientProfile(registrationRequest)
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        liveData.postValue(bodyResponse as RegistrationResponse)
                    }
                }, context))
        }
        return liveData
    }

    override fun getMemberProfile(): MutableLiveData<ChoProfileResponse.ChoProfileResponse> {
        val liveData = MutableLiveData<ChoProfileResponse.ChoProfileResponse>()
        if (context.isNetworkConnected()) {
            choServices.getMemberProfile()
                .enqueue(
                    CustomCB(object : CustomCB.OnAPIResponse {
                        override fun onResponse(bodyResponse: Any?) {
                            liveData.postValue(bodyResponse as ChoProfileResponse.ChoProfileResponse)
                        }
                    }, context)
                )
        }
        return liveData
    }

    override fun getDoctorProfile(): MutableLiveData<DoctorProfileResponse.DoctorProfileResponse> {
        val liveData = MutableLiveData<DoctorProfileResponse.DoctorProfileResponse>()
        if (context.isNetworkConnected()) {
            choServices.getDoctorProfile()
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        liveData.postValue(bodyResponse as DoctorProfileResponse.DoctorProfileResponse)
                    }
                }, context))
        }
        return liveData
    }

    override fun updateChoProfile(updateChoRequest: UpdateChoRequest): MutableLiveData<ChoProfileResponse.ChoProfileResponse> {
        val liveData = MutableLiveData<ChoProfileResponse.ChoProfileResponse>()
        if (context.isNetworkConnected()) {
            choServices.updateChoProfile(updateChoRequest)
                .enqueue(
                    CustomCB(object : CustomCB.OnAPIResponse {
                        override fun onResponse(bodyResponse: Any?) {
                            liveData.postValue(bodyResponse as ChoProfileResponse.ChoProfileResponse)
                        }
                    }, context)
                )
        }
        return liveData
    }

}