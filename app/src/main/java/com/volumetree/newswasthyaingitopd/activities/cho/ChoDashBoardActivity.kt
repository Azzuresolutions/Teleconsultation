package com.volumetree.newswasthyaingitopd.activities.cho

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.activities.BaseActivity
import com.volumetree.newswasthyaingitopd.databinding.ActivityChoDashboardBinding
import com.volumetree.newswasthyaingitopd.utils.observeOnce
import com.volumetree.newswasthyaingitopd.viewmodel.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class ChoDashBoardActivity : BaseActivity(), View.OnClickListener {

    var binding: ActivityChoDashboardBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChoDashboardBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
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