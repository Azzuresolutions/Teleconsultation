package com.volumetree.newswasthyaingitopd.fragments.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.volumetree.newswasthyaingitopd.activities.ChatCallActivity
import com.volumetree.newswasthyaingitopd.adapters.ChatPrescriptionListAdapter
import com.volumetree.newswasthyaingitopd.databinding.FragmentChatPrescriptionBinding
import com.volumetree.newswasthyaingitopd.model.responseData.comman.PrescriptionData
import com.volumetree.newswasthyaingitopd.utils.dialogPrescriptionViewSync
import com.volumetree.newswasthyaingitopd.utils.observeOnce
import com.volumetree.newswasthyaingitopd.utils.setVerticalLayoutManager
import com.volumetree.newswasthyaingitopd.viewmodel.ChoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChatPrescriptionFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentChatPrescriptionBinding? = null
    private val binding get() = _binding!!
    private val choViewModel: ChoViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatPrescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onResume() {
        super.onResume()
        if (ChatCallActivity.lastConsultationId > 0) {
            binding.tvNoPrescription.visibility = View.GONE
            binding.rvPrescription.visibility = View.VISIBLE
            setupPrescriptionListAdapter()
        } else {
            binding.tvNoPrescription.visibility = View.VISIBLE
            binding.rvPrescription.visibility = View.GONE
        }
    }

    private fun setupPrescriptionListAdapter() {
        binding.rvPrescription.setVerticalLayoutManager(requireActivity())
        val prescriptionData = ArrayList<PrescriptionData>()
        prescriptionData.add(
            PrescriptionData(
                doctorName = ChatCallActivity.consultDoctorName,
                consultationId = ChatCallActivity.lastConsultationId,
                consultationDate = ChatCallActivity.lastConsultationTime
            )
        )
        binding.rvPrescription.adapter =
            ChatPrescriptionListAdapter(prescriptionData, ::onPrescriptionClick)
    }

    private fun onPrescriptionClick(consultationId: Int) {
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
}