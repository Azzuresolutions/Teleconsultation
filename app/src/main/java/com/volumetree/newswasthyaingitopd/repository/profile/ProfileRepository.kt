package com.volumetree.newswasthyaingitopd.repository.profile

import androidx.lifecycle.MutableLiveData
import com.volumetree.newswasthyaingitopd.model.requestData.cho.UpdateChoRequest
import com.volumetree.newswasthyaingitopd.model.requestData.cho.UploadProfilePicRequest
import com.volumetree.newswasthyaingitopd.model.requestData.patient.RegistrationRequest
import com.volumetree.newswasthyaingitopd.model.responseData.cho.ChoProfileResponse
import com.volumetree.newswasthyaingitopd.model.responseData.comman.BaseResponse
import com.volumetree.newswasthyaingitopd.model.responseData.doctor.DoctorProfileResponse
import com.volumetree.newswasthyaingitopd.model.responseData.patient.PatientProfileResponse
import com.volumetree.newswasthyaingitopd.model.responseData.patient.RegistrationResponse

interface ProfileRepository {
    fun registration(registrationRequest: RegistrationRequest): MutableLiveData<RegistrationResponse>
    fun getPatientProfile(): MutableLiveData<PatientProfileResponse.PatientProfileResponse>
    fun updatePatientProfile(registrationRequest: RegistrationRequest): MutableLiveData<RegistrationResponse>

    fun getMemberProfile(): MutableLiveData<ChoProfileResponse.ChoProfileResponse>
    fun getDoctorProfile(): MutableLiveData<DoctorProfileResponse.DoctorProfileResponse>
    fun updateChoProfile(updateChoRequest: UpdateChoRequest): MutableLiveData<ChoProfileResponse.ChoProfileResponse>

}