package com.volumetree.newswasthyaingitopd.model.responseData.comman


data class DurationResponse(
    val lstModel: ArrayList<DurationData> = ArrayList()
) : BaseResponse()

class DurationData(
    val allergyDurationId: Int = 0,
    val allergyDuration: String = "",
    val isActive: Boolean = false
)
