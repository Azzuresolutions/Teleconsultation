package com.volumetree.newswasthyaingitopd.repository.doctor

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.volumetree.newswasthyaingitopd.model.requestData.cho.GetCaseRequest
import com.volumetree.newswasthyaingitopd.model.requestData.cho.InsertConsultationRequest
import com.volumetree.newswasthyaingitopd.model.requestData.doctor.InProgressConsultationRequest
import com.volumetree.newswasthyaingitopd.model.requestData.doctor.UpdateDoctorRequest
import com.volumetree.newswasthyaingitopd.model.responseData.cho.CaseResponse
import com.volumetree.newswasthyaingitopd.model.responseData.cho.DraftConsultationResponse
import com.volumetree.newswasthyaingitopd.model.responseData.comman.BaseResponse
import com.volumetree.newswasthyaingitopd.model.responseData.doctor.AvailabilityResponse
import com.volumetree.newswasthyaingitopd.model.responseData.doctor.DoctorProfileResponse
import com.volumetree.newswasthyaingitopd.retrofit.CustomCB
import com.volumetree.newswasthyaingitopd.retrofit.DoctorServices
import com.volumetree.newswasthyaingitopd.utils.isNetworkConnected

open class DoctorRepositoryImpl(private val doctorServices: DoctorServices, val context: Context) :
    DoctorRepository {

    override fun checkAvailability(): MutableLiveData<AvailabilityResponse> {
        val responseData = MutableLiveData<AvailabilityResponse>()
        if (context.isNetworkConnected()) {
            doctorServices.checkAvailability()
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        responseData.postValue(bodyResponse as AvailabilityResponse)
                    }
                }, context))
        }
        return responseData
    }

    override fun changeAvailability(updateStatus: Int): MutableLiveData<AvailabilityResponse> {
        val responseData = MutableLiveData<AvailabilityResponse>()
        if (context.isNetworkConnected()) {
            doctorServices.changeAvailability(updateStatus)
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        responseData.postValue(bodyResponse as AvailabilityResponse)
                    }
                }, context))
        }
        return responseData
    }

    override fun updateOnlineDoctorsAcceptCall(
        inProgressConsultationRequest: InProgressConsultationRequest
    ): MutableLiveData<BaseResponse> {
        val responseData = MutableLiveData<BaseResponse>()
        if (context.isNetworkConnected()) {
            doctorServices.inProcessConsultation(
                inProgressConsultationRequest
            )
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        responseData.postValue(bodyResponse as BaseResponse)
                    }
                }, context))
        }
        return responseData
    }

    override fun getDoctorAppointments(
        getCaseRequest: GetCaseRequest
    ): MutableLiveData<CaseResponse> {
        val responseData = MutableLiveData<CaseResponse>()
        if (context.isNetworkConnected()) {
            doctorServices.getDoctorAppointments(
                getCaseRequest
            )
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        responseData.postValue(bodyResponse as CaseResponse)
                    }
                }, context))
        }
        return responseData
    }

    override fun updateDoctorProfile(
        updateDoctorRequest: UpdateDoctorRequest
    ): MutableLiveData<DoctorProfileResponse.DoctorProfileResponse> {
        val responseData = MutableLiveData<DoctorProfileResponse.DoctorProfileResponse>()
        doctorServices.updateDoctorProfile(updateDoctorRequest)
            .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                override fun onResponse(bodyResponse: Any?) {
                    responseData.postValue(bodyResponse as DoctorProfileResponse.DoctorProfileResponse)
                }
            }, context))
        return responseData
    }

    override fun insertResponseConsultation(
        draftConsultationRequest: InsertConsultationRequest
    ): MutableLiveData<DraftConsultationResponse> {
        val responseData = MutableLiveData<DraftConsultationResponse>()
        doctorServices.insertResponseConsultation(draftConsultationRequest)
            .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                override fun onResponse(bodyResponse: Any?) {
                    responseData.postValue(bodyResponse as DraftConsultationResponse)
                }
            }, context))
        return responseData
    }

    override fun doctorLogout(
    ): MutableLiveData<BaseResponse> {
        val responseData = MutableLiveData<BaseResponse>()
        doctorServices.doctorLogout()
            .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                override fun onResponse(bodyResponse: Any?) {
                    responseData.postValue(bodyResponse as BaseResponse)
                }
            }, context))
        return responseData
    }

}