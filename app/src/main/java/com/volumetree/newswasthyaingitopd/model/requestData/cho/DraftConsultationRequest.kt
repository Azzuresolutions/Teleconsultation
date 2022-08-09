package com.volumetree.newswasthyaingitopd.model.requestData.cho

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

open class DraftConsultationRequest(
    val consultationModel: ConsultationModel? = null,
    val patientConsultationModel: PatientConsultationModel? = null,
    val lstConsultationAllergyModel: ArrayList<LstConsultationAllergyModel> = ArrayList(),
    val lstConsultationImagesModel: ArrayList<LstConsultationImagesModel> = ArrayList(),
    val lstConsultationMessageModel: ArrayList<LstConsultationMessageModel> = ArrayList(),
    val lstConsultationProblemsModel: ArrayList<LstConsultationProblemsModel> = ArrayList(),
    val lstConsultationTestResultsModel: ArrayList<LstConsultationTestResultsModel> = ArrayList(),
    val lstConsultationMedicineModel: ArrayList<LstConsultationMedicineModel> = ArrayList()
)

open class DraftConsultationRequest2(
    val consultationModel: ConsultationModel? = null,
    val patientConsultationModel: PatientConsultationModel2? = null,
    val lstConsultationAllergyModel: ArrayList<LstConsultationAllergyModel> = ArrayList(),
    val lstConsultationImagesModel: ArrayList<LstConsultationImagesModel> = ArrayList(),
    val lstConsultationMessageModel: ArrayList<LstConsultationMessageModel> = ArrayList(),
    val lstConsultationProblemsModel: ArrayList<LstConsultationProblemsModel> = ArrayList(),
    val lstConsultationTestResultsModel: ArrayList<LstConsultationTestResultsModel> = ArrayList(),
    val lstConsultationMedicineModel: ArrayList<LstConsultationMedicineModel> = ArrayList()
)

open class InsertConsultationRequest(
    val consultationModel: ConsultationModel? = null,
    val consultationMessageModel: LstConsultationMessageModel = LstConsultationMessageModel(),
    val lstConsultationMedicineModel: ArrayList<LstConsultationMedicineModel> = ArrayList(),
    val lstConsultationDiagnosisModel: ArrayList<LstConsultationDiagnosisModel> = ArrayList()
)

open class ConsultationModel {
    var consultationId: Int = 0
    var patientInfoId: String = ""
    var patientName: String = ""
    var gender: String = ""
    var genderId: String = ""
    var PatientDOB: String = ""
    var mobileNumber: String = ""
    var StateId: String = ""
    var patientAddress: String = ""
    var CRNumber: String = ""
    var physicalExamination: String = ""
    var systemicExamination: String = ""
    var additionalMedicine: String = ""
    var additionalDiagnosis: String = ""
    var isDiabetic: Boolean = false
    var isSmoker: Boolean = false
    var isAlcoholic: Boolean = false
    var isHypertension: Boolean = false
    var isWronglySent: Boolean = false
    var isInadequate: Boolean = false
    var isCovid: Boolean = false
    var additionalAllergy: String = ""
    var additionalProblem: String = ""
    var dob: String = ""
    var queryDesc: String = ""
    var gerneralExamination: String = ""
    var requestTo = 0
    var requestFrom = 0
    var advice = ""
    var statusId = 0
}

open class PatientConsultationModel() {
    var patientInfoId: String = ""
    var patientFirstName: String = ""
    var patientLastName: String = ""
    var patientGenderId: String = ""
    var PatientDOB: String = ""
    var patientMobile: String = ""
    var StateId: String = ""
    var patientAddress: String = ""
    val crNumber: String = ""
}

open class PatientConsultationModelResponse() {
    var patientInfoId: String = ""
    var patientFirstName: String = ""
    var patientLastName: String = ""
    var patientGenderId: String = ""
    var PatientDOB: String = ""
    var patientMobile: String = ""
    var StateId: String = ""
    var patientAddress: String = ""
    val crNumber: String = ""
    val patientAge: Int = 0
    val patientStateName: String? = ""
    var patientAddressLine1: String? = ""
    var patientDistrictName: String? = ""
    var patientPinCode: String? = ""
    val closeTime: String = ""
}

open class PatientConsultationModel2() {
    var consultationId: String = ""
    var patientInfoId: String = ""
    var patientFirstName: String = ""
    var patientLastName: String = ""
    var patientGenderId: String = ""
    var PatientDOB: String = ""
    var patientMobile: String = ""
    var StateId: String = ""
    var patientAddress: String = ""
    var crNumber: String = ""
}

