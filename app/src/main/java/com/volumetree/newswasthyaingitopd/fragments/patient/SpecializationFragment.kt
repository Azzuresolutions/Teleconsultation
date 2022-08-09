package com.volumetree.newswasthyaingitopd.fragments.patient

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.adapters.SearchSpecialityAdapter
import com.volumetree.newswasthyaingitopd.databinding.FragmentSpecializationBinding
import com.volumetree.newswasthyaingitopd.interfaces.CreateCaseListener
import com.volumetree.newswasthyaingitopd.interfaces.SelectDoctorAndSpecializationListener
import com.volumetree.newswasthyaingitopd.utils.Constants
import com.volumetree.newswasthyaingitopd.utils.observeOnce
import com.volumetree.newswasthyaingitopd.utils.setVerticalLayoutManager
import com.volumetree.newswasthyaingitopd.utils.showBottomNavigationView
import com.volumetree.newswasthyaingitopd.viewmodel.MasterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SpecializationFragment(private val listener: SelectDoctorAndSpecializationListener) :
    DialogFragment(),
    View.OnClickListener, TextWatcher {

    private var _binding: FragmentSpecializationBinding? = null
    private val binding get() = _binding!!
    private val masterViewModel: MasterViewModel by viewModel()
    private lateinit var searchSpecialityAdapter: SearchSpecialityAdapter


    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (_binding == null) {
            _binding = FragmentSpecializationBinding.inflate(inflater, container, false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.showBottomNavigationView(false)
        binding.etSearchSpeciality.addTextChangedListener(this)
        binding.imgBack.setOnClickListener(this)
        getSpecialization()
    }

    private fun getSpecialization() {
        masterViewModel.getSpecialityMaster()
            .observeOnce(viewLifecycleOwner) { specialityResponse ->
                binding.rvSpecializations.setVerticalLayoutManager(requireActivity())
                searchSpecialityAdapter = SearchSpecialityAdapter()
                binding.rvSpecializations.adapter = searchSpecialityAdapter
                searchSpecialityAdapter.addData(specialityResponse.lstModel)
                searchSpecialityAdapter.onItemClick = { selectedSpeciality ->
                    listener.onSpecialization(selectedSpeciality)
                }
            }

    }

    override fun onClick(clickedView: View) {
        when (clickedView.id) {
            R.id.imgBack -> {
                listener.onSpecialization(null)
            }
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(searchText: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (this::searchSpecialityAdapter.isInitialized) {
            searchSpecialityAdapter.filter.filter(searchText)
        }
    }

    override fun afterTextChanged(p0: Editable?) {

    }


}