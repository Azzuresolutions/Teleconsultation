package com.volumetree.newswasthyaingitopd.fragments.doctor

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.application.App
import com.volumetree.newswasthyaingitopd.databinding.FragmentDrProfileBinding
import com.volumetree.newswasthyaingitopd.utils.*
import com.volumetree.newswasthyaingitopd.viewmodel.DoctorViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DoctorProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentDrProfileBinding? = null
    private val binding get() = _binding!!

    private val doctorViewModel: DoctorViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDrProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.showBottomNavigationView(true)
        binding.layChangeLanguage.setOnClickListener(this)
        binding.layProfileNotification.setOnClickListener(this)
        binding.layProfileLogout.setOnClickListener(this)
        binding.tvEditProfile.setOnClickListener(this)

        getProfileData()
    }

    private fun getProfileData() {
        val doctorData = PrefUtils.getDoctorData(requireActivity())
        if (doctorData != null) {
            binding.tvDrName.text = "${doctorData.firstName} ${doctorData.lastName}"
            binding.tvDoctorSpeciality.text = doctorData.specializationName
            binding.imgProfile.loadImageURL(
                requireActivity(),
                doctorData.imagePath,
                isNoCache = true,
                doctor = true
            )

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(clickedView: View) {
        when (clickedView.id) {
            R.id.layProfileMember -> {

            }
            R.id.layProfileNotification -> {
                findNavController().navigate(DoctorProfileFragmentDirections.actionNotification())
            }
            R.id.layProfileLogout -> {

                requireActivity().confirmationDialog(
                    "Logout",
                    binding.root.context.getString(R.string.logout_confirmation),
                    ::confirmLogout, 0
                )


            }
            R.id.tvEditProfile -> {
                findNavController().navigate(DoctorProfileFragmentDirections.actionViewDoctorProfile())
            }
        }
    }

    private fun confirmLogout(isConfirmLogout: Boolean, i: Int, context: Context) {
        doctorViewModel.doctorLogout().observeOnce(viewLifecycleOwner) {
            (requireActivity().application as App).confirmLogout(isConfirmLogout, "")
        }
    }


}