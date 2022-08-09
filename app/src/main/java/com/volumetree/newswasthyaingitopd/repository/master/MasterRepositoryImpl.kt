package com.volumetree.newswasthyaingitopd.repository.master

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.volumetree.newswasthyaingitopd.model.requestData.cho.CancelTokenRequest
import com.volumetree.newswasthyaingitopd.model.requestData.cho.ChangeQueueRequest
import com.volumetree.newswasthyaingitopd.model.requestData.cho.RatingRequest
import com.volumetree.newswasthyaingitopd.model.requestData.cho.UploadProfilePicRequest
import com.volumetree.newswasthyaingitopd.model.responseData.cho.ConsultationOnlineDoctorResponse
import com.volumetree.newswasthyaingitopd.model.responseData.comman.*
import com.volumetree.newswasthyaingitopd.model.responseData.doctor.DoctorProfileResponse
import com.volumetree.newswasthyaingitopd.model.responseData.patient.DoctorSpecializationResponse
import com.volumetree.newswasthyaingitopd.model.responseData.patient.SpecialityResponse
import com.volumetree.newswasthyaingitopd.retrofit.CommonServices
import com.volumetree.newswasthyaingitopd.retrofit.CustomCB
import com.volumetree.newswasthyaingitopd.utils.PrefUtils
import com.volumetree.newswasthyaingitopd.utils.isNetworkConnected

