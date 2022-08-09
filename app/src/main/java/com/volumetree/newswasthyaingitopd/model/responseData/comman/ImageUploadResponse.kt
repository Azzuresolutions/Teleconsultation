package com.volumetree.newswasthyaingitopd.model.responseData.comman

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageUploadResponse(
    val imageURL: String = "",

    ) : BaseResponse(), Parcelable
