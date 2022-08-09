package com.volumetree.newswasthyaingitopd.model.responseData.comman


data class BlockResponse(
    val lstModel: ArrayList<BlockModelData> = ArrayList()
) : BaseResponse()

class BlockModelData(
    val blockId: Int = 0,
    val blockName: String = "",
    val blockVersion: Int = 0,
    val countryId: Int = 0,
    val districtId: Int = 0,
    val id: Int = 0,
    val districtName: String = "",
    val isActive: Boolean = false,
    val stateId: Int = 0,
    val stateName: String = ""
)
