package com.volumetree.newswasthyaingitopd.retrofit

import com.volumetree.newswasthyaingitopd.model.requestData.patient.*
import com.volumetree.newswasthyaingitopd.model.responseData.comman.BaseResponse
import com.volumetree.newswasthyaingitopd.model.responseData.patient.*
import retrofit2.Call
import retrofit2.http.*

interface PatientServices {
    @POST("UnAuth/SendOTP")
    fun sendOTP(@Body mobileNumber: SendOTPRequest): Call<OTPResponse>

    @POST("UnAuth/VerifyOTP")
    fun verifyOTP(@Body verifyOTPRequest: VerifyOTPRequest): Call<VerifyOtpResponse.VerifyOtpResponse>

    @POST("Patient/Registration")
    fun registration(@Body registrationRequest: RegistrationRequest): Call<RegistrationResponse>

    @GET("Patient/GetPatientProfile")
    fun getPatientProfile(): Call<PatientProfileResponse.PatientProfileResponse>

    @PUT("Patient/UpdatePatientProfile")
    fun updatePatientProfile(@Body registrationRequest: RegistrationRequest): Call<RegistrationResponse>

    @POST("Patient/TokenRegistration")
    fun tokenRegistration(@Body tokenRegistrationRequest: TokenRegistrationRequest): Call<BaseResponse>

    @GET("PatientCommon/GetFamilyMembers")
    fun getFamilyMembers(): Call<FamilyMemberResponse>

    @GET("PatientCommon/DeleteFamilyMemember/")
    fun deleteFamilyMembers(@Query("PatientInfoId") PatientInfoId: Int): Call<BaseResponse>

    @POST("PatientCommon/CreateUserFamilyMember")
    fun createUserFamilyMember(@Body memberRequest: MemberRequest): Call<BaseResponse>

    @POST("PatientCommon/UpdateUserFamilyMember")
    fun updateUserFamilyMember(@Body memberRequest: MemberRequest): Call<BaseResponse>


}