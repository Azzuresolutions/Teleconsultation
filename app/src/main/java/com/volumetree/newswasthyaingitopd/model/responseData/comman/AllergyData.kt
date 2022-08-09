package com.volumetree.newswasthyaingitopd.model.responseData.comman

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AllergyData(
    val hierarchy: String = "",
    val isPreferredTerm: String = "",
    val conceptState: String = "",
    val conceptFsn: String = "",
    val definitionStatus: String = "",
    val conceptId: String = "",
    val languageCode: String = "",
    val typeId: String = "",
    val term: String = "",
    val caseSignificanceId: String = "",
    val id: String = "",
    val effectiveTime: String = "",
    val activeStatus: String = "",
    val moduleId: String = ""
) : Parcelable
