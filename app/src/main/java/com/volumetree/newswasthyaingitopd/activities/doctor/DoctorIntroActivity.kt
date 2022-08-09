package com.volumetree.newswasthyaingitopd.activities.doctor

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.activities.BaseActivity
import com.volumetree.newswasthyaingitopd.databinding.ActivityDoctorIntroBinding
import com.volumetree.newswasthyaingitopd.utils.Constants
import com.volumetree.newswasthyaingitopd.utils.PrefUtils
import com.volumetree.newswasthyaingitopd.utils.loadImageURL

class DoctorIntroActivity : BaseActivity() {

    private lateinit var binding: ActivityDoctorIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setDoctorInfo()
        Handler().postDelayed({ moveToNextActivity() }, 3000)
    }

    private fun setDoctorInfo() {
        val doctorData = PrefUtils.getDoctorData(this)
        if (doctorData != null) {
            binding.drName.text =
                "${doctorData.firstName} ${doctorData.lastName} ${getString(R.string.welcome_to_swasthya_ingit2)}"
            binding.imgProfile.loadImageURL(
                this,
                doctorData.imagePath,
                isNoCache = true,
                doctor = true
            )

        }
    }

    private fun moveToNextActivity() {
        startActivity(
            Intent(
                this,
                DoctorDashBoardActivity::class.java
            )
        )
        finish()
    }


}