package com.volumetree.newswasthyaingitopd.fragments.cho

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.adapters.CasesViewPagerAdapter
import com.volumetree.newswasthyaingitopd.databinding.FragmentChoCasesBinding
import com.volumetree.newswasthyaingitopd.enums.CaseType
import com.volumetree.newswasthyaingitopd.model.responseData.cho.CaseData
import com.volumetree.newswasthyaingitopd.model.responseData.cho.ChoPatientData
import com.volumetree.newswasthyaingitopd.model.responseData.cho.ConsultationOnlineDoctorResponse
import com.volumetree.newswasthyaingitopd.utils.showBottomNavigationView

class ChoCasesFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentChoCasesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChoCasesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.showBottomNavigationView(true)
        setupViewPager()
//        setupCaseListAdapter()
    }


    private fun setupViewPager() {

        val fragments =
            mutableListOf(
                ChoCasesListingFragment(CaseType.DRAFTED.type, ::onCreateCase, null),
                ChoCasesListingFragment(CaseType.COMPLETED.type, null, null),
                ChoCasesListingFragment(CaseType.INCOMPLETED.type, null, ::onTryAgain)
            )

        val registeredPatientViewPagerAdapter = CasesViewPagerAdapter(
            requireActivity(),
            fragments.toMutableList(),
        )
        binding.viewpager.adapter = registeredPatientViewPagerAdapter
        binding.viewpager.offscreenPageLimit = 1
        binding.viewpager.isUserInputEnabled = false
        addTabLayoutMediator()

        binding.viewpager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        binding.tvActionbarTitle.text = "All Drafted Cases"
                    }
                    1 -> {
                        binding.tvActionbarTitle.text = "All Completed Cases"
                    }
                    2 -> {
                        binding.tvActionbarTitle.text = "All Incomplete Cases"
                    }
                }
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(clickedView: View) {

    }


    private fun addTabLayoutMediator() {
        val titles = ArrayList<String>()
        titles.add(getString(R.string.draft_title))
        titles.add(getString(R.string.complete_title))
        titles.add(getString(R.string.incomplete_title))

        TabLayoutMediator(
            binding.casesTabLayout, binding.viewpager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = titles[position]
        }.attach()
    }

    private fun onCreateCase(patientData: ChoPatientData, consultationId: Int) {
        lifecycleScope.launchWhenResumed {
            findNavController().navigate(
                ChoCasesFragmentDirections.actionUpdateCase(
                    patientData, consultationId
                )
            )
        }
    }

    private fun onTryAgain(caseData: CaseData) {
        val consultationOnlineDoctorResponse = ConsultationOnlineDoctorResponse()
        consultationOnlineDoctorResponse.model.doctor_id = caseData.sendTo.toInt()
        consultationOnlineDoctorResponse.patientInfoId = caseData.patientInfoID.toInt()
        findNavController().navigate(
            ChoCasesFragmentDirections.actionWaitingRoom(
                consultationOnlineDoctorResponse
            )
        )
    }


}