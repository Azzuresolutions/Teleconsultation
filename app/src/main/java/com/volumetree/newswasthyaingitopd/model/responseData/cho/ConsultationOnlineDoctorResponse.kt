package com.volumetree.newswasthyaingitopd.model.responseData.cho

import android.os.Parcelable
import com.volumetree.newswasthyaingitopd.model.responseData.comman.BaseResponse
import kotlinx.android.parcel.Parcelize

@Parcelize
class ConsultationOnlineDoctorResponse : BaseResponse(), Parcelable {
    val token: String = ""
    val model: ConsultationOnlineDoctorData = ConsultationOnlineDoctorData()
    val isQueue: Boolean = false
    var queueNo: Int = 0
    val total_Records: Int = 0
    var patientInfoId: Int = 0
}
