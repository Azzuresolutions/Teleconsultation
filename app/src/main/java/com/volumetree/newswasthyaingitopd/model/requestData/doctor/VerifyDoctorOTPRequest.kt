package com.volumetree.newswasthyaingitopd.model.requestData.doctor


data class VerifyDoctorOTPRequest(
    val username: String,
    val otp: String,
    val institutionId: String
)
