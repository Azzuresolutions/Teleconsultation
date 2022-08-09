package com.volumetree.newswasthyaingitopd.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.volumetree.newswasthyaingitopd.fragments.cho.ChoCasesFragment
import com.volumetree.newswasthyaingitopd.fragments.common.ChatCaseRecordFragment
import com.volumetree.newswasthyaingitopd.fragments.common.ChatFragment
import com.volumetree.newswasthyaingitopd.fragments.common.ChatPrescriptionFragment
import com.volumetree.newswasthyaingitopd.fragments.patient.PreviewCase

class ChatCallViewPagerAdapter(
    fa: FragmentActivity,
    private val listOfTitle: List<String>,
    private val chatFragment: ChatFragment,
    private val prescriptionFragment: ChatPrescriptionFragment,
    private val caseRecordFragment: ChatCaseRecordFragment?,
    private val previewCase: PreviewCase?
) :
    FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = listOfTitle.size

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return chatFragment
            1 -> return prescriptionFragment
            2 -> return caseRecordFragment ?: previewCase!!
        }
        return ChoCasesFragment()
    }
}
