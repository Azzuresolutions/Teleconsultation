package com.volumetree.newswasthyaingitopd.model.responseData.cho

import android.os.Parcelable
import com.volumetree.newswasthyaingitopd.model.responseData.comman.BaseResponse
import kotlinx.android.parcel.Parcelize


@Parcelize
class ConsultationOnlineDoctorData(
    var doctor_id: Int = 0,
    val statusid: Int = 0,
    var doctor_Name: String = "",
    val experience: String = "",
    val hospitalName: String = "",
    var doctorimage: String = "",
    var speciality: String = "",
) : Parcelable