package com.volumetree.newswasthyaingitopd.activities.patient

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.activities.BaseActivity
import com.volumetree.newswasthyaingitopd.databinding.ActivityDashboardBinding


class DashBoardActivity : BaseActivity(), View.OnClickListener {

    var dashboardBinding: ActivityDashboardBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dashboardBinding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(dashboardBinding!!.root)
        val navController: NavController =
            Navigation.findNavController(this, R.id.fcvMain)
        val bottomNavigationView =
            findViewById<BottomNavigationView>(R.id.bottomNavigation)
        setupWithNavController(bottomNavigationView, navController)

    }

    override fun onClick(clickedView: View) {
        when (clickedView.id) {
            R.id.btnProceed -> {
            }
        }
    }

}