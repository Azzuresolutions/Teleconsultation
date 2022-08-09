package com.volumetree.newswasthyaingitopd.fragments.doctor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.adapters.VitalListAdapter
import com.volumetree.newswasthyaingitopd.databinding.FragmentShowAllergiesListBinding
import com.volumetree.newswasthyaingitopd.interfaces.CreateCaseListener
import com.volumetree.newswasthyaingitopd.model.requestData.cho.LstConsultationTestResultsModel
import com.volumetree.newswasthyaingitopd.utils.setVerticalLayoutManager

class VitalListFragment(
    private val selectedVitals: ArrayList<LstConsultationTestResultsModel>,
    private val createCaseListener: CreateCaseListener
) : DialogFragment(), View.OnClickListener {

    private var _binding: FragmentShowAllergiesListBinding? = null
    private val binding get() = _binding!!
    var vitalAdapter: VitalListAdapter? = null
    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShowAllergiesListBinding.inflate(inflater, container, false)
        binding.layToolbar.tvActionbarTitle.text = getString(R.string.added_vitals)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.layToolbar.imgBack.setOnClickListener(this)
        binding.rvAllergiesList.setVerticalLayoutManager(requireActivity())
        vitalAdapter = VitalListAdapter(selectedVitals)
        binding.rvAllergiesList.adapter = vitalAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(clickedView: View) {
        when (clickedView.id) {
            R.id.imgBack -> {
                vitalAdapter?.vitalList?.let { createCaseListener.onVitalUpdate(it) }
            }
        }
    }
}