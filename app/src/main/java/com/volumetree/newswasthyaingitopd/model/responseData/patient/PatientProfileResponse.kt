package com.volumetree.newswasthyaingitopd.model.responseData.patient

import android.os.Parcelable
import com.volumetree.newswasthyaingitopd.model.responseData.comman.BaseResponse

sealed class PatientProfileResponse {
    @kotlinx.parcelize.Parcelize
    data class PatientProfileResponse(
        val token: String,
        val model: PatientProfileData?
    ) : BaseResponse(), Parcelable

    @kotlinx.parcelize.Parcelize
    data class PatientProfileData(
        val patientInfoId: Int,
        val firstName: String,
        val middleName: String,
        val lastName: String,
        val fax: String,
        val addressLine1: String,
        val email: String,
        val mobile: String,
        val age: Int,
        val dob: String,
        val stateId: Int,
        val patientStateId: Int,
        val registrationType: Int,
        val cityId: Int,
        val cityName: String,
        val districtId: Int,
        val districtName: String,
        val blockId: Int,
        val blockName: String,
        val countryId: Int,
        val pinCode: Int,
        val institutionId: Int,
        val sourceId: Int,
        val genderId: Int
    ) : Parcelable
}