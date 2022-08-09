package com.volumetree.newswasthyaingitopd.fragments.doctor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.databinding.FragmentViewDrProfileBinding
import com.volumetree.newswasthyaingitopd.utils.*

class ViewDoctorProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentViewDrProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewDrProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.showBottomNavigationView(false)
        binding.tvEditProfile.setOnClickListener(this)
        setData()
    }

    private fun setData() {
        val doctorData = PrefUtils.getDoctorData(requireActivity())
        if (doctorData != null) {
            binding.tvDrName.text =
                "${doctorData.firstName} ${doctorData.lastName}".capitalizeLetter()
            binding.tvDoctorSpeciality.text = doctorData.specializationName
            binding.tvEmailValue.text = doctorData.email
            binding.tvPhoneNumberValue.text = doctorData.mobile
            binding.tvDOBValue.text = doctorData.dob.fromDOBToStr()
            binding.tvGenderValue.text = doctorData.genderId.getGenderNameFromId()
            binding.tvSpecializationValue.text = doctorData.specializationName
            binding.tvQualificationValue.text = doctorData.qualificationName
            binding.tvExperinecesValue.text = doctorData.doctor_exp
            binding.tvMedicalRegistrationValue.text = doctorData.registrationNumber
            binding.tvStateValue.text = doctorData.stateName
            binding.tvDistrictValue.text = doctorData.districtName
            binding.tvCityValue.text = doctorData.cityName
            binding.tvAddressLineValue.text = doctorData.addressLine1
            binding.tvZipCodeValue.text = doctorData.pinCode
            binding.imgProfile.loadImageURL(
                requireActivity(),
                doctorData.imagePath,
                isNoCache = true,
                doctor = true
            )

            if (doctorData.signaturePath.isNotEmpty()) {
                binding.imgSignature.loadImageURL(
                    requireActivity(),
                    doctorData.signaturePath,
                    isNoCache = true
                )
                binding.tvNotApplicable.visibility = View.GONE
                binding.imgSignature.visibility = View.VISIBLE
            } else {
                binding.tvNotApplicable.visibility = View.VISIBLE
                binding.imgSignature.visibility = View.GONE
            }
        }
    }

    override fun onClick(clickedView: View) {
        when (clickedView.id) {
            R.id.tvEditProfile -> {
                findNavController().navigate(ViewDoctorProfileFragmentDirections.actionEditDrProfile())
            }
        }
    }

}