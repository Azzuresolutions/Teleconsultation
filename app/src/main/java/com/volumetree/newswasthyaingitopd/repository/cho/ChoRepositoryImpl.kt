package com.volumetree.newswasthyaingitopd.repository.cho

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.volumetree.newswasthyaingitopd.model.requestData.cho.*
import com.volumetree.newswasthyaingitopd.model.responseData.cho.CaseResponse
import com.volumetree.newswasthyaingitopd.model.responseData.cho.ChoPatientResponse
import com.volumetree.newswasthyaingitopd.model.responseData.cho.DoctorQueueData
import com.volumetree.newswasthyaingitopd.model.responseData.cho.DraftConsultationResponse
import com.volumetree.newswasthyaingitopd.model.responseData.comman.BaseResponse
import com.volumetree.newswasthyaingitopd.retrofit.ChoServices
import com.volumetree.newswasthyaingitopd.retrofit.CustomCB
import com.volumetree.newswasthyaingitopd.utils.isNetworkConnected

open class ChoRepositoryImpl(private val choServices: ChoServices, val context: Context) :
    ChoRepository {

    override fun addPatient(addPatientRequest: AddPatientRequest): MutableLiveData<ChoPatientResponse> {
        val responseData = MutableLiveData<ChoPatientResponse>()
        if (context.isNetworkConnected()) {
            choServices.addPatient(addPatientRequest)
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        responseData.postValue(bodyResponse as ChoPatientResponse)
                    }
                }, context))
        }
        return responseData
    }

    override fun updatePatientProfile(addPatientRequest: AddPatientRequest): MutableLiveData<ChoPatientResponse> {
        val responseData = MutableLiveData<ChoPatientResponse>()
        if (context.isNetworkConnected()) {
            choServices.updatePatientProfile(addPatientRequest)
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        responseData.postValue(bodyResponse as ChoPatientResponse)
                    }
                }, context))
        }
        return responseData
    }

    override fun getPatientProfile(patientId: String): MutableLiveData<ChoPatientResponse> {
        val responseData = MutableLiveData<ChoPatientResponse>()
        if (context.isNetworkConnected()) {
            choServices.getChoPatient(patientId)
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        responseData.postValue(bodyResponse as ChoPatientResponse)
                    }
                }, context))
        }
        return responseData
    }

    override fun changePassword(changePasswordRequest: ChangePasswordRequest): MutableLiveData<BaseResponse> {
        val responseData = MutableLiveData<BaseResponse>()
        if (context.isNetworkConnected()) {
            choServices.changePassword(changePasswordRequest)
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        responseData.postValue(bodyResponse as BaseResponse)
                    }
                }, context))
        }
        return responseData
    }

    override fun getPatientList(
        searchText: String,
        recordLimit: Int,
        skipRecords: Int,
        isLoadAll: Boolean
    ): MutableLiveData<ChoPatientResponse> {
        val responseData = MutableLiveData<ChoPatientResponse>()
        if (context.isNetworkConnected()) {
            if (searchText.isEmpty()) {
                choServices.getChoPatientList(isLoadAll, skipRecords, recordLimit)
            } else {
                choServices.getChoPatientList(isLoadAll, searchText, skipRecords, recordLimit)
            }.enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                override fun onResponse(bodyResponse: Any?) {
                    responseData.postValue(bodyResponse as ChoPatientResponse)
                }
            }, context))
        }
        return responseData
    }

    override fun getCaseList(
        caseRequest: GetCaseRequest
    ): MutableLiveData<CaseResponse> {
        val responseData = MutableLiveData<CaseResponse>()
        if (context.isNetworkConnected()) {
            choServices.getCaseList(caseRequest)
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        responseData.postValue(bodyResponse as CaseResponse)
                    }
                }, context))
        }
        return responseData
    }

    override fun getPatientQueueListByDoctor(doctorId: Int): MutableLiveData<DoctorQueueData.DoctorQueueData> {
        val responseData = MutableLiveData<DoctorQueueData.DoctorQueueData>()
        if (context.isNetworkConnected()) {
            choServices.getPatientQueueListByDoctor(doctorId)
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        responseData.postValue(bodyResponse as DoctorQueueData.DoctorQueueData)
                    }
                }, context))
        }
        return responseData
    }

    override fun dratConsultation(draftConsultationRequest: DraftConsultationRequest): MutableLiveData<DraftConsultationResponse> {
        val responseData = MutableLiveData<DraftConsultationResponse>()
        if (context.isNetworkConnected()) {
            choServices.draftConsultation(draftConsultationRequest)
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        responseData.postValue(bodyResponse as DraftConsultationResponse)
                    }
                }, context))
        }
        return responseData
    }

    override fun updateConsultation(draftConsultationRequest: DraftConsultationRequest2): MutableLiveData<DraftConsultationResponse> {
        val responseData = MutableLiveData<DraftConsultationResponse>()
        if (context.isNetworkConnected()) {
            choServices.updateConsultation(draftConsultationRequest)
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        responseData.postValue(bodyResponse as DraftConsultationResponse)
                    }
                }, context))
        }
        return responseData
    }

    override fun getConsultation(consultationId: Int): MutableLiveData<DraftConsultationResponse> {
        val responseData = MutableLiveData<DraftConsultationResponse>()
        if (context.isNetworkConnected()) {
            choServices.getConsultation(consultationId)
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        responseData.postValue(bodyResponse as DraftConsultationResponse)
                    }
                }, context))
        }
        return responseData
    }

    override fun createFamilyMember(memberRequest: ChoMemberRequest): MutableLiveData<ChoPatientResponse> {
        val liveData = MutableLiveData<ChoPatientResponse>()
        if (context.isNetworkConnected()) {
            choServices.createUserFamilyMember(memberRequest)
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        liveData.postValue(bodyResponse as ChoPatientResponse)
                    }
                }, context))
        }
        return liveData
    }

    override fun updateFamilyMember(memberRequest: ChoMemberRequest): MutableLiveData<ChoPatientResponse> {
        val liveData = MutableLiveData<ChoPatientResponse>()
        if (context.isNetworkConnected()) {
            choServices.updateFamilyMember(memberRequest)
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        liveData.postValue(bodyResponse as ChoPatientResponse)
                    }
                }, context))
        }
        return liveData
    }

    override fun deleteFamilyMember(
        patientInfoId: String,
        familyMemberId: String
    ): MutableLiveData<BaseResponse> {
        val liveData = MutableLiveData<BaseResponse>()
        if (context.isNetworkConnected()) {
            choServices.deleteFamilyMember(patientInfoId, familyMemberId)
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        liveData.postValue(bodyResponse as BaseResponse)
                    }
                }, context))
        }
        return liveData
    }

    override fun getFamilyMembers(patientInfoId: String): MutableLiveData<ChoPatientResponse> {
        val liveData = MutableLiveData<ChoPatientResponse>()
        if (context.isNetworkConnected()) {
            choServices.getFamilyMembers(patientInfoId)
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        liveData.postValue(bodyResponse as ChoPatientResponse)
                    }
                }, context))
        }
        return liveData
    }

}