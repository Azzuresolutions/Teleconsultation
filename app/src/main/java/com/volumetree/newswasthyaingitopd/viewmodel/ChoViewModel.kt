package com.volumetree.newswasthyaingitopd.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.volumetree.newswasthyaingitopd.model.requestData.cho.*
import com.volumetree.newswasthyaingitopd.model.responseData.cho.CaseResponse
import com.volumetree.newswasthyaingitopd.model.responseData.cho.ChoPatientResponse
import com.volumetree.newswasthyaingitopd.model.responseData.cho.DoctorQueueData
import com.volumetree.newswasthyaingitopd.model.responseData.cho.DraftConsultationResponse
import com.volumetree.newswasthyaingitopd.model.responseData.comman.BaseResponse
import com.volumetree.newswasthyaingitopd.repository.cho.ChoRepository

class ChoViewModel(private val choRepository: ChoRepository) : ViewModel() {

    fun addPatient(addPatientRequest: AddPatientRequest): MutableLiveData<ChoPatientResponse> {
        return choRepository.addPatient(addPatientRequest)
    }

    fun updatePatientProfile(addPatientRequest: AddPatientRequest): MutableLiveData<ChoPatientResponse> {
        return choRepository.updatePatientProfile(addPatientRequest)
    }

    fun getPatientProfile(patientId: String): MutableLiveData<ChoPatientResponse> {
        return choRepository.getPatientProfile(patientId)
    }

    fun changePassword(changePasswordRequest: ChangePasswordRequest): MutableLiveData<BaseResponse> {
        return choRepository.changePassword(changePasswordRequest)
    }

    fun getPatientList(
        searchText: String,
        recordLimit: Int,
        skipRecords: Int,
        isLoadAll: Boolean
    ): MutableLiveData<ChoPatientResponse> {
        return choRepository.getPatientList(searchText, recordLimit, skipRecords, isLoadAll)
    }

    fun getCaseList(
        caseRequest: GetCaseRequest
    ): MutableLiveData<CaseResponse> {
        return choRepository.getCaseList(caseRequest)
    }

    fun draftConsultation(draftConsultationRequest: DraftConsultationRequest): MutableLiveData<DraftConsultationResponse> {
        return choRepository.dratConsultation(draftConsultationRequest)
    }

    fun updateConsultation(draftConsultationRequest: DraftConsultationRequest2): MutableLiveData<DraftConsultationResponse> {
        return choRepository.updateConsultation(draftConsultationRequest)
    }

    fun getConsultation(consultationId: Int): MutableLiveData<DraftConsultationResponse> {
        return choRepository.getConsultation(consultationId)
    }

    fun createFamilyMember(memberRequest: ChoMemberRequest): MutableLiveData<ChoPatientResponse> {
        return choRepository.createFamilyMember(memberRequest)
    }

    fun updateFamilyMember(memberRequest: ChoMemberRequest): MutableLiveData<ChoPatientResponse> {
        return choRepository.updateFamilyMember(memberRequest)
    }

    fun deleteFamilyMember(
        patientInfoId: String,
        familyMemberId: String
    ): MutableLiveData<BaseResponse> {
        return choRepository.deleteFamilyMember(patientInfoId, familyMemberId)
    }

    fun getFamilyMembers(patientInfoId: String): MutableLiveData<ChoPatientResponse> {
        return choRepository.getFamilyMembers(patientInfoId)
    }

    fun getPatientQueueListByDoctor(doctorId: Int): MutableLiveData<DoctorQueueData.DoctorQueueData> {
        return choRepository.getPatientQueueListByDoctor(doctorId)
    }

}