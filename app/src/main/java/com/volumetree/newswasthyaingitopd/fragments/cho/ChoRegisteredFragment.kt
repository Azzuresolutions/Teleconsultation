package com.volumetree.newswasthyaingitopd.fragments.cho

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.activities.cho.ChoDashBoardActivity
import com.volumetree.newswasthyaingitopd.adapters.RegisteredPatientViewPagerAdapter
import com.volumetree.newswasthyaingitopd.databinding.FragmentChoRegisteredBinding
import com.volumetree.newswasthyaingitopd.fragments.cho.CreateCaseFragment.Companion.isFromDraft
import com.volumetree.newswasthyaingitopd.model.responseData.cho.ChoPatientData
import com.volumetree.newswasthyaingitopd.utils.showBottomNavigationView

class ChoRegisteredFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentChoRegisteredBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChoRegisteredBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.showBottomNavigationView(true)
        setupViewPager()
    }


    private fun setupViewPager() {

        val fragments =
            mutableListOf(
                ChoMySkRegisteredFragment(
                    ::onCreateCase,
                    ::onViewHistory,
                    ::onEdit
                ),
                ChoOtherSkRegisteredFragment(
                    ::onCreateCase,
                    ::onViewHistory,
                    ::onEdit
                )
            )

        val registeredPatientViewPagerAdapter = RegisteredPatientViewPagerAdapter(
            requireActivity(),
            fragments,
        )
        binding.viewpager.adapter = registeredPatientViewPagerAdapter
        binding.viewpager.offscreenPageLimit = 2
        binding.viewpager.isUserInputEnabled = false
        addTabLayoutMediator()
    }

    private fun onEdit(patientData: ChoPatientData) {
        val action = ChoRegisteredFragmentDirections.actionCreatePatientCho()
        action.patientId = patientData.patientInfoId.toString()
        lifecycleScope.launchWhenResumed {
            findNavController().navigate(
                action
            )
        }
    }

    private fun onCreateCase(patientData: ChoPatientData) {
        lifecycleScope.launchWhenResumed {
            findNavController().navigate(
                ChoRegisteredFragmentDirections.actionCreateCase(
                    patientData, 0
                )
            )
        }
    }

    private fun onViewHistory(patientData: ChoPatientData) {
        lifecycleScope.launchWhenResumed {
            findNavController().navigate(
                ChoRegisteredFragmentDirections.actionCaseHistory(
                    patientData
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addTabLayoutMediator() {
        val titles = ArrayList<String>()
        titles.add(getString(R.string.my_sk_registered))
        titles.add(getString(R.string.other_sk_registered))

        TabLayoutMediator(
            binding.registeredTabLayout, binding.viewpager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = titles[position]
        }.attach()
    }

    override fun onClick(clickedView: View) {
    }

    override fun onResume() {
        super.onResume()
        if (isFromDraft) {
            isFromDraft = false
            (activity as ChoDashBoardActivity).binding?.bottomNavigation?.selectedItemId =
                R.id.action_cases
        }
    }

}