package com.volumetree.newswasthyaingitopd.retrofit

import com.volumetree.newswasthyaingitopd.model.requestData.cho.*
import com.volumetree.newswasthyaingitopd.model.responseData.cho.*
import com.volumetree.newswasthyaingitopd.model.responseData.comman.BaseResponse
import com.volumetree.newswasthyaingitopd.model.responseData.doctor.DoctorProfileResponse
import retrofit2.Call
import retrofit2.http.*

interface ChoServices {
    @POST("UnAuth/SignInCHO")
    fun signInCHO(@Body singInRequest: SingInRequest): Call<ChoProfileResponse.ChoProfileResponse>

    @GET("MemberProfile/Get")
    fun getMemberProfile(): Call<ChoProfileResponse.ChoProfileResponse>

    @GET("MemberProfile/Get")
    fun getDoctorProfile(): Call<DoctorProfileResponse.DoctorProfileResponse>

    @POST("CHOPatient/AddPatient")
    fun addPatient(@Body addPatientRequest: AddPatientRequest): Call<ChoPatientResponse>

    @POST("CHOPatient/CreateUserFamilyMember")
    fun createUserFamilyMember(@Body memberRequest: ChoMemberRequest): Call<ChoPatientResponse>

    @POST("CHOPatient/UpdateUserFamilyMember")
    fun updateFamilyMember(@Body memberRequest: ChoMemberRequest): Call<ChoPatientResponse>

    @GET("CHOPatient/DeleteFamilyMemember")
    fun deleteFamilyMember(
        @Query("PatientInfoId") PatientInfoId: String,
        @Query("PatientFamilyMember") PatientFamilyMember: String
    ): Call<BaseResponse>

    @GET("CHOPatient/GetFamilyMembers")
    fun getFamilyMembers(@Query("PatientInfoId") PatientInfoId: String): Call<ChoPatientResponse>

    @PUT("CHOPatient/UpdatePatientProfile")
    fun updatePatientProfile(@Body addPatientRequest: AddPatientRequest): Call<ChoPatientResponse>

    @POST("MemberProfile/CHOUpdateProfile")
    fun updateChoProfile(@Body updateChoRequest: UpdateChoRequest): Call<ChoProfileResponse.ChoProfileResponse>

    @GET("CHOPatient/GetPatientProfile")
    fun getChoPatient(@Query("Patient_id") Patient_id: String): Call<ChoPatientResponse>

    @GET("CHOPatient/GetCHOPatientList")
    fun getChoPatientList(
        @Query("LoadAll") LoadAll: Boolean,
        @Query("EnterSearch") EnterSearch: String,
        @Query("SkipRecords") SkipRecords: Int,
        @Query("LimitRecords") LimitRecords: Int
    ): Call<ChoPatientResponse>

    @GET("CHOPatient/GetCHOPatientList")
    fun getChoPatientList(
        @Query("LoadAll") LoadAll: Boolean,
        @Query("SkipRecords") SkipRecords: Int,
        @Query("LimitRecords") LimitRecords: Int
    ): Call<ChoPatientResponse>

    @POST("Consultation/ReferredList")
    fun getCaseList(
        @Body caseRequest: GetCaseRequest
    ): Call<CaseResponse>

    @POST("MemberProfile/ChangePassword")
    fun changePassword(@Body changePasswordRequest: ChangePasswordRequest): Call<BaseResponse>

    @POST("Consultation/DraftConsultation")
    fun draftConsultation(@Body draftConsultationRequest: DraftConsultationRequest): Call<DraftConsultationResponse>

    @POST("Consultation/UpdateSyncResponseConsultation")
    fun updateConsultation(@Body draftConsultationRequest: DraftConsultationRequest2): Call<DraftConsultationResponse>

    @GET("Consultation/{id}")
    fun getConsultation(@Path("id") id: Int): Call<DraftConsultationResponse>

//    @GET("Consultation/{id}")
//    fun getConsultationById(@Path("id") id: Int): Call<SuperConsultationResponseModel>

    @GET("Members/GetPatientQueueListByDoctor")
    fun getPatientQueueListByDoctor(
        @Query("DoctorId") DoctorId: Int,
    ): Call<DoctorQueueData.DoctorQueueData>


}