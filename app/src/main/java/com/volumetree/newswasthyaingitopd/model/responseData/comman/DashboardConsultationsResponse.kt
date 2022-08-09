package com.volumetree.newswasthyaingitopd.model.responseData.comman


data class DashboardConsultationsResponse(
    val lstModel: ArrayList<DashboardConsultationsData> = ArrayList()
) : BaseResponse()

class DashboardConsultationsData(
    val avgCons: String = "",
    val totalCons: String = "",
    val totalPatient: String = "",
    val prescription: String = "",
    val casesFwd: String = "",
    val medPres: String = ""
)
