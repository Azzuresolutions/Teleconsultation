package com.volumetree.newswasthyaingitopd.model.requestData.patient


data class TokenRegistrationRequest(
    val patientInfoId: String,
    val opdTypeId: String,
    val specialityId: String,
    val doctorId: String,
    val imageList: ArrayList<TokenImageData>
)

data class TokenImageData(
    val imageName: String
)
