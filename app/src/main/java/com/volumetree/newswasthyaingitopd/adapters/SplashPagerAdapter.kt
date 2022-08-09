package com.volumetree.newswasthyaingitopd.adapters


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.volumetree.newswasthyaingitopd.fragments.patient.SplashInfoFragment
import com.volumetree.newswasthyaingitopd.model.responseData.comman.SplashInfo

class SplashPagerAdapter(
    fragmentManager: FragmentManager,
    private val pagerInfo: ArrayList<SplashInfo>,
) :
    FragmentStatePagerAdapter(fragmentManager) {


    override fun getItem(position: Int): Fragment {
        return SplashInfoFragment(pagerInfo[position])
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence {
        return ""
    }
}