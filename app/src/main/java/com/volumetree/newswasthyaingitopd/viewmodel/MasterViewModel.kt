package com.volumetree.newswasthyaingitopd.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.volumetree.newswasthyaingitopd.model.requestData.cho.CancelTokenRequest
import com.volumetree.newswasthyaingitopd.model.requestData.cho.ChangeQueueRequest
import com.volumetree.newswasthyaingitopd.model.requestData.cho.RatingRequest
import com.volumetree.newswasthyaingitopd.model.requestData.cho.UploadProfilePicRequest
import com.volumetree.newswasthyaingitopd.model.responseData.cho.ConsultationOnlineDoctorResponse
import com.volumetree.newswasthyaingitopd.model.responseData.comman.*
import com.volumetree.newswasthyaingitopd.model.responseData.doctor.DoctorProfileResponse
import com.volumetree.newswasthyaingitopd.model.responseData.patient.DoctorSpecializationResponse
import com.volumetree.newswasthyaingitopd.model.responseData.patient.SpecialityResponse
import com.volumetree.newswasthyaingitopd.repository.master.MasterRepository

class MasterViewModel(private val masterRepository: MasterRepository) : ViewModel() {

    fun getDistrictMaster(): MutableLiveData<DistrictResponse> {
        return masterRepository.getDistrictMaster()
    }

    fun getCityMaster(distId: Int): MutableLiveData<CityResponse> {
        return masterRepository.getCityMaster(distId)
    }

    fun getBlockMaster(distId: Int): MutableLiveData<BlockResponse> {
        return masterRepository.getBlockMaster(distId)
    }

    fun getSpecialityMaster(): MutableLiveData<SpecialityResponse> {
        return masterRepository.getSpecialityMaster()
    }

    fun getDoctorBySpecialization(
        specialityId: Int,
        searchName: String
    ): MutableLiveData<DoctorSpecializationResponse> {
        return masterRepository.getDoctorBySpecialization(specialityId, searchName)
    }

    fun consultationOnlineDoctor(
        consultationId: String,
        patientInfoId: String,
        specialityId: Int,
        doctorId: String
    ): MutableLiveData<ConsultationOnlineDoctorResponse> {
        return masterRepository.consultationOnlineDoctor(
            consultationId,
            patientInfoId,
            specialityId,
            doctorId
        )
    }

    fun getBloodGroup(): MutableLiveData<BloodGroupResponse> {
        return masterRepository.getBloodGroup()
    }

    fun getMaritalStatus(): MutableLiveData<MaritalStatusResponse> {
        return masterRepository.getMaritalStatus()
    }

    fun getAllergy(searchText: String): MutableLiveData<ArrayList<AllergyData>> {
        return masterRepository.getAllergy(searchText)
    }

    fun getProblem(searchText: String): MutableLiveData<ArrayList<AllergyData>> {
        return masterRepository.getProblem(searchText)
    }

    fun getDiagnosis(searchText: String): MutableLiveData<ArrayList<AllergyData>> {
        return masterRepository.getDiagnosis(searchText)
    }

    fun getDuration(): MutableLiveData<DurationResponse> {
        return masterRepository.getDuration()
    }

    fun getSeverity(): MutableLiveData<SeverityResponse> {
        return masterRepository.getSeverity()
    }

    fun getVitals(): MutableLiveData<VitalResponse> {
        return masterRepository.getVital()
    }

    fun uploadProfilePic(uploadProfilePicRequest: UploadProfilePicRequest): MutableLiveData<ImageUploadResponse> {
        return masterRepository.uploadProfilePic(uploadProfilePicRequest)
    }

    fun uploadSignatureImage(uploadProfilePicRequest: UploadProfilePicRequest): MutableLiveData<ImageUploadResponse> {
        return masterRepository.uploadSignatureImage(uploadProfilePicRequest)
    }

    fun patientRating(ratingRequest: RatingRequest): MutableLiveData<BaseResponse> {
        return masterRepository.patientRating(ratingRequest)
    }

    fun getConsultationsDayDashboard(
        DWYLabel: Int,
        SendRec: Int
    ): MutableLiveData<DashboardConsultationsReportsResponse> {
        return masterRepository.getConsultationsDayDashboard(DWYLabel, SendRec)
    }

    fun getConsultationsGenderDashboard(
        SendRec: Int
    ): MutableLiveData<DashboardConsultationsReportsResponse> {
        return masterRepository.getConsultationsGenderDashboard(SendRec)
    }

    fun getConsultationsVC(
        DWYLabel: Int,
        SendRec: Int
    ): MutableLiveData<DashboardConsultationsReportsResponse> {
        return masterRepository.getConsultationsVC(DWYLabel, SendRec)
    }

    fun getConsultationsDashboard(
        SendRec: Int
    ): MutableLiveData<DashboardConsultationsResponse> {
        return masterRepository.getConsultationsDashboard(SendRec)
    }

    fun cancelDeleteToken(
        cancelTokenRequest: CancelTokenRequest
    ): MutableLiveData<BaseResponse> {
        return masterRepository.cancelDeleteToken(cancelTokenRequest)
    }

    fun changePatientQueue(
        changeQueueRequest: ChangeQueueRequest
    ): MutableLiveData<BaseResponse> {
        return masterRepository.changePatientQueue(changeQueueRequest)
    }


    fun getDoctorProfileById(
        doctorId: Int
    ): MutableLiveData<DoctorProfileResponse.DoctorProfileResponse> {
        return masterRepository.getDoctorProfileById(doctorId)
    }

    fun chatMessageByConsultationId(
        consultationId: Int
    ): MutableLiveData<ChatMessagesData> {
        return masterRepository.chatMessageByConsultationId(consultationId)
    }
    fun getMedicine(searchText: String): MutableLiveData<ArrayList<AllergyData>> {
        return masterRepository.getMedicine(searchText)
    }


}