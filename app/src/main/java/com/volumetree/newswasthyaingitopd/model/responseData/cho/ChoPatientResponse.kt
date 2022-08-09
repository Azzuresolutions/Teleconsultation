package com.volumetree.newswasthyaingitopd.model.responseData.cho

import android.os.Parcelable
import com.volumetree.newswasthyaingitopd.model.responseData.comman.BaseResponse


open class ChoPatientResponse(
    val token: String,
    val total_Records: Int,
    val lstModel: ArrayList<ChoPatientData>?,
    val model: ChoPatientData
) : BaseResponse(), Parcelable

@kotlinx.parcelize.Parcelize
open class ChoPatientData(
    val patientInfoId: Int = 0,
    val firstName: String = "",
    val imagePath: String = "",
    val lastName: String = "",
    val email: String = "",
    val mobile: String = "",
    val dob: String = "",
    val createdDate: String = "",
    val genderId: Int = 0,
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
    val pincode: String = "",
    val createdBy: Int = 0,
    val ageId: Int = 0,
    val age: Int = 0,
    val monthId: Int = 0,
    val profile_Pic: String = "",
    val cityName: String = "",
    val districtName: String = "",
    val stateName: String = "",
    var relationName: String = "",
    var relationId: String = "",
    val blood_group_Name: String = "",
) : Parcelable