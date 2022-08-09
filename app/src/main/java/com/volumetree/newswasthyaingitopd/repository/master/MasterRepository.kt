package com.volumetree.newswasthyaingitopd.repository.master

import androidx.lifecycle.MutableLiveData
import com.volumetree.newswasthyaingitopd.model.requestData.cho.CancelTokenRequest
import com.volumetree.newswasthyaingitopd.model.requestData.cho.ChangeQueueRequest
import com.volumetree.newswasthyaingitopd.model.requestData.cho.RatingRequest
import com.volumetree.newswasthyaingitopd.model.requestData.cho.UploadProfilePicRequest
import com.volumetree.newswasthyaingitopd.model.responseData.cho.ConsultationOnlineDoctorResponse
import com.volumetree.newswasthyaingitopd.model.responseData.comman.*
import com.volumetree.newswasthyaingitopd.model.responseData.doctor.DoctorProfileResponse
import com.volumetree.newswasthyaingitopd.model.responseData.patient.DoctorSpecializationResponse
import com.volumetree.newswasthyaingitopd.model.responseData.patient.SpecialityResponse

interface MasterRepository {
    fun getDistrictMaster(): MutableLiveData<DistrictResponse>
    fun getCityMaster(distId: Int): MutableLiveData<CityResponse>
    fun getBlockMaster(distId: Int): MutableLiveData<BlockResponse>
    fun getSpecialityMaster(): MutableLiveData<SpecialityResponse>
    fun getDoctorBySpecialization(
        specialityId: Int,
        searchName: String
    ): MutableLiveData<DoctorSpecializationResponse>

    fun consultationOnlineDoctor(
        consultationId: String,
        patientInfo: String,
        specialityId: Int,
        doctorId: String
    ): MutableLiveData<ConsultationOnlineDoctorResponse>

    fun getBloodGroup(): MutableLiveData<BloodGroupResponse>
    fun getMaritalStatus(): MutableLiveData<MaritalStatusResponse>
    fun getAllergy(searchText: String): MutableLiveData<ArrayList<AllergyData>>
    fun getProblem(searchText: String): MutableLiveData<ArrayList<AllergyData>>
    fun getDiagnosis(searchText: String): MutableLiveData<ArrayList<AllergyData>>
    fun getVital(): MutableLiveData<VitalResponse>
    fun getDuration(): MutableLiveData<DurationResponse>
    fun getSeverity(): MutableLiveData<SeverityResponse>

    fun uploadProfilePic(uploadProfilePicRequest: UploadProfilePicRequest): MutableLiveData<ImageUploadResponse>
    fun uploadSignatureImage(uploadProfilePicRequest: UploadProfilePicRequest): MutableLiveData<ImageUploadResponse>
    fun patientRating(ratingRequest: RatingRequest): MutableLiveData<BaseResponse>
    fun getConsultationsDayDashboard(
        DWYLabel: Int,
        SendRec: Int
    ): MutableLiveData<DashboardConsultationsReportsResponse>

    fun getConsultationsVC(
        DWYLabel: Int,
        SendRec: Int
    ): MutableLiveData<DashboardConsultationsReportsResponse>

    fun getConsultationsGenderDashboard(
        SendRec: Int
    ): MutableLiveData<DashboardConsultationsReportsResponse>

    fun getConsultationsDashboard(
        SendRec: Int
    ): MutableLiveData<DashboardConsultationsResponse>

    fun cancelDeleteToken(
        cancelTokenRequest: CancelTokenRequest
    ): MutableLiveData<BaseResponse>

    fun changePatientQueue(
        changeQueueRequest: ChangeQueueRequest
    ): MutableLiveData<BaseResponse>

    fun getDoctorProfileById(
        doctorId: Int
    ): MutableLiveData<DoctorProfileResponse.DoctorProfileResponse>


    fun chatMessageByConsultationId(
        consultationId: Int
    ): MutableLiveData<ChatMessagesData>

    fun getMedicine(searchText: String): MutableLiveData<ArrayList<AllergyData>>

}