package com.volumetree.newswasthyaingitopd.retrofit

import com.volumetree.newswasthyaingitopd.model.requestData.cho.CancelTokenRequest
import com.volumetree.newswasthyaingitopd.model.requestData.cho.ChangeQueueRequest
import com.volumetree.newswasthyaingitopd.model.requestData.cho.RatingRequest
import com.volumetree.newswasthyaingitopd.model.requestData.cho.UploadProfilePicRequest
import com.volumetree.newswasthyaingitopd.model.responseData.cho.ConsultationOnlineDoctorResponse
import com.volumetree.newswasthyaingitopd.model.responseData.comman.*
import com.volumetree.newswasthyaingitopd.model.responseData.doctor.DoctorProfileResponse
import com.volumetree.newswasthyaingitopd.model.responseData.patient.DoctorSpecializationResponse
import com.volumetree.newswasthyaingitopd.model.responseData.patient.SpecialityResponse
import retrofit2.Call
import retrofit2.http.*

interface CommonServices {

    @GET("PatientCommon/GetDistrictMaster/{id}")
    fun getDistrictMaster(@Path("id") id: Int): Call<DistrictResponse>

    @GET("PatientCommon/GetCityMaster/{id}")
    fun getCityMaster(@Path("id") id: Int): Call<CityResponse>

    @GET("PatientCommon/GetBlockMasterByDistrict/{id}")
    fun getBlockMasterByDistrict(@Path("id") id: Int): Call<BlockResponse>

    @GET("Masters/GetSpeciality")
    fun getSpeciality(): Call<SpecialityResponse>

    @GET("Masters/GetDoctorBySpecialization/{id}")
    fun getDoctorBySpecialization(@Path("id") id: Int): Call<DoctorSpecializationResponse>

    @GET("CHOPatient/ConsultationOnlineDoctor")
    fun consultationOnlineDoctor(
        @Query("ConsultationId") ConsultationId: String,
        @Query("PatientInfoId") PatientInfoId: String,
        @Query("spl_id") spl_id: Int,
        @Query("DoctorId") DoctorId: String
    ): Call<ConsultationOnlineDoctorResponse>

    @GET("CHOPatient/GetOnlineDoctors")
    fun getOnlineDoctorBySpecialization(
        @Query("spl_id") spl_id: Int,
        @Query("LimitRecords") LimitRecords: Int
    ): Call<DoctorSpecializationResponse>

    @GET("CHOPatient/GetOnlineDoctors")
    fun getOnlineDoctorBySpecialization(
        @Query("spl_id") spl_id: Int,
        @Query("LimitRecords") LimitRecords: Int,
        @Query("EnterSearch") EnterSearch: String
    ): Call<DoctorSpecializationResponse>

    @GET("Consultation/GetAllergyType/{id}")
    fun getAllergy(@Path("id") searchText: String): Call<ArrayList<AllergyData>>

    @GET("Consultation/GetDiagnosis/{id}")
    fun getDiagnosis(@Path("id") searchText: String): Call<ArrayList<AllergyData>>

    @GET("Masters/GetHCTestMaster")
    fun getVitals(): Call<VitalResponse>

    @GET("Consultation/GetProblemType/{id}")
    fun getProblemType(@Path("id") searchText: String): Call<ArrayList<AllergyData>>

    @GET("/api/Consultation/GetMedicineType/{id}")
    fun getMedicineType(@Path("id") searchText: String): Call<ArrayList<AllergyData>>

    @GET("Masters/GetMaritalStatusMaster")
    fun getMaritalStatus(): Call<MaritalStatusResponse>

    @GET("Masters/GetBloodGroupMaster")
    fun getBloodGroup(): Call<BloodGroupResponse>

    @GET("Masters/GetDurationMaster")
    fun getDuration(): Call<DurationResponse>

    @GET("Masters/GetSeverityMaster")
    fun getSeverity(): Call<SeverityResponse>

    @POST("MemberProfile/UpdateProfileImage")
    fun uploadProfilePic(@Body uploadProfilePicRequest: UploadProfilePicRequest): Call<ImageUploadResponse>

    @POST("MemberProfile/UpdateSignatureImage")
    fun uploadSignatureImage(@Body uploadProfilePicRequest: UploadProfilePicRequest): Call<ImageUploadResponse>

    @POST("/api/CHOPatient/SaveConsultationFeedBack")
    fun patientRating(@Body ratingRequest: RatingRequest): Call<BaseResponse>

    @GET("MemberDashboard/MemConsPDayDoc")
    fun getConsultationsDayDashboard(
        @Query("DWYLabel") DWYLabel: Int,
        @Query("SendRec") SendRec: Int,
    ): Call<DashboardConsultationsReportsResponse>

    @GET("MemberDashboard/MemVCDoc")
    fun getConsultationsVC(
        @Query("DWYLabel") DWYLabel: Int,
        @Query("SendRec") SendRec: Int
    ): Call<DashboardConsultationsReportsResponse>

    @GET("MemberDashboard/MemPatientGenderDoc")
    fun getConsultationsGenderDashboard(
        @Query("SendRec") SendRec: Int
    ): Call<DashboardConsultationsReportsResponse>


    @GET("MemberDashboard/MemDashLandingDoc")
    fun getConsultationsDashboard(
        @Query("SendRec") SendRec: Int
    ): Call<DashboardConsultationsResponse>

    @POST("CHOPatient/DeleteCancelToken")
    fun cancelDeleteToken(
        @Body cancelTokenRequest: CancelTokenRequest
    ): Call<BaseResponse>


    @POST("Consultation/ChangePatientQueue")
    fun changePatientQueue(
        @Body changeQueueRequest: ChangeQueueRequest
    ): Call<BaseResponse>


    @GET("/api/Members/GetDoctorProfile")
    fun getDoctorProfileById(
        @Query("DoctorId") DoctorId: Int
    ): Call<DoctorProfileResponse.DoctorProfileResponse>

    @GET("Consultation/ChatMessageByConsultationId")
    fun chatMessageByConsultationId(
        @Query("ConsultationId") ConsultationId: Int
    ): Call<ChatMessagesData>

}