package com.volumetree.newswasthyaingitopd.fragments.cho

import PaginationScrollListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.volumetree.newswasthyaingitopd.adapters.RegisteredPatientListAdapter
import com.volumetree.newswasthyaingitopd.databinding.FragmentChoMySkRegisteredBinding
import com.volumetree.newswasthyaingitopd.model.responseData.cho.ChoPatientData
import com.volumetree.newswasthyaingitopd.utils.getTextFromEt
import com.volumetree.newswasthyaingitopd.utils.hideKeyboard
import com.volumetree.newswasthyaingitopd.utils.observeOnce
import com.volumetree.newswasthyaingitopd.utils.setVerticalLayoutManager
import com.volumetree.newswasthyaingitopd.viewmodel.ChoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChoOtherSkRegisteredFragment(
    private val createCase: (ChoPatientData) -> Unit,
    private val viewHistory: (ChoPatientData) -> Unit,
    private val edit: (ChoPatientData) -> Unit
) : Fragment() {

    private var _binding: FragmentChoMySkRegisteredBinding? = null
    private val binding get() = _binding!!
    private val choModel: ChoViewModel by viewModel()
    private val recordLimit = 10
    lateinit var registeredPatientListAdapter: RegisteredPatientListAdapter
    private var loading = false
    private var lastPage = false

    private val totalPatients: ArrayList<ChoPatientData> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChoMySkRegisteredBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        checkIsAnyPatientAvailable()
        binding.etSearch.setOnEditorActionListener { textView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                totalPatients.clear()
                getPatients()
                true
            }
            false
        }
        getPatients()
    }

    private fun setupAdapter() {
        binding.rvRegisteredPatient.setVerticalLayoutManager(requireActivity())
        registeredPatientListAdapter =
            RegisteredPatientListAdapter(ArrayList(), createCase, viewHistory, edit)
        binding.rvRegisteredPatient.adapter = registeredPatientListAdapter
        binding.rvRegisteredPatient.addOnScrollListener(object : PaginationScrollListener(
            binding.rvRegisteredPatient.layoutManager as LinearLayoutManager
        ) {
            override fun loadMoreItems() {
                loading = true
                getPatients()
            }

            override val isLastPage: Boolean
                get() = lastPage
            override val isLoading: Boolean
                get() = loading
        })
    }

    private fun getPatients() {
        hideKeyboard()
        choModel.getPatientList(
            binding.etSearch.getTextFromEt(),
            recordLimit,
            totalPatients.size,
            true
        )
            .observeOnce(viewLifecycleOwner) { choPatientResponse ->
                loading = false
                if (choPatientResponse.lstModel != null) {
                    if (choPatientResponse.lstModel.isNotEmpty()) {
                        totalPatients.addAll(choPatientResponse.lstModel)
                    }
                    if (this::registeredPatientListAdapter.isInitialized) {
                        registeredPatientListAdapter.addList(totalPatients)
                    }
                }

                checkIsAnyPatientAvailable()
                lastPage = totalPatients.size >= choPatientResponse.total_Records
            }
    }

    private fun checkIsAnyPatientAvailable() {
        if (totalPatients.isEmpty()) {
            binding.tvNoPatient.visibility = View.VISIBLE
            binding.rvRegisteredPatient.visibility = View.GONE
        } else {
            binding.tvNoPatient.visibility = View.GONE
            binding.rvRegisteredPatient.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}