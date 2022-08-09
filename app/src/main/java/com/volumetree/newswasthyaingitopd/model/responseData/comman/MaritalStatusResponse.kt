package com.volumetree.newswasthyaingitopd.model.responseData.comman


data class MaritalStatusResponse(
    val lstModel: ArrayList<MaritalData> = ArrayList()
) : BaseResponse()

class MaritalData(
    val maritalStatusId: Int = 0,
    val typeName: String = "",
    val isActive: Boolean = false
)
