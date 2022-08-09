package com.volumetree.newswasthyaingitopd.repository.patient

import androidx.lifecycle.MutableLiveData
import com.volumetree.newswasthyaingitopd.model.requestData.patient.MemberRequest
import com.volumetree.newswasthyaingitopd.model.requestData.patient.TokenRegistrationRequest
import com.volumetree.newswasthyaingitopd.model.responseData.comman.BaseResponse
import com.volumetree.newswasthyaingitopd.model.responseData.patient.FamilyMemberResponse

interface PatientRepository {
    fun registrationToken(tokenRegistrationRequest: TokenRegistrationRequest): MutableLiveData<BaseResponse>
    fun getFamilyMember(): MutableLiveData<FamilyMemberResponse>
    fun deleteFamilyMember(memberId: Int): MutableLiveData<BaseResponse>
    fun createFamilyMember(memberRequest: MemberRequest): MutableLiveData<BaseResponse>
    fun updateFamilyMember(memberRequest: MemberRequest): MutableLiveData<BaseResponse>

}