package com.volumetree.newswasthyaingitopd.interfaces

import com.volumetree.newswasthyaingitopd.model.responseData.patient.DoctorSpecializationData
import com.volumetree.newswasthyaingitopd.model.responseData.patient.SpecialityData

interface SelectDoctorAndSpecializationListener {
    fun onSpecialization(specialityData: SpecialityData?)
    fun onSpecialDoctor(doctorSpecializationData: DoctorSpecializationData?)
}