package com.volumetree.newswasthyaingitopd.model.responseData.patient

import android.graphics.Bitmap
import com.volumetree.newswasthyaingitopd.model.responseData.comman.BaseResponse


data class SelectedDocData(
    val bitmap: Bitmap?,
    val imgName: String,
    val base64: String,
    val size: String,
    val fileFlag: String
) : BaseResponse()
