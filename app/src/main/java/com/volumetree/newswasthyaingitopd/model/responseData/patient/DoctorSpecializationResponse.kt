package com.volumetree.newswasthyaingitopd.model.responseData.patient

import android.os.Parcelable
import com.volumetree.newswasthyaingitopd.model.responseData.comman.BaseResponse
import kotlinx.android.parcel.Parcelize


@Parcelize
data class DoctorSpecializationResponse(
    val lstModel: ArrayList<DoctorSpecializationData> = ArrayList()
) : BaseResponse(), Parcelable

@Parcelize
data class DoctorSpecializationData(
    val id: Int = 0,
    val name: String = "",
    val doctor_id: String = "",
    val doctor_Name: String = "",
    val doctor_Status: String = "",
    val doctor_rating: Double = 0.0,
    val prefix: String = "",
    val experience: String = "",
    val hospitalName: String = "",
    val doctorimage: String = ""
) : Parcelable

