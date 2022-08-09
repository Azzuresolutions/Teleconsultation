package com.volumetree.newswasthyaingitopd.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.volumetree.newswasthyaingitopd.model.requestData.patient.MemberRequest
import com.volumetree.newswasthyaingitopd.model.requestData.patient.TokenRegistrationRequest
import com.volumetree.newswasthyaingitopd.model.responseData.comman.BaseResponse
import com.volumetree.newswasthyaingitopd.model.responseData.patient.FamilyMemberResponse
import com.volumetree.newswasthyaingitopd.repository.patient.PatientRepository

class PatientViewModel(private val patientRepository: PatientRepository) : ViewModel() {

    fun registrationToken(tokenRegistrationRequest: TokenRegistrationRequest): MutableLiveData<BaseResponse> {
        return patientRepository.registrationToken(tokenRegistrationRequest)
    }

    fun getFamilyMember(): MutableLiveData<FamilyMemberResponse> {
        return patientRepository.getFamilyMember()
    }

    fun createFamilyMember(memberRequest: MemberRequest): MutableLiveData<BaseResponse> {
        return patientRepository.createFamilyMember(memberRequest)
    }

    fun updateFamilyMember(memberRequest: MemberRequest): MutableLiveData<BaseResponse> {
        return patientRepository.updateFamilyMember(memberRequest)
    }

    fun deleteFamilyMember(memberId: Int): MutableLiveData<BaseResponse> {
        return patientRepository.deleteFamilyMember(memberId)
    }

}