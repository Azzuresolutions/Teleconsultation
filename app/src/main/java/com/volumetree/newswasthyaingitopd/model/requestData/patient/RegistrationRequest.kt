package com.volumetree.newswasthyaingitopd.model.requestData.patient


data class RegistrationRequest(
    val firstName: String,
    val lastName: String,
    val AddressLine1: String,
    val email: String,
    val DOB: String,
    val SourceId: Int,
    val CountryId: Int = 0,
    val StateId: Int = 19,
    val DistrictId: Int,
    val CityId: Int,
    val BlockId: Int,
    val PatientStateId: Int,
    val PinCode: String,
    val Mobile: String,
    val GenderId: String,
)