open class MasterRepositoryImpl(private val retrofitService: CommonServices, val context: Context) :
    MasterRepository {

    override fun getDistrictMaster(): MutableLiveData<DistrictResponse> {
        val liveData = MutableLiveData<DistrictResponse>()
        if (context.isNetworkConnected()) {
            retrofitService.getDistrictMaster(19).enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                override fun onResponse(bodyResponse: Any?) {
                    liveData.postValue(bodyResponse as DistrictResponse)
                }
            }, context))
        }
        return liveData
    }

    override fun getCityMaster(distId: Int): MutableLiveData<CityResponse> {
        val liveData = MutableLiveData<CityResponse>()
        if (context.isNetworkConnected()) {
            retrofitService.getCityMaster(distId).enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                override fun onResponse(bodyResponse: Any?) {
                    liveData.postValue(bodyResponse as CityResponse)
                }
            }, context))
        }
        return liveData
    }

    override fun getBlockMaster(distId: Int): MutableLiveData<BlockResponse> {
        val liveData = MutableLiveData<BlockResponse>()
        if (context.isNetworkConnected()) {
            retrofitService.getBlockMasterByDistrict(distId)
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        liveData.postValue(bodyResponse as BlockResponse)
                    }
                }, context))
        }
        return liveData
    }

    override fun getSpecialityMaster(): MutableLiveData<SpecialityResponse> {
        val liveData = MutableLiveData<SpecialityResponse>()
        if (context.isNetworkConnected()) {
            retrofitService.getSpeciality()
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        liveData.postValue(bodyResponse as SpecialityResponse)
                    }
                }, context))
        }
        return liveData
    }

    override fun getDoctorBySpecialization(
        specialityId: Int,
        searchName: String
    ): MutableLiveData<DoctorSpecializationResponse> {
        val liveData = MutableLiveData<DoctorSpecializationResponse>()
        if (context.isNetworkConnected()) {
            if (PrefUtils.getLogin(context) == 3) {
                if (searchName.isEmpty()) {
                    retrofitService.getOnlineDoctorBySpecialization(
                        specialityId, 100
                    )
                } else {
                    retrofitService.getOnlineDoctorBySpecialization(
                        specialityId, 100, searchName
                    )
                }
            } else {
                retrofitService.getDoctorBySpecialization(specialityId)
            }.enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                override fun onResponse(bodyResponse: Any?) {
                    liveData.postValue(bodyResponse as DoctorSpecializationResponse)
                }
            }, context))
        }
        return liveData
    }

    override fun consultationOnlineDoctor(
        consultationId: String,
        patientInfo: String,
        specialityId: Int,
        doctorId: String
    ): MutableLiveData<ConsultationOnlineDoctorResponse> {
        val liveData = MutableLiveData<ConsultationOnlineDoctorResponse>()
        if (context.isNetworkConnected()) {
            retrofitService.consultationOnlineDoctor(
                ConsultationId = consultationId,
                PatientInfoId = patientInfo,
                spl_id = specialityId,
                doctorId
            )
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        liveData.postValue(bodyResponse as ConsultationOnlineDoctorResponse)
                    }
                }, context))
        }
        return liveData
    }

    override fun getBloodGroup(): MutableLiveData<BloodGroupResponse> {
        val liveData = MutableLiveData<BloodGroupResponse>()
        if (context.isNetworkConnected()) {
            retrofitService.getBloodGroup()
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        liveData.postValue(bodyResponse as BloodGroupResponse)
                    }
                }, context))
        }
        return liveData
    }

    override fun getMaritalStatus(): MutableLiveData<MaritalStatusResponse> {
        val liveData = MutableLiveData<MaritalStatusResponse>()
        if (context.isNetworkConnected()) {
            retrofitService.getMaritalStatus()
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        liveData.postValue(bodyResponse as MaritalStatusResponse)
                    }
                }, context))
        }
        return liveData
    }

    override fun getDuration(): MutableLiveData<DurationResponse> {
        val liveData = MutableLiveData<DurationResponse>()
        if (context.isNetworkConnected()) {
            retrofitService.getDuration()
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        liveData.postValue(bodyResponse as DurationResponse)
                    }
                }, context))
        }
        return liveData
    }

    override fun getSeverity(): MutableLiveData<SeverityResponse> {
        val liveData = MutableLiveData<SeverityResponse>()
        if (context.isNetworkConnected()) {
            retrofitService.getSeverity()
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        liveData.postValue(bodyResponse as SeverityResponse)
                    }
                }, context))
        }
        return liveData
    }

    override fun getAllergy(searchText: String): MutableLiveData<ArrayList<AllergyData>> {
        val liveData = MutableLiveData<ArrayList<AllergyData>>()
        if (context.isNetworkConnected()) {
            retrofitService.getAllergy(searchText)
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        liveData.postValue(bodyResponse as ArrayList<AllergyData>)
                    }
                }, context))
        }
        return liveData
    }

    override fun getProblem(searchText: String): MutableLiveData<ArrayList<AllergyData>> {
        val liveData = MutableLiveData<ArrayList<AllergyData>>()
        if (context.isNetworkConnected()) {
            retrofitService.getProblemType(searchText)
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        liveData.postValue(bodyResponse as ArrayList<AllergyData>)
                    }
                }, context))
        }
        return liveData
    }

    override fun getDiagnosis(searchText: String): MutableLiveData<ArrayList<AllergyData>> {
        val liveData = MutableLiveData<ArrayList<AllergyData>>()
        if (context.isNetworkConnected()) {
            retrofitService.getDiagnosis(searchText)
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        liveData.postValue(bodyResponse as ArrayList<AllergyData>)
                    }
                }, context))
        }
        return liveData
    }

    override fun getVital(): MutableLiveData<VitalResponse> {
        val liveData = MutableLiveData<VitalResponse>()
        if (context.isNetworkConnected()) {
            retrofitService.getVitals()
                .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        liveData.postValue(bodyResponse as VitalResponse)
                    }
                }, context))
        }
        return liveData
    }

    override fun uploadProfilePic(uploadProfilePicRequest: UploadProfilePicRequest): MutableLiveData<ImageUploadResponse> {
        val liveData = MutableLiveData<ImageUploadResponse>()
        if (context.isNetworkConnected()) {
            retrofitService.uploadProfilePic(uploadProfilePicRequest)
                .enqueue(
                    CustomCB(object : CustomCB.OnAPIResponse {
                        override fun onResponse(bodyResponse: Any?) {
                            liveData.postValue(bodyResponse as ImageUploadResponse)
                        }
                    }, context)
                )
        }
        return liveData
    }

    override fun uploadSignatureImage(uploadProfilePicRequest: UploadProfilePicRequest): MutableLiveData<ImageUploadResponse> {
        val liveData = MutableLiveData<ImageUploadResponse>()
        if (context.isNetworkConnected()) {
            retrofitService.uploadSignatureImage(uploadProfilePicRequest)
                .enqueue(
                    CustomCB(object : CustomCB.OnAPIResponse {
                        override fun onResponse(bodyResponse: Any?) {
                            liveData.postValue(bodyResponse as ImageUploadResponse)
                        }
                    }, context)
                )
        }
        return liveData
    }

    override fun patientRating(ratingRequest: RatingRequest): MutableLiveData<BaseResponse> {
        val liveData = MutableLiveData<BaseResponse>()
        if (context.isNetworkConnected()) {
            retrofitService.patientRating(ratingRequest)
                .enqueue(
                    CustomCB(object : CustomCB.OnAPIResponse {
                        override fun onResponse(bodyResponse: Any?) {
                            liveData.postValue(bodyResponse as BaseResponse)
                        }
                    }, context)
                )
        }
        return liveData
    }

    override fun getConsultationsDayDashboard(
        DWYLabel: Int,
        SendRec: Int
    ): MutableLiveData<DashboardConsultationsReportsResponse> {
        val liveData = MutableLiveData<DashboardConsultationsReportsResponse>()
        if (context.isNetworkConnected()) {
            retrofitService.getConsultationsDayDashboard(DWYLabel, SendRec)
                .enqueue(
                    CustomCB(object : CustomCB.OnAPIResponse {
                        override fun onResponse(bodyResponse: Any?) {
                            liveData.postValue(bodyResponse as DashboardConsultationsReportsResponse)
                        }
                    }, context)
                )
        }
        return liveData
    }

    override fun getConsultationsVC(
        DWYLabel: Int,
        SendRec: Int
    ): MutableLiveData<DashboardConsultationsReportsResponse> {
        val liveData = MutableLiveData<DashboardConsultationsReportsResponse>()
        retrofitService.getConsultationsVC(DWYLabel, SendRec)
            .enqueue(
                CustomCB(object : CustomCB.OnAPIResponse {
                    override fun onResponse(bodyResponse: Any?) {
                        liveData.postValue(bodyResponse as DashboardConsultationsReportsResponse)
                    }
                }, context)
            )
        return liveData
    }

    override fun getConsultationsGenderDashboard(
        SendRec: Int
    ): MutableLiveData<DashboardConsultationsReportsResponse> {
        val liveData = MutableLiveData<DashboardConsultationsReportsResponse>()
        if (context.isNetworkConnected()) {
            retrofitService.getConsultationsGenderDashboard(SendRec)
                .enqueue(
                    CustomCB(object : CustomCB.OnAPIResponse {
                        override fun onResponse(bodyResponse: Any?) {
                            liveData.postValue(bodyResponse as DashboardConsultationsReportsResponse)
                        }
                    }, context)
                )
        }
        return liveData
    }


    override fun getConsultationsDashboard(
        SendRec: Int
    ): MutableLiveData<DashboardConsultationsResponse> {
        val liveData = MutableLiveData<DashboardConsultationsResponse>()
        if (context.isNetworkConnected()) {
            retrofitService.getConsultationsDashboard(SendRec)
                .enqueue(
                    CustomCB(object : CustomCB.OnAPIResponse {
                        override fun onResponse(bodyResponse: Any?) {
                            liveData.postValue(bodyResponse as DashboardConsultationsResponse)
                        }
                    }, context)
                )
        }
        return liveData
    }

    override fun cancelDeleteToken(
        cancelTokenRequest: CancelTokenRequest
    ): MutableLiveData<BaseResponse> {
        val liveData = MutableLiveData<BaseResponse>()
        if (context.isNetworkConnected()) {
            retrofitService.cancelDeleteToken(cancelTokenRequest)
                .enqueue(
                    CustomCB(object : CustomCB.OnAPIResponse {
                        override fun onResponse(bodyResponse: Any?) {
                            liveData.postValue(bodyResponse as BaseResponse)
                        }
                    }, context)
                )
        }
        return liveData
    }

    override fun changePatientQueue(
        changeQueueRequest: ChangeQueueRequest
    ): MutableLiveData<BaseResponse> {
        val liveData = MutableLiveData<BaseResponse>()
        if (context.isNetworkConnected()) {
            retrofitService.changePatientQueue(changeQueueRequest)
                .enqueue(
                    CustomCB(object : CustomCB.OnAPIResponse {
                        override fun onResponse(bodyResponse: Any?) {
                            liveData.postValue(bodyResponse as BaseResponse)
                        }
                    }, context)
                )
        }
        return liveData
    }


    override fun getDoctorProfileById(
        doctorId: Int
    ): MutableLiveData<DoctorProfileResponse.DoctorProfileResponse> {
        val liveData = MutableLiveData<DoctorProfileResponse.DoctorProfileResponse>()
        if (context.isNetworkConnected()) {
            retrofitService.getDoctorProfileById(doctorId)
                .enqueue(
                    CustomCB(object : CustomCB.OnAPIResponse {
                        override fun onResponse(bodyResponse: Any?) {
                            liveData.postValue(bodyResponse as DoctorProfileResponse.DoctorProfileResponse)
                        }
                    }, context)
                )
        }
        return liveData
    }


    override fun chatMessageByConsultationId(
        doctorId: Int
    ): MutableLiveData<ChatMessagesData> {
        val liveData = MutableLiveData<ChatMessagesData>()
        if (context.isNetworkConnected()) {
            retrofitService.chatMessageByConsultationId(doctorId)
                .enqueue(
                    CustomCB(object : CustomCB.OnAPIResponse {
                        override fun onResponse(bodyResponse: Any?) {
                            liveData.postValue(bodyResponse as ChatMessagesData)
                        }
                    }, context)
                )
        }
        return liveData
    }

    override fun getMedicine(searchText: String): MutableLiveData<ArrayList<AllergyData>> {
        val liveData = MutableLiveData<ArrayList<AllergyData>>()
        retrofitService.getMedicineType(searchText)
            .enqueue(CustomCB(object : CustomCB.OnAPIResponse {
                override fun onResponse(bodyResponse: Any?) {
                    liveData.postValue(bodyResponse as ArrayList<AllergyData>)
                }
            }, context))
        return liveData
    }
}