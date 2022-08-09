package com.volumetree.newswasthyaingitopd.fragments.patient

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.adapters.SearchSpecializedDoctorAdapter
import com.volumetree.newswasthyaingitopd.databinding.FragmentSpecializedDoctorBinding
import com.volumetree.newswasthyaingitopd.interfaces.SelectDoctorAndSpecializationListener
import com.volumetree.newswasthyaingitopd.utils.getTextFromEt
import com.volumetree.newswasthyaingitopd.utils.setVerticalLayoutManager
import com.volumetree.newswasthyaingitopd.utils.showBottomNavigationView
import com.volumetree.newswasthyaingitopd.viewmodel.MasterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SpecializedDoctorFragment(
    private val specialityId: Int,
    private val specialityName: String,
    private val listener: SelectDoctorAndSpecializationListener
) :
    DialogFragment(), View.OnClickListener, TextWatcher {

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    private var _binding: FragmentSpecializedDoctorBinding? = null
    private val binding get() = _binding!!
    private val masterViewModel: MasterViewModel by viewModel()
    private lateinit var searchSpecializedDoctorAdapter: SearchSpecializedDoctorAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpecializedDoctorBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.showBottomNavigationView(false)
        binding.tvSelectedSpecialityValue.text = specialityName
        binding.etSearchDoctorName.addTextChangedListener(this)
        binding.imgBack.setOnClickListener(this)
        binding.etSearchDoctorName.setOnEditorActionListener { textView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getDoctors()
                true
            }
            false
        }
        setupAdapter()
        getDoctors()
    }

    private fun setupAdapter() {
        binding.rvSpecializedDr.setVerticalLayoutManager(requireActivity())
        val itemDecoration =
            DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(resources.getDrawable(R.drawable.recycler_divider))
        binding.rvSpecializedDr.addItemDecoration(
            itemDecoration
        )
        searchSpecializedDoctorAdapter = SearchSpecializedDoctorAdapter()
        binding.rvSpecializedDr.adapter = searchSpecializedDoctorAdapter
        searchSpecializedDoctorAdapter.onItemClick = { selectedSpeciality ->
            listener.onSpecialDoctor(selectedSpeciality)
        }
    }

    private fun getDoctors() {
        masterViewModel.getDoctorBySpecialization(
            specialityId,
            binding.etSearchDoctorName.getTextFromEt()
        ).observe(viewLifecycleOwner) { specializedDrResponse ->
            searchSpecializedDoctorAdapter.addData(specializedDrResponse.lstModel)

            if (specializedDrResponse.lstModel.isEmpty()) {
                binding.tvNoDoctor.visibility = View.VISIBLE
            } else {
                binding.tvNoDoctor.visibility = View.GONE
            }
        }
    }

    override fun onClick(clickedView: View) {
        when (clickedView.id) {
            R.id.imgBack -> {
                listener.onSpecialDoctor(null)
            }
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(searchText: CharSequence?, p1: Int, p2: Int, p3: Int) {
        getDoctors()
    }

    override fun afterTextChanged(p0: Editable?) {
    }


}