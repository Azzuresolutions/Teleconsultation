package com.volumetree.newswasthyaingitopd.model.responseData.patient

import com.volumetree.newswasthyaingitopd.model.responseData.comman.BaseResponse


data class OTPResponse(
    val token: String
) : BaseResponse()
