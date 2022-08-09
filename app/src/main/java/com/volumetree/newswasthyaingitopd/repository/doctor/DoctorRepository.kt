package com.volumetree.newswasthyaingitopd.repository.doctor

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

interface DoctorRepository {
    fun checkAvailability(): MutableLiveData<AvailabilityResponse>
    fun changeAvailability(updateStatus: Int): MutableLiveData<AvailabilityResponse>
    fun updateOnlineDoctorsAcceptCall(
        inProgressConsultationRequest: InProgressConsultationRequest
    ): MutableLiveData<BaseResponse>

    fun updateDoctorProfile(
        updateDoctorRequest: UpdateDoctorRequest
    ): MutableLiveData<DoctorProfileResponse.DoctorProfileResponse>

    fun insertResponseConsultation(
        draftConsultationRequest: InsertConsultationRequest
    ): MutableLiveData<DraftConsultationResponse>

    fun getDoctorAppointments(
        getCaseRequest: GetCaseRequest
    ): MutableLiveData<CaseResponse>

    fun doctorLogout(
    ): MutableLiveData<BaseResponse>
}

