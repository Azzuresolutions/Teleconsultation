package com.volumetree.newswasthyaingitopd.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.volumetree.newswasthyaingitopd.activities.cho.ChoDashBoardActivity
import com.volumetree.newswasthyaingitopd.activities.doctor.DoctorDashBoardActivity
import com.volumetree.newswasthyaingitopd.activities.patient.CreateProfileActivity
import com.volumetree.newswasthyaingitopd.activities.patient.DashBoardActivity
import com.volumetree.newswasthyaingitopd.databinding.ActivitySplashBinding
import com.volumetree.newswasthyaingitopd.utils.PrefUtils

class SplashActivity : BaseActivity() {

    lateinit var splashBinding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(splashBinding.root)
        Handler().postDelayed({ moveToNextActivity() }, 1500)
    }

    private fun moveToNextActivity() {
        when (PrefUtils.getLogin(this)) {
            1 -> {
                startActivity(Intent(this, DashBoardActivity::class.java))
            }
            2 -> {
                startActivity(Intent(this, CreateProfileActivity::class.java))
            }
            3 -> {
                startActivity(Intent(this, ChoDashBoardActivity::class.java))
            }
            4 -> {
                startActivity(Intent(this, DoctorDashBoardActivity::class.java))
            }
            else -> {
                PrefUtils.setLogin(this, 0)
                startActivity(Intent(this@SplashActivity, SplashInfoActivity::class.java))
            }
        }
        finish()

    }
}