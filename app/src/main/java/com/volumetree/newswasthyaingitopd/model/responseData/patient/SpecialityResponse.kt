package com.volumetree.newswasthyaingitopd.model.responseData.patient

import android.os.Parcelable
import com.volumetree.newswasthyaingitopd.model.responseData.comman.BaseResponse
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SpecialityResponse(
    val lstModel: ArrayList<SpecialityData> = ArrayList()
) : BaseResponse(), Parcelable

@Parcelize
data class SpecialityData(
    val specialityId: Int = 0,
    val specialityName: String = "",
    val isActive: Boolean = false,
    val sourceId: Int = 0,
    val stateId: Int = 0
) : Parcelable
