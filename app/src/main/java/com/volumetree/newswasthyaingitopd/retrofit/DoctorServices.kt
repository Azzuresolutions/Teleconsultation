package com.volumetree.newswasthyaingitopd.retrofit

import com.volumetree.newswasthyaingitopd.model.requestData.cho.GetCaseRequest
import com.volumetree.newswasthyaingitopd.model.requestData.cho.InsertConsultationRequest
import com.volumetree.newswasthyaingitopd.model.requestData.doctor.InProgressConsultationRequest
import com.volumetree.newswasthyaingitopd.model.requestData.doctor.SendOTPRequest
import com.volumetree.newswasthyaingitopd.model.requestData.doctor.UpdateDoctorRequest
import com.volumetree.newswasthyaingitopd.model.requestData.doctor.VerifyDoctorOTPRequest
import com.volumetree.newswasthyaingitopd.model.responseData.cho.CaseResponse
import com.volumetree.newswasthyaingitopd.model.responseData.cho.DraftConsultationResponse
import com.volumetree.newswasthyaingitopd.model.responseData.comman.BaseResponse
import com.volumetree.newswasthyaingitopd.model.responseData.doctor.AvailabilityResponse
import com.volumetree.newswasthyaingitopd.model.responseData.doctor.DoctorOTPResponse
import com.volumetree.newswasthyaingitopd.model.responseData.doctor.DoctorProfileResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface DoctorServices {
    @POST("UnAuth/DoctorLoginRequestOTP")
    fun sendOTP(@Body mobileNumber: SendOTPRequest): Call<DoctorOTPResponse>

    @POST("UnAuth/DoctorLoginVerifyOTP")
    fun verifyOTP(@Body verifyOTPRequest: VerifyDoctorOTPRequest): Call<DoctorProfileResponse.DoctorProfileResponse>

    @GET("Consultation/CheckAvailability")
    fun checkAvailability(): Call<AvailabilityResponse>

    @GET("Consultation/ChangeAvailabilityStatus")
    fun changeAvailability(@Query("StatusID") StatusID: Int): Call<AvailabilityResponse>


    @POST("/api/MemberProfile/UpdateProfile")
    fun updateDoctorProfile(@Body updateDoctorRequest: UpdateDoctorRequest): Call<DoctorProfileResponse.DoctorProfileResponse>

    @POST("Consultation/InsertResponseConsultation")
    fun insertResponseConsultation(@Body draftConsultationRequest: InsertConsultationRequest): Call<DraftConsultationResponse>

    @POST("Consultation/InProcessConsultation")
    fun inProcessConsultation(
        @Body inProgressConsultationRequest: InProgressConsultationRequest
    ): Call<BaseResponse>

    @POST("Consultation/ReceivedList")
    fun getDoctorAppointments(
        @Body caseRequest: GetCaseRequest
    ): Call<CaseResponse>

    @GET("MemberProfile/DoctorLogout")
    fun doctorLogout(
    ): Call<BaseResponse>

}