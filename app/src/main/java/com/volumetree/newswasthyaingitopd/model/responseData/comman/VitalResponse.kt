package com.volumetree.newswasthyaingitopd.model.responseData.comman


data class VitalResponse(
    val lstModel: ArrayList<VitalData> = ArrayList()
) : BaseResponse()

class VitalData(
    val testId: Int = 0,
    val name: String = "",
    val categoryName: String = "",
    val units: String = "",
    val isActive: Boolean = false,
)
