package com.volumetree.newswasthyaingitopd.fragments.common

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.adapters.SearchAllergyAdapter
import com.volumetree.newswasthyaingitopd.databinding.FragmentAllergyBinding
import com.volumetree.newswasthyaingitopd.interfaces.CreateCaseListener
import com.volumetree.newswasthyaingitopd.model.responseData.comman.AllergyData
import com.volumetree.newswasthyaingitopd.utils.*
import com.volumetree.newswasthyaingitopd.viewmodel.MasterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllergyFragment(val type: Int, private val caseListener: CreateCaseListener) :
    DialogFragment(),
    View.OnClickListener, TextWatcher {

    private var _binding: FragmentAllergyBinding? = null
    private val binding get() = _binding!!
    private val masterViewModel: MasterViewModel by viewModel()
    private lateinit var allergyAdapter: SearchAllergyAdapter

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (_binding == null) {
            _binding = FragmentAllergyBinding.inflate(inflater, container, false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.showBottomNavigationView(false)
        binding.etSearch.addTextChangedListener(this)
        binding.imgBack.setOnClickListener(this)
        binding.etSearch.showKeyBoard(requireActivity())
    }

    private fun setupAdapter(allergyResponse: ArrayList<AllergyData>) {
        binding.rvAllergies.setVerticalLayoutManager(requireActivity())
        allergyAdapter = SearchAllergyAdapter()
        binding.rvAllergies.adapter = allergyAdapter
        allergyAdapter.addData(allergyResponse)
        allergyAdapter.onItemClick = { selectedObj ->
            when (type) {
                1 -> {
                    caseListener.onAllergySelected(selectedObj)
                }
                2 -> {
                    caseListener.onProblemSelected(selectedObj)
                }
                3 -> {
                    caseListener.onMedicineSelected(selectedObj)
                }
                4 -> {
                    caseListener.onDiagnosisSelected(selectedObj)
                }
            }
            hideKeyboard()
        }
    }

    override fun onClick(clickedView: View) {
        when (clickedView.id) {
            R.id.imgBack -> {
                when (type) {
                    1 -> {
                        caseListener.onAllergySelected(null)
                    }
                    2 -> {
                        caseListener.onProblemSelected(null)
                    }
                    3 -> {
                        caseListener.onMedicineSelected(null)
                    }
                    4 -> {
                        caseListener.onDiagnosisSelected(null)
                    }
                }
                hideKeyboard()
            }
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (binding.etSearch.getTextFromEt().length > 2) {
            when (type) {
                1 -> {
                    masterViewModel.getAllergy(binding.etSearch.getTextFromEt())
                        .observeOnce(viewLifecycleOwner) { allergyResponse ->
                            setupAdapter(allergyResponse)
                        }
                }
                2 -> {
                    masterViewModel.getProblem(binding.etSearch.getTextFromEt())
                        .observeOnce(viewLifecycleOwner) { allergyResponse ->
                            setupAdapter(allergyResponse)
                        }
                }
                3 -> {
                    masterViewModel.getMedicine(binding.etSearch.getTextFromEt())
                        .observeOnce(viewLifecycleOwner) { medicineResponse ->
                            setupAdapter(medicineResponse)
                        }
                }
                4 -> {
                    masterViewModel.getDiagnosis(binding.etSearch.getTextFromEt())
                        .observeOnce(viewLifecycleOwner) { diagnosis ->
                            setupAdapter(diagnosis)
                        }
                }
            }
        }
    }

    override fun afterTextChanged(p0: Editable?) {
    }


}