package com.volumetree.newswasthyaingitopd.model.requestData.doctor


data class InProgressConsultationRequest(
    val consultationId: Int,
    val sendTo: Int,
    val statusId: Int,
    val sendBy: Int,

)
