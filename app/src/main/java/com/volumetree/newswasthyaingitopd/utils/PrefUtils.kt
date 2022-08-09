package com.volumetree.newswasthyaingitopd.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.volumetree.newswasthyaingitopd.model.responseData.cho.ChoProfileResponse
import com.volumetree.newswasthyaingitopd.model.responseData.doctor.DoctorProfileResponse
import com.volumetree.newswasthyaingitopd.model.responseData.doctor.InstituteModel
import com.volumetree.newswasthyaingitopd.model.responseData.patient.PatientProfileResponse

object PrefUtils {
    private const val PREF_NAME = "MyPref"
    private const val PREF_OTP_LOGIN_DATA = "otp_login_data"
    private const val PREF_USER_DATA = "user_data"
    private const val PREF_CHO_USER_DATA = "cho_user_data"
    private const val PREF_DOCTOR_DATA = "doctor_data"
    private const val PREF_DOCTOR_INSTITUTE_DATA = "doctor_institute_data"
    private const val PREF_LOGIN_TOKEN = "login_token"
    private const val PREF_LOGIN_TYPE = "login_user_type"
    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun setLogin(context: Context, onBoardingFlow: Int) {
        val editor = getSharedPreferences(context).edit()
        editor.putInt(PREF_OTP_LOGIN_DATA, onBoardingFlow).apply()
        if (onBoardingFlow == 0) {
            setLoginToken(context, "")
            setPatientUserData(context, null)
        }
    }

    fun getLogin(context: Context): Int {
        return getSharedPreferences(context).getInt(PREF_OTP_LOGIN_DATA, 0)
    }

    fun setPatientUserData(
        context: Context,
        userModel: PatientProfileResponse.PatientProfileData?
    ) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(PREF_USER_DATA, Gson().toJson(userModel)).apply()
    }

    fun getPatientUserData(context: Context): PatientProfileResponse.PatientProfileData? {
        val strUserModel = getSharedPreferences(context).getString(PREF_USER_DATA, "")
        return try {
            Gson().fromJson(strUserModel, PatientProfileResponse.PatientProfileData::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun setChoData(context: Context, userModel: ChoProfileResponse.ChoProfileData?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(PREF_CHO_USER_DATA, Gson().toJson(userModel)).apply()
    }

    fun getChoData(context: Context): ChoProfileResponse.ChoProfileData? {
        val strUserModel = getSharedPreferences(context).getString(PREF_CHO_USER_DATA, "")
        return try {
            Gson().fromJson(strUserModel, ChoProfileResponse.ChoProfileData::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun setDoctorData(context: Context, userModel: DoctorProfileResponse.DoctorProfileData?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(PREF_DOCTOR_DATA, Gson().toJson(userModel)).apply()
    }

    fun getDoctorData(context: Context): DoctorProfileResponse.DoctorProfileData? {
        val strUserModel = getSharedPreferences(context).getString(PREF_DOCTOR_DATA, "")
        return try {
            Gson().fromJson(strUserModel, DoctorProfileResponse.DoctorProfileData::class.java)
        } catch (e: Exception) {
            null
        }
    }


    fun setDoctorInstituteData(context: Context, instituteModel: InstituteModel?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(PREF_DOCTOR_INSTITUTE_DATA, Gson().toJson(instituteModel)).apply()
    }

    fun getDoctorInstituteData(context: Context): InstituteModel? {
        val strUserModel = getSharedPreferences(context).getString(PREF_DOCTOR_INSTITUTE_DATA, "")
        return try {
            Gson().fromJson(strUserModel, InstituteModel::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun setLoginToken(context: Context, strToken: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(PREF_LOGIN_TOKEN, strToken).apply()
    }

    fun getLoginToken(context: Context): String {
        return getSharedPreferences(context).getString(PREF_LOGIN_TOKEN, "").toString()
    }


    fun setLoginUserType(context: Context, loginUserType: Int) {
        val editor = getSharedPreferences(context).edit()
        editor.putInt(PREF_LOGIN_TYPE, loginUserType).apply()
    }

    fun getLoginUserType(context: Context): Int {
        return getSharedPreferences(context).getInt(PREF_LOGIN_TYPE, 0)
    }

}