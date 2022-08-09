package com.volumetree.newswasthyaingitopd.repository.patient

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.volumetree.newswasthyaingitopd.model.requestData.patient.MemberRequest
import com.volumetree.newswasthyaingitopd.model.requestData.patient.TokenRegistrationRequest
import com.volumetree.newswasthyaingitopd.model.responseData.comman.BaseResponse
import com.volumetree.newswasthyaingitopd.model.responseData.patient.FamilyMemberResponse
import com.volumetree.newswasthyaingitopd.retrofit.CustomCB
import com.volumetree.newswasthyaingitopd.retrofit.PatientServices
import com.volumetree.newswasthyaingitopd.utils.isNetworkConnected

open class PatientRepositoryImpl(
    private val retrofitService: PatientServices,
    val context: Context
) :
    PatientRepository {
    override fun registrationToken(tokenRegistrationRequest: TokenRegistrationRequest): MutableLiveData<BaseResponse> {
        val liveData = MutableLiveData<BaseResponse>()
        if (context.isNetworkConnected()) {
            retrofitService.tokenRegistration(tokenRegistrationRequest)
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        liveData.postValue(bodyResponse as BaseResponse)
                    }
                }, context))
        }
        return liveData
    }

    override fun getFamilyMember(): MutableLiveData<FamilyMemberResponse> {
        val liveData = MutableLiveData<FamilyMemberResponse>()
        if (context.isNetworkConnected()) {
            retrofitService.getFamilyMembers()
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        liveData.postValue(bodyResponse as FamilyMemberResponse)
                    }
                }, context))
        }
        return liveData
    }

    override fun deleteFamilyMember(memberId: Int): MutableLiveData<BaseResponse> {
        val liveData = MutableLiveData<BaseResponse>()
        if (context.isNetworkConnected()) {
            retrofitService.deleteFamilyMembers(memberId)
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        liveData.postValue(bodyResponse as BaseResponse)
                    }
                }, context))
        }
        return liveData
    }

    override fun createFamilyMember(memberRequest: MemberRequest): MutableLiveData<BaseResponse> {
        val liveData = MutableLiveData<BaseResponse>()
        if (context.isNetworkConnected()) {
            retrofitService.createUserFamilyMember(memberRequest)
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        liveData.postValue(bodyResponse as BaseResponse)
                    }
                }, context))
        }
        return liveData
    }

    override fun updateFamilyMember(memberRequest: MemberRequest): MutableLiveData<BaseResponse> {
        val liveData = MutableLiveData<BaseResponse>()
        if (context.isNetworkConnected()) {
            retrofitService.updateUserFamilyMember(memberRequest)
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        liveData.postValue(bodyResponse as BaseResponse)
                    }
                }, context))
        }
        return liveData
    }

}