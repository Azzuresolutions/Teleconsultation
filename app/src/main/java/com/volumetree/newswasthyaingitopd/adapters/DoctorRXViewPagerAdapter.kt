package com.volumetree.newswasthyaingitopd.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.volumetree.newswasthyaingitopd.fragments.cho.ChoCasesFragment
import com.volumetree.newswasthyaingitopd.fragments.doctor.DoctorCallPrescription
import com.volumetree.newswasthyaingitopd.fragments.patient.PreviewCase

class DoctorRXViewPagerAdapter(
    fa: FragmentActivity,
    private val listOfTitle: List<String>,
    private val previewCase: PreviewCase,
    private val doctorCallPrescription: DoctorCallPrescription
) :
    FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = listOfTitle.size

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return previewCase
            1 -> return doctorCallPrescription
        }
        return ChoCasesFragment()
    }
}
