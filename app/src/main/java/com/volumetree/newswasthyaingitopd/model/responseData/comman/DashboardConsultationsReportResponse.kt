package com.volumetree.newswasthyaingitopd.model.responseData.comman


data class DashboardConsultationsReportsResponse(
    val lstModel: ArrayList<DashboardConsultationsReportData> = ArrayList()
) : BaseResponse()

class DashboardConsultationsReportData(
    val value: Double = 0.0,
    val label: String = ""
)
