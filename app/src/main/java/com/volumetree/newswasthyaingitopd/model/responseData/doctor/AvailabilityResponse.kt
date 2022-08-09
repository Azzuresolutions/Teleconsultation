package com.volumetree.newswasthyaingitopd.model.responseData.doctor

import android.os.Parcelable
import com.volumetree.newswasthyaingitopd.model.responseData.comman.BaseResponse

data class AvailabilityResponse(
    val statusId: Int,
) : BaseResponse(), Parcelable