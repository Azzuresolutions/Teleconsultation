package com.volumetree.newswasthyaingitopd.fragments.patient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.adapters.ConsultationAdapter
import com.volumetree.newswasthyaingitopd.databinding.FragmentConsultationBinding
import com.volumetree.newswasthyaingitopd.model.responseData.cho.ConsultationOnlineDoctorResponse
import com.volumetree.newswasthyaingitopd.utils.CommonDialog
import com.volumetree.newswasthyaingitopd.utils.setVerticalLayoutManager

class ConsultationFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentConsultationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConsultationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupConsultation()
    }

    private fun setupConsultation() {
        binding.rvOldConsultation.setVerticalLayoutManager(requireActivity())
        val consultations = ArrayList<String>()
        consultations.add("")
        consultations.add("")
        consultations.add("")
        consultations.add("")
        consultations.add("")
        consultations.add("")
        binding.rvOldConsultation.adapter = ConsultationAdapter(consultations, ::onConsultation)
    }

    private fun onConsultation() {
        CommonDialog.showTokenDialog(
            requireActivity(),
            ConsultationOnlineDoctorResponse(),
            ::onTokenConfirmation
        )
    }

    private fun onTokenConfirmation(consultationOnlineDoctorResponse: ConsultationOnlineDoctorResponse) {
//        findNavController().navigate(ConsultationFragmentDirections.actionWaitingRoom())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(clickedView: View) {
        when (clickedView.id) {
            R.id.imgBack -> {
                findNavController().popBackStack()
            }
        }
    }


}