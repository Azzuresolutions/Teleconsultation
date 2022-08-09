package com.volumetree.newswasthyaingitopd.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.volumetree.newswasthyaingitopd.model.requestData.cho.GetCaseRequest
import com.volumetree.newswasthyaingitopd.model.requestData.cho.InsertConsultationRequest
import com.volumetree.newswasthyaingitopd.model.requestData.doctor.InProgressConsultationRequest
import com.volumetree.newswasthyaingitopd.model.requestData.doctor.UpdateDoctorRequest
import com.volumetree.newswasthyaingitopd.model.responseData.cho.CaseResponse
import com.volumetree.newswasthyaingitopd.model.responseData.cho.DraftConsultationResponse
import com.volumetree.newswasthyaingitopd.model.responseData.comman.BaseResponse
import com.volumetree.newswasthyaingitopd.model.responseData.doctor.AvailabilityResponse
import com.volumetree.newswasthyaingitopd.model.responseData.doctor.DoctorProfileResponse
import com.volumetree.newswasthyaingitopd.repository.doctor.DoctorRepository

class DoctorViewModel(private val doctorRepository: DoctorRepository) : ViewModel() {

    fun checkAvailability(): MutableLiveData<AvailabilityResponse> {
        return doctorRepository.checkAvailability()
    }

    fun changeAvailability(updateStatus: Int): MutableLiveData<AvailabilityResponse> {
        return doctorRepository.changeAvailability(updateStatus)
    }

    fun updateOnlineDoctorsAcceptCall(
        inProgressConsultationRequest: InProgressConsultationRequest
    ): MutableLiveData<BaseResponse> {
        return doctorRepository.updateOnlineDoctorsAcceptCall(
            inProgressConsultationRequest
        )
    }

    fun getDoctorAppointments(
        caseRequest: GetCaseRequest
    ): MutableLiveData<CaseResponse> {
        return doctorRepository.getDoctorAppointments(
            caseRequest
        )
    }

    fun updateDoctorProfile(
        updateDoctorRequest: UpdateDoctorRequest
    ): MutableLiveData<DoctorProfileResponse.DoctorProfileResponse> {
        return doctorRepository.updateDoctorProfile(updateDoctorRequest)
    }

    fun insertResponseConsultation(
        draftConsultationRequest: InsertConsultationRequest
    ): MutableLiveData<DraftConsultationResponse> {
        return doctorRepository.insertResponseConsultation(draftConsultationRequest)
    }

    fun doctorLogout(
    ): MutableLiveData<BaseResponse> {
        return doctorRepository.doctorLogout()
    }
}