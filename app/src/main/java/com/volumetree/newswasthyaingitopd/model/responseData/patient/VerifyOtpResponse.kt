package com.volumetree.newswasthyaingitopd.model.responseData.patient

import android.os.Parcelable
import com.volumetree.newswasthyaingitopd.model.responseData.comman.BaseResponse
import kotlinx.android.parcel.Parcelize

sealed class VerifyOtpResponse {
    @Parcelize
    data class VerifyOtpResponse(
        val token: String,
        val model: UserModel
    ) : BaseResponse(), Parcelable

    @Parcelize
    data class UserModel(
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
        val districtId: Int,
        val blockId: Int,
        val countryId: Int,
        val pinCode: Int,
        val institutionId: Int,
        val sourceId: Int
    ) : Parcelable
}