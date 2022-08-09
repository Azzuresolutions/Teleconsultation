package com.volumetree.newswasthyaingitopd.model.responseData.comman

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
open class BaseResponse : Parcelable {
    val success: Boolean = false
    val message: String = ""
    val requestCode: Int = 0
}
