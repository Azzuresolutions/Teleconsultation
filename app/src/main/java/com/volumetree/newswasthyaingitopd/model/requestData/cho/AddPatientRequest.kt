package com.volumetree.newswasthyaingitopd.model.requestData.cho

data class AddPatientRequest(
    val patientInfoId: String = "0",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val mobile: String = "",
    val genderID: Int = 0,
    val guardian_Title: String = "",
    val guardian_Name: String = "",
    val marital_Status: String = "",
    val blood_group_id: Int = 0,
    val addressLine1: String = "",
    val addressLine2: String = "",
    val patientStateId: Int = 19,
    val stateId: Int = 19,
    val cityId: Int = 0,
    val districtId: Int = 0,
    val countryId: Int = 0,
    val institutionId: Int = 0,
    val sourceId: Int = 0,
    val blockId: Int = 0,
    val pinCode: String = "",
    val createdBy: Int = 0,
    val profile_Pic: String = "",
    var filePath: String = "",
    var fileFlag: String = "",
    var fileName: String = "",
    var ageId: Int = 0,
    var monthId: Int = 0,
    var age: Int = 0,
)