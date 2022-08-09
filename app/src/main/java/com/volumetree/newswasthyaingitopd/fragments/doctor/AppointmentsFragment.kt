package com.volumetree.newswasthyaingitopd.fragments.doctor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.adapters.CasesViewPagerAdapter
import com.volumetree.newswasthyaingitopd.application.App
import com.volumetree.newswasthyaingitopd.databinding.FragmentDoctorAppointmentBinding
import com.volumetree.newswasthyaingitopd.enums.CaseType
import com.volumetree.newswasthyaingitopd.model.responseData.cho.CaseData
import com.volumetree.newswasthyaingitopd.utils.dialogPrescriptionViewSync
import com.volumetree.newswasthyaingitopd.utils.observeOnce
import com.volumetree.newswasthyaingitopd.utils.showBottomNavigationView
import com.volumetree.newswasthyaingitopd.viewmodel.ChoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AppointmentsFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentDoctorAppointmentBinding? = null
    private val binding get() = _binding!!
    private val choViewModel: ChoViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoctorAppointmentBinding.inflate(inflater, container, false)
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
                DoctorCasesListingFragment(CaseType.COMPLETED.type, ::viewCaseDetails, null),
                DoctorCasesListingFragment(CaseType.INCOMPLETED.type, null, ::addPrescription)
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
                        binding.tvActionbarTitle.text = "All Completed Cases"
                    }
                    1 -> {
                        binding.tvActionbarTitle.text = "All Incomplete Cases"
                    }
                }
            }
        })
    }

    private fun addPrescription(caseData: CaseData) {
        App.consultationId = caseData.consultationId.toInt()
        DoctorRxFragment.toUserId == ""
        DoctorRxFragment.remoteUserName = "${caseData.patientFirstName} ${caseData.patientLastName}"
        findNavController().navigate(AppointmentsFragmentDirections.doctorRxFragment())
    }

    private fun viewCaseDetails(consultationId: Int) {
        choViewModel.getConsultation(consultationId).observeOnce(this) { prescriptionData ->
            requireActivity().dialogPrescriptionViewSync(prescriptionData, ::onPrescriptionDismiss)
        }
    }

    private fun onPrescriptionDismiss() {
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(clickedView: View) {

    }


    private fun addTabLayoutMediator() {
        val titles = ArrayList<String>()
        titles.add(getString(R.string.complete_title))
        titles.add(getString(R.string.incomplete_title))

        TabLayoutMediator(
            binding.casesTabLayout, binding.viewpager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = titles[position]
        }.attach()
    }

}