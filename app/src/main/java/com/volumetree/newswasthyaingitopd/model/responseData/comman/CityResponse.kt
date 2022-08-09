package com.volumetree.newswasthyaingitopd.model.responseData.comman


data class CityResponse(
    val lstModel: ArrayList<CityModelData> = ArrayList()
) : BaseResponse()

data class CityModelData(
    val cityId: Int = 0,
    val cityName: String = "",
    val districtId: Int = 0,
    val isActive: Boolean = false,
    val sourceId: Int = 0,

    )
