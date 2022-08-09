package com.volumetree.newswasthyaingitopd.repository.cho

import androidx.lifecycle.MutableLiveData
import com.volumetree.newswasthyaingitopd.model.requestData.cho.*
import com.volumetree.newswasthyaingitopd.model.responseData.cho.CaseResponse
import com.volumetree.newswasthyaingitopd.model.responseData.cho.ChoPatientResponse
import com.volumetree.newswasthyaingitopd.model.responseData.cho.DoctorQueueData
import com.volumetree.newswasthyaingitopd.model.responseData.cho.DraftConsultationResponse
import com.volumetree.newswasthyaingitopd.model.responseData.comman.BaseResponse

interface ChoRepository {
    fun addPatient(addPatientRequest: AddPatientRequest): MutableLiveData<ChoPatientResponse>
    fun updatePatientProfile(addPatientRequest: AddPatientRequest): MutableLiveData<ChoPatientResponse>
    fun getPatientProfile(patientId: String): MutableLiveData<ChoPatientResponse>
    fun changePassword(changePasswordRequest: ChangePasswordRequest): MutableLiveData<BaseResponse>
    fun dratConsultation(draftConsultationRequest: DraftConsultationRequest): MutableLiveData<DraftConsultationResponse>
    fun updateConsultation(draftConsultationRequest: DraftConsultationRequest2): MutableLiveData<DraftConsultationResponse>
    fun getConsultation(consultationId: Int): MutableLiveData<DraftConsultationResponse>
    fun createFamilyMember(memberRequest: ChoMemberRequest): MutableLiveData<ChoPatientResponse>
    fun updateFamilyMember(memberRequest: ChoMemberRequest): MutableLiveData<ChoPatientResponse>
    fun deleteFamilyMember(
        patientInfoId: String,
        familyMemberId: String
    ): MutableLiveData<BaseResponse>

    fun getFamilyMembers(patientInfoId: String): MutableLiveData<ChoPatientResponse>
    fun getPatientList(
        searchText: String, recordLimit: Int,
        skipRecords: Int, isLoadAll: Boolean
    ): MutableLiveData<ChoPatientResponse>

    fun getCaseList(
        caseRequest: GetCaseRequest
    ): MutableLiveData<CaseResponse>

    fun getPatientQueueListByDoctor(
        doctorId: Int
    ): MutableLiveData<DoctorQueueData.DoctorQueueData>
}