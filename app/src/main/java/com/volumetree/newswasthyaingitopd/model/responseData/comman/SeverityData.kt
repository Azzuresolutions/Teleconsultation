package com.volumetree.newswasthyaingitopd.model.responseData.comman


data class SeverityResponse(
    val lstModel: ArrayList<SeverityData> = ArrayList()
) : BaseResponse()

class SeverityData(
    val allergySeverityId: Int = 0,
    val allergySeverityName: String = "",
    val sctid: Int = 0,
    val isActive: Boolean = false
)