@Parcelize
open class LstConsultationAllergyModel(
    val name: String = "",
    val isStill: String = "",
    val consultationAllergyId: String = "",
    val severityTypeId: Int = 0,
    val durationId: Int = 0,
    val allergyDuration: String = "",
    val code: String = "",
    val allergySeverityName: String = ""
) : Parcelable

open class LstConsultationImagesModel() {
    var filePath: String = ""
    var fileName: String = ""
    var fileFlag: String = ""
}

open class LstConsultationMessageModel() {
    var message: String? = ""
    var provisionalDiagnosis = ""
    var requestTo = 0
    var consultationId = 0
    var consultationMessageId = 0

}

open class LstConsultationProblemsModel(
    val name: String = "",
    val consultationProblemId: String = "",
    val code: String = ""
)

open class LstConsultationTestResultsModel(
    val name: String = "",
    val categoryName: String = "",
    val units: String? = "",
    val testId: Int = 0,
    val result: String = ""
)

open class LstConsultationMedicineModel(
    val dosageType: String = "",
    val durationtype: String = "",
    val durationvalue: String = "",
    val frequency: String = "",
    val quant: String = "",
    val rxId: String = "",
    val rxName: String = ""
)

open class LstConsultationDiagnosisModel(
    val code: String = "",
    val name: String = ""
)

class ToMembersPrescription {
    var memberId: Long? = null
    var firstName: String? = null
    var middleName: String? = null
    var lastName: String? = null
    var mobile: String? = null
    var email: String? = null
    var genderName: String? = null
    var registrationNumber: String? = null
    var addressLine1: String? = null
    var addressLine2: String? = null
    var stateName: String? = null
    var districtName: String? = null
    var cityName: String? = null
    var specialityName: String? = null
    var qualificationName: String? = null
    var pinCode: String? = null
    var signaturePath: String? = null
    var institutionId: Long? = null
    var prefix: String? = null
}

class ToInstitutionPrescription {
    @SerializedName("institutionId")
    @Expose
    var institutionId: Long? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("addressLine1")
    @Expose
    var addressLine1: String? = null

    @SerializedName("addressLine2")
    @Expose
    var addressLine2: String? = null

    @SerializedName("referenceNumber")
    @Expose
    var referenceNumber: String? = null

    @SerializedName("stateName")
    @Expose
    var stateName: String? = null

    @SerializedName("districtName")
    @Expose
    var districtName: String? = null

    @SerializedName("cityName")
    @Expose
    var cityName: String? = null

    //SpecialityName 03-03-2021
    @SerializedName("specialityName")
    @Expose
    var specialityName: String? = null

    @SerializedName("pinCode")
    @Expose
    var pinCode: String? = null

    @SerializedName("mobile")
    @Expose
    var mobile: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("imagePath")
    @Expose
    var imagePath: String? = null

    @SerializedName("institutionTypeName")
    @Expose
    var institutionTypeName: String? = null

    //InstitutionTypeId 03-03-2021
    @SerializedName("institutionTypeId")
    @Expose
    var institutionTypeId: Int? = null

    //declarationNote 03-03-2021
    @SerializedName("declarationNote")
    @Expose
    var declarationNote: String? = null

    /**
     * No args constructor for use in serialization
     */
    constructor() {}

    /**
     * @param institutionId
     * @param districtName
     * @param imagePath
     * @param mobile
     * @param institutionTypeName
     * @param cityName
     * @param referenceNumber
     * @param stateName
     * @param pinCode
     * @param name
     * @param addressLine1
     * @param addressLine2
     * @param email
     * @param specialityName
     * @param institutionTypeId
     * @param declarationNote
     */
    constructor(
        institutionId: Long?,
        name: String?,
        addressLine1: String?,
        addressLine2: String?,
        referenceNumber: String?,
        stateName: String?,
        districtName: String?,
        cityName: String?,
        specialityName: String?,
        pinCode: String?,
        mobile: String?,
        email: String?,
        imagePath: String?,
        institutionTypeName: String?,
        institutionTypeId: Int?,
        declarationNote: String?
    ) : super() {
        this.institutionId = institutionId
        this.name = name
        this.addressLine1 = addressLine1
        this.addressLine2 = addressLine2
        this.referenceNumber = referenceNumber
        this.stateName = stateName
        this.districtName = districtName
        this.cityName = cityName
        this.specialityName = specialityName
        this.pinCode = pinCode
        this.mobile = mobile
        this.email = email
        this.imagePath = imagePath
        this.institutionTypeName = institutionTypeName
        this.institutionTypeId = institutionTypeId
        this.declarationNote = declarationNote
    }
}


