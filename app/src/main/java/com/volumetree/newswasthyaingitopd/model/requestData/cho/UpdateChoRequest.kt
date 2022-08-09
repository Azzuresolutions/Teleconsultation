package com.volumetree.newswasthyaingitopd.model.requestData.cho

data class UpdateChoRequest(
    val memberId: String = "0",
    val addressLine1: String = "",
    val addressLine2: String = "",
    val pinCode: String = "",
    val mobile: String = "",
    val email: String = "",
    val genderID: Int = 0,
    val genderName: String = "",
    val cityId: Int = 0,
    val profile_Pic: String = ""
)
