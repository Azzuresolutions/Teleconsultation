package com.volumetree.newswasthyaingitopd.model.responseData.cho

import com.volumetree.newswasthyaingitopd.model.requestData.cho.*
import com.volumetree.newswasthyaingitopd.model.responseData.comman.BaseResponse

data class DraftConsultationResponse(
    val model: ModelData,
    val isQueue: Boolean = false,
    val doctorID: Int = 0
) : BaseResponse()

class ModelData(
    val doctor_id: Int = 0,
    val doctor_Name: String = "",
    val specialityID: String = "",
    val consultationModel: ConsultationModel,
    val patientConsultationModel: PatientConsultationModelResponse,
    val lstConsultationAllergyModel: ArrayList<LstConsultationAllergyModel> = ArrayList(),
    val lstConsultationImagesModel: ArrayList<LstConsultationImagesModel> = ArrayList(),
    val lstConsultationMessageModel: ArrayList<LstConsultationMessageModel> = ArrayList(),
    val lstConsultationProblemsModel: ArrayList<LstConsultationProblemsModel>? = ArrayList(),
    val lstConsultationTestResultsModel: ArrayList<LstConsultationTestResultsModel> = ArrayList(),
    var lstConsultationMedicineModel: ArrayList<LstConsultationMedicineModel> = ArrayList(),
    var lstConsultationDiagnosisModel: List<LstConsultationDiagnosisModel> = ArrayList(),
    var toMembersPrescription: ToMembersPrescription = ToMembersPrescription(),
    var toInstitutionPrescription: ToInstitutionPrescription? = ToInstitutionPrescription()
)