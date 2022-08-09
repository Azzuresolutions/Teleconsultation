package com.volumetree.newswasthyaingitopd.model.responseData.cho

import android.os.Parcelable
import com.volumetree.newswasthyaingitopd.model.responseData.comman.BaseResponse

sealed class ChoProfileResponse {
    @kotlinx.parcelize.Parcelize
    data class ChoProfileResponse(
        val token: String,
        val model: ChoProfileData?
    ) : BaseResponse(), Parcelable

    @kotlinx.parcelize.Parcelize
    data class ChoProfileData(
        val patientId: Int,
        val memberId: Int,
        val firstName: String,
        val middleName: String,
        val lastName: String,
        val fax: String,
        val addressLine1: String,
        val addressLine2: String,
        val email: String,
        var imagePath: String,
        var signaturePath: String,
        val createdBy: Int,
        val isActive: Boolean,
        val mobile: String,
        val registrationNumber: String,
        val age: Int,
        val dob: String,
        val stateId: Int,
        val stateName: String,
        val patientStateId: Int,
        val registrationType: Int,
        val cityId: Int,
        val cityName: String,
        val districtId: Int,
        val districtName: String,
        val blockId: Int,
        val blockName: String,
        val countryId: Int,
        val pinCode: String,
        val institutionId: Int,
        val institutionName: String,
        val genderName: String,
        val specialityName: String,
        val sourceId: Int,
        val isAvailable: Int,
        val roleType: Int,
        val para: Int,
        val medicalCollegeId: Int,
        val institutionStateId: Int,
        val doctor_exp: String,
        val genderId: Int,
        val qualificationId: Int,
        val qualificationName: String,
        val specializationName: String,
        val userName: String,
        val specializationId: Int,
        val ratingMasterId: Int,
        val isMaster: Boolean,
        val statusId: Int
    ) : Parcelable
}