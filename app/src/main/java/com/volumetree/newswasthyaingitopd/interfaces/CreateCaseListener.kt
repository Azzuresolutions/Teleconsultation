package com.volumetree.newswasthyaingitopd.interfaces

import com.volumetree.newswasthyaingitopd.model.requestData.cho.LstConsultationAllergyModel
import com.volumetree.newswasthyaingitopd.model.requestData.cho.LstConsultationTestResultsModel
import com.volumetree.newswasthyaingitopd.model.responseData.comman.AllergyData

interface CreateCaseListener {
    fun onAllergySelected(allergyData: AllergyData?)
    fun onProblemSelected(allergyData: AllergyData?)
    fun onMedicineSelected(allergyData: AllergyData?)
    fun onAllergyUpdated(allergies: ArrayList<LstConsultationAllergyModel>)
    fun onVitalUpdate(vitals: ArrayList<LstConsultationTestResultsModel>)
    fun onDiagnosisSelected(diagnosis: AllergyData?)
}