package com.volumetree.newswasthyaingitopd.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.volumetree.newswasthyaingitopd.model.requestData.cho.UpdateChoRequest
import com.volumetree.newswasthyaingitopd.model.requestData.cho.UploadProfilePicRequest
import com.volumetree.newswasthyaingitopd.model.requestData.patient.RegistrationRequest
import com.volumetree.newswasthyaingitopd.model.responseData.cho.ChoProfileResponse
import com.volumetree.newswasthyaingitopd.model.responseData.doctor.DoctorProfileResponse
import com.volumetree.newswasthyaingitopd.model.responseData.patient.PatientProfileResponse
import com.volumetree.newswasthyaingitopd.model.responseData.patient.RegistrationResponse
import com.volumetree.newswasthyaingitopd.repository.profile.ProfileRepository

class ProfileViewModel(private val profileRepository: ProfileRepository) : ViewModel() {
    fun registration(registrationRequest: RegistrationRequest): MutableLiveData<RegistrationResponse> {
        return profileRepository.registration(registrationRequest)
    }

    fun getPatientProfile(): MutableLiveData<PatientProfileResponse.PatientProfileResponse> {
        return profileRepository.getPatientProfile()
    }

    fun updatePatientProfile(registrationRequest: RegistrationRequest): MutableLiveData<RegistrationResponse> {
        return profileRepository.updatePatientProfile(registrationRequest)
    }

    fun getMemberProfile(): MutableLiveData<ChoProfileResponse.ChoProfileResponse> {
        return profileRepository.getMemberProfile()
    }
   fun getDoctorProfile(): MutableLiveData<DoctorProfileResponse.DoctorProfileResponse> {
        return profileRepository.getDoctorProfile()
    }

    fun updateChoProfile(updateChoRequest: UpdateChoRequest): MutableLiveData<ChoProfileResponse.ChoProfileResponse> {
        return profileRepository.updateChoProfile(updateChoRequest)
    }
}