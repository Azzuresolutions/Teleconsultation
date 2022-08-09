package com.volumetree.newswasthyaingitopd.model.requestData.cho

data class CancelTokenRequest(
    val doctorId: Int = 0,
    val consultationId: Int = 0,
    val patientInfoId: Int = 0
)
