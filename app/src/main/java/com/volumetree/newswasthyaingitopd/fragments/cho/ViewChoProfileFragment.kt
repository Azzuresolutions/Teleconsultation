package com.volumetree.newswasthyaingitopd.fragments.cho

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.databinding.FragmentViewChoProfileBinding
import com.volumetree.newswasthyaingitopd.utils.*

class ViewChoProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentViewChoProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewChoProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.showBottomNavigationView(false)
        binding.tvEditProfile.setOnClickListener(this)
        setData()
    }

    private fun setData() {
        val choData = PrefUtils.getChoData(requireActivity())
        if (choData != null) {
            binding.tvDrName.text = "${choData.firstName} ${choData.lastName}".capitalizeLetter()
            binding.tvDoctorSpeciality.text = choData.specializationName
            binding.tvEmailValue.text = choData.email
            binding.tvPhoneNumberValue.text = choData.mobile
            binding.tvDOBValue.text = choData.dob.fromDOBToStr()
            binding.tvGenderValue.text = choData.genderId.getGenderNameFromId()
            binding.tvSpecializationValue.text = choData.specializationName
            binding.tvQualificationValue.text = choData.qualificationName
            binding.tvExperinecesValue.text = choData.doctor_exp
            binding.imgProfile.loadImageURL(requireActivity(), choData.imagePath, isNoCache = true)
            if (choData.signaturePath.isNotEmpty()) {
                binding.imgSignature.loadImageURL(
                    requireActivity(),
                    choData.signaturePath,
                    isNoCache = true
                )
                binding.tvNotApplicable.visibility = View.GONE
                binding.imgSignature.visibility = View.VISIBLE
            } else {
                binding.tvNotApplicable.visibility = View.VISIBLE
                binding.imgSignature.visibility = View.GONE
            }
            binding.tvMedicalRegistrationValue.text = choData.registrationNumber
            binding.tvStateValue.text = choData.stateName
            binding.tvDistrictValue.text = choData.districtName
            binding.tvCityValue.text = choData.cityName
            binding.tvAddressLineValue.text = choData.addressLine1
            binding.tvZipCodeValue.text = choData.pinCode
        }
    }

    override fun onClick(clickedView: View) {
        when (clickedView.id) {
            R.id.tvEditProfile -> {
                findNavController().navigate(ViewChoProfileFragmentDirections.actionEditChoProfile())
            }
        }
    }

}