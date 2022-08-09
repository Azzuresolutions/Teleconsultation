package com.volumetree.newswasthyaingitopd.model.requestData.patient


data class VerifyOTPRequest(
    val mobile: String,
    val otp: String
)
