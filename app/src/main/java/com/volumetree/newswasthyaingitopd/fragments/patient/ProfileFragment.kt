package com.volumetree.newswasthyaingitopd.fragments.patient

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.activities.SplashInfoActivity
import com.volumetree.newswasthyaingitopd.databinding.FragmentProfileBinding
import com.volumetree.newswasthyaingitopd.utils.PrefUtils
import com.volumetree.newswasthyaingitopd.utils.observeOnce
import com.volumetree.newswasthyaingitopd.utils.showBottomNavigationView
import com.volumetree.newswasthyaingitopd.viewmodel.PatientViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val patientViewModel: PatientViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.showBottomNavigationView(true)
        binding.layProfileMember.setOnClickListener(this)
        binding.layProfileNotification.setOnClickListener(this)
        binding.layProfileLogout.setOnClickListener(this)
        binding.tvEditProfile.setOnClickListener(this)

        getProfileData()
    }

    private fun getProfileData() {
        val loginUserData = PrefUtils.getPatientUserData(requireActivity())
        if (loginUserData != null) {
            binding.tvDrName.text = "${loginUserData.firstName} ${loginUserData.lastName}"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(clickedView: View) {
        when (clickedView.id) {
            R.id.layProfileMember -> {
                patientViewModel.getFamilyMember()
                    .observeOnce(viewLifecycleOwner) { familyMemberResponse ->
                        if (familyMemberResponse.success && !familyMemberResponse.lstModel.isNullOrEmpty()) {
                            findNavController().navigate(ProfileFragmentDirections.memberList())
                        } else {
                            findNavController().navigate(
                                ProfileFragmentDirections.actionAddMember(
                                    null
                                )
                            )
                        }
                    }

            }
            R.id.layProfileNotification -> {
                findNavController().navigate(ProfileFragmentDirections.actionNotification())
            }
            R.id.layProfileLogout -> {
                PrefUtils.setLogin(requireActivity(), 0)
                startActivity(Intent(requireActivity(), SplashInfoActivity::class.java))
                requireActivity().finish()
            }
            R.id.tvEditProfile -> {
                findNavController().navigate(ProfileFragmentDirections.actionEditProfile())
            }
        }
    }


}