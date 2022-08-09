package com.volumetree.newswasthyaingitopd.activities.doctor

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.activities.BaseActivity
import com.volumetree.newswasthyaingitopd.application.App
import com.volumetree.newswasthyaingitopd.databinding.ActivityDoctorDashboardBinding
import com.volumetree.newswasthyaingitopd.utils.SignalR


class DoctorDashBoardActivity : BaseActivity(), View.OnClickListener {

    var binding: ActivityDoctorDashboardBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorDashboardBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        val navController: NavController =
            Navigation.findNavController(this, R.id.fcvMain)
        val bottomNavigationView =
            findViewById<BottomNavigationView>(R.id.bottomNavigation)
        setupWithNavController(bottomNavigationView, navController)
        (application as App).setupSignalR()
    }

    override fun onClick(clickedView: View) {
        when (clickedView.id) {
            R.id.btnProceed -> {
            }
        }
    }

}