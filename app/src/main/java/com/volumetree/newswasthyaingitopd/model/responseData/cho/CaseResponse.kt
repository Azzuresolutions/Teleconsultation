package com.volumetree.newswasthyaingitopd.model.responseData.cho

import android.os.Parcelable
import com.volumetree.newswasthyaingitopd.model.responseData.comman.BaseResponse


open class CaseResponse(
    val token: String,
    val total_Records: Int,
    val lstModel: ArrayList<CaseData>?,
    val model: ChoPatientData
) : BaseResponse(), Parcelable

@kotlinx.parcelize.Parcelize
open class CaseData(
    val consultationId: String = "",
    val patientInfoID: String = "",
    val imagepath: String = "",
    val createdBy: String = "",
    val statusId: Int = 0,
    val patientFirstName: String = "",
    val patientLastName: String = "",
    val sendToName: String = "",
    val sendTo: String = "0",
    val sendByName: String = "",
    val statusName: String = "",
    val recentQuery: String = "",
    val referredOn: String = "",
    val patientGenderId: String = "",
    val patientDOB: String = "",
    val patientMobile: String = "",
    val crNumber: String = "",
    val receivedOn: String = ""
) : Parcelable