package com.volumetree.newswasthyaingitopd.model.requestData.cho

data class RatingRequest(
    val patientInfoId: String = "",
    val memberId: String = "",
    val consultationId: String = "",
    val rating: String = "",
    val feedback: String = ""
)
