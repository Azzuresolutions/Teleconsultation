package com.volumetree.newswasthyaingitopd.model.responseData.comman


data class BloodGroupResponse(
    val lstModel: ArrayList<BloodGroupData> = ArrayList()
) : BaseResponse()

class BloodGroupData(
    val bloodGroupId: Int = 0,
    val bloodGroupName: String = "",
    val description: String = "",
    val isActive: Boolean = false
)
