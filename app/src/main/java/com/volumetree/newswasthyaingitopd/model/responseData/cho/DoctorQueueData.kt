package com.volumetree.newswasthyaingitopd.model.responseData.cho

import android.os.Parcelable
import com.volumetree.newswasthyaingitopd.model.responseData.comman.BaseResponse

class DoctorQueueData {
    @kotlinx.parcelize.Parcelize
    data class DoctorQueueData(
        val token: String,
        val model: DoctorQueueModelData?
    ) : BaseResponse(), Parcelable

    @kotlinx.parcelize.Parcelize
    data class DoctorQueueModelData(
        val _list: ArrayList<DoctorQueueDataList>,
//        val _GetOnlineDoctorModel: DoctorQueueDoctorData
    ) : Parcelable

    @kotlinx.parcelize.Parcelize
    data class DoctorQueueDataList(
        val doctorId: Int,
        val consultationId: String,
        var waitingQueueId: Int,
        val firstname: String,
        val lastname: String,
        val crNumber: String,
        val tokenNumber: String,
        val age: String,
        val doB: String,
        val genderId: Int,
    ) : Parcelable

    @kotlinx.parcelize.Parcelize
    data class DoctorQueueDoctorData(
        val doctor_id: Int,
        val doctor_Name: String,
        val doctor_Status: String,
        val statusid: Int,
        val total_count: Int,
        val doctor_rating: String,
        val doctorimage: String,
        val hospitalName: String,
        val experience: String,
        val specialityName: String,
        val qualificationName: String
    ) : Parcelable
}