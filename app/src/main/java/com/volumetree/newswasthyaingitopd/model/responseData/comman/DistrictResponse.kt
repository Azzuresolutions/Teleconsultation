package com.volumetree.newswasthyaingitopd.model.responseData.comman


data class DistrictResponse(
    val lstModel: ArrayList<DistrictModelData> = ArrayList()
) : BaseResponse()

data class DistrictModelData(
    val districtId: Int = 0,
    val stateId: Int = 0,
    val sourceId: Int = 0,
    val districtName: String = "",
    val isActive: Boolean = false,

    )
