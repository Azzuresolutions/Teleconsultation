package com.volumetree.newswasthyaingitopd.model.requestData.doctor

data class UpdateDoctorRequest(
    val memberId: String = "0",
    val addressLine1: String = "",
    val addressLine2: String = "",
    val pinCode: String = "",
    val registrationNumber: String = "",
    val doctor_Exp: String = "",
    val genderID: Int = 0,
    val genderName: String = "",
    val imagePath: String = ""
)
