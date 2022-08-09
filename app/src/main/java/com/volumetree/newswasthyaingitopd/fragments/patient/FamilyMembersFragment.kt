package com.volumetree.newswasthyaingitopd.fragments.patient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.adapters.MemberListAdapter
import com.volumetree.newswasthyaingitopd.databinding.FragmentFamilyMembersBinding
import com.volumetree.newswasthyaingitopd.model.responseData.patient.FamilyMemberData
import com.volumetree.newswasthyaingitopd.utils.observeOnce
import com.volumetree.newswasthyaingitopd.utils.setVerticalLayoutManager
import com.volumetree.newswasthyaingitopd.utils.showBottomNavigationView
import com.volumetree.newswasthyaingitopd.utils.showToast
import com.volumetree.newswasthyaingitopd.viewmodel.PatientViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FamilyMembersFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentFamilyMembersBinding? = null
    private val binding get() = _binding!!
    private val patientViewModel: PatientViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFamilyMembersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.showBottomNavigationView(false)
        binding.layToolbar.tvActionbarTitle.text = getString(R.string.add_family_members)
        binding.layToolbar.imgBack.setOnClickListener(this)
        binding.btnAddNewMember.setOnClickListener(this)
        setupMemberAdapter()
    }

    private fun setupMemberAdapter() {

        binding.rvFamilymembers.setVerticalLayoutManager(requireActivity())

        patientViewModel.getFamilyMember().observeOnce(viewLifecycleOwner) { familyMemberResponse ->
            if (familyMemberResponse.success && !familyMemberResponse.lstModel.isNullOrEmpty()) {
                binding.rvFamilymembers.adapter =
                    MemberListAdapter(
                        familyMemberResponse.lstModel,
                        ::onMemberEdit,
                        ::onMemberDelete
                    )
            }
        }
    }

    private fun onMemberDelete(memberId: Int) {
        patientViewModel.deleteFamilyMember(memberId)
            .observeOnce(viewLifecycleOwner) { baseResponse ->
                if (baseResponse.success) {
                    requireActivity().showToast(baseResponse.message)
                    (binding.rvFamilymembers.adapter as MemberListAdapter).deleteFromList(memberId)
                }
            }
    }

    private fun onMemberEdit(memberData: FamilyMemberData) {
        findNavController().navigate(FamilyMembersFragmentDirections.actionAddMemberFromList(
                memberData
            )
        )
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
            R.id.btnAddNewMember -> {
                findNavController().navigate(FamilyMembersFragmentDirections.actionAddMemberFromList(
                        null
                    )
                )
            }
        }
    }


}