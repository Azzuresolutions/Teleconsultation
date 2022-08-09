package com.volumetree.newswasthyaingitopd.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.activities.cho.ChoLoginActivity
import com.volumetree.newswasthyaingitopd.databinding.ActivitySelectLoginTypeBinding
import com.volumetree.newswasthyaingitopd.enums.UserTypes
import com.volumetree.newswasthyaingitopd.utils.Constants
import com.volumetree.newswasthyaingitopd.utils.PrefUtils

class SelectLoginTypeActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySelectLoginTypeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectLoginTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvLoginAsDoctor.setOnClickListener(this)
        binding.tvLoginAsPatient.setOnClickListener(this)
        binding.tvLoginAsHWC.setOnClickListener(this)
    }

    override fun onClick(clickedView: View) {
        when (clickedView.id) {
            R.id.tvLoginAsDoctor -> {
                PrefUtils.setLoginUserType(this, UserTypes.DOCTOR.type)
                startActivity(
                    Intent(this, LoginActivity::class.java)
                )
                finish()
            }
            R.id.tvLoginAsPatient -> {
                PrefUtils.setLoginUserType(this, UserTypes.PATIENT.type)
                startActivity(
                    Intent(this, LoginActivity::class.java)
                )
                finish()
            }
            R.id.tvLoginAsHWC -> {
                PrefUtils.setLoginUserType(this, UserTypes.CHO.type)
                startActivity(
                    Intent(this, ChoLoginActivity::class.java)
                )
                finish()
            }
        }
    }

}