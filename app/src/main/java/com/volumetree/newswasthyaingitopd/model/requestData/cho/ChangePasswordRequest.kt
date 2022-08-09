package com.volumetree.newswasthyaingitopd.model.requestData.cho

data class ChangePasswordRequest(
    val memberId: Int = 0,
    val password: String = "",
    val oldPassword: String = ""
)
