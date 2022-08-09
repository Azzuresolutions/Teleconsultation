package com.volumetree.newswasthyaingitopd.model.responseData.comman

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class PrescriptionData(
    val doctorName: String = "",
    val consultationId: Int = 0,
    val consultationDate: String = "",

    )
