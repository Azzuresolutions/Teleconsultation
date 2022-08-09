package com.volumetree.newswasthyaingitopd.model.requestData.cho

data class ChoMemberRequest(
    val firstName: String,
    val lastName: String,
    val relationID: String,
    val relationName: String,
    val dob: String,
    val genderID: Int = 0,
    val patientInfoId: Int = 0,
    val stateId: Int = 19
)
