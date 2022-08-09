package com.volumetree.newswasthyaingitopd.model.responseData.doctor

import android.os.Parcelable
import com.volumetree.newswasthyaingitopd.model.responseData.comman.BaseResponse


data class DoctorOTPResponse(
    val token: String,
    val model: InstituteModel
) : BaseResponse()


@kotlinx.parcelize.Parcelize
open class InstituteModel(
    val mobile: String,
    val institutionName: String,
    val institutionId: Int
) : Parcelable

