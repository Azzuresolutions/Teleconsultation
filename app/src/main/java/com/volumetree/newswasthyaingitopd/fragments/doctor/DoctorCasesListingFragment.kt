package com.volumetree.newswasthyaingitopd.fragments.doctor

import PaginationScrollListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.volumetree.newswasthyaingitopd.adapters.DoctorCasesListAdapter
import com.volumetree.newswasthyaingitopd.databinding.FragmentChoCasesListingBinding
import com.volumetree.newswasthyaingitopd.model.requestData.cho.GetCaseRequest
import com.volumetree.newswasthyaingitopd.model.responseData.cho.CaseData
import com.volumetree.newswasthyaingitopd.utils.getTextFromEt
import com.volumetree.newswasthyaingitopd.utils.hideKeyboard
import com.volumetree.newswasthyaingitopd.utils.observeOnce
import com.volumetree.newswasthyaingitopd.utils.setVerticalLayoutManager
import com.volumetree.newswasthyaingitopd.viewmodel.DoctorViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DoctorCasesListingFragment(
    private val caseType: Int,
    val viewDetails: ((Int) -> Unit)?,
    val addPrescription: ((CaseData) -> Unit)?
) : Fragment() {

    private var _binding: FragmentChoCasesListingBinding? = null
    private val binding get() = _binding!!
    private val doctorViewModel: DoctorViewModel by viewModel()
    private val recordLimit = 10
    private var pageCount = 1
    lateinit var casesListAdapter: DoctorCasesListAdapter
    private var loading = false
    private var lastPage = false

    private val totalCases: ArrayList<CaseData> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChoCasesListingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        binding.etSearch.setOnEditorActionListener { textView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                totalCases.clear()
                pageCount = 0
                getCases()
                true
            }
            false
        }
        getCases()
    }

    private fun setupAdapter() {
        binding.rvCases.setVerticalLayoutManager(requireActivity())
        casesListAdapter = DoctorCasesListAdapter(
            ArrayList(), viewDetails, addPrescription
        )
        binding.rvCases.adapter = casesListAdapter
        binding.rvCases.addOnScrollListener(object : PaginationScrollListener(
            binding.rvCases.layoutManager as LinearLayoutManager
        ) {
            override fun loadMoreItems() {
                pageCount++
                loading = true
                getCases()
            }

            override val isLastPage: Boolean
                get() = lastPage
            override val isLoading: Boolean
                get() = loading
        })
    }

    private fun getCases() {
        hideKeyboard()
        doctorViewModel.getDoctorAppointments(
            GetCaseRequest(
                aParameter = caseType,
                itemsPerPage = recordLimit,
                currentPage = pageCount,
                skip = totalCases.size,
                EnterSearch = binding.etSearch.getTextFromEt(),
                searchWord = binding.etSearch.getTextFromEt()
            )
        ).observeOnce(viewLifecycleOwner) { caseResponse ->
            loading = false
            if (caseResponse.lstModel != null) {
                if (caseResponse.lstModel.isNotEmpty()) {
                    binding.tvNoCase.visibility = View.GONE
                    binding.rvCases.visibility = View.VISIBLE
                    totalCases.addAll(caseResponse.lstModel)
                }
                if (this::casesListAdapter.isInitialized) {
                    casesListAdapter.addList(totalCases)
                }
            }
            lastPage = totalCases.size >= caseResponse.total_Records
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}