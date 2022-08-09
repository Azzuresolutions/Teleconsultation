package com.volumetree.newswasthyaingitopd.fragments.patient

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.databinding.FragmentAddMemberBinding
import com.volumetree.newswasthyaingitopd.model.requestData.patient.MemberRequest
import com.volumetree.newswasthyaingitopd.model.responseData.patient.FamilyMemberData
import com.volumetree.newswasthyaingitopd.model.responseData.patient.RelationData
import com.volumetree.newswasthyaingitopd.utils.*
import com.volumetree.newswasthyaingitopd.viewmodel.PatientViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class AddFamilyMemberFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentAddMemberBinding? = null
    private val binding get() = _binding!!
    private lateinit var myCalendar: Calendar
    private val args: AddFamilyMemberFragmentArgs by navArgs()
    private val patientViewModel: PatientViewModel by viewModel()
    private var lastSelectRelation: RelationData? = null
    var memberData: FamilyMemberData? = null
    private var genderId = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddMemberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.showBottomNavigationView(false)
        memberData = args.familyMemberData

        binding.layToolbar.tvActionbarTitle.text = getString(R.string.add_family_members)
        binding.layToolbar.imgBack.setOnClickListener(this)
        binding.etRelation.setOnClickListener(this)
        binding.etDateOfBirth.setOnClickListener(this)
        binding.etDateOfBirth.setOnClickListener(this)
        binding.btnMale.setOnClickListener(this)
        binding.btnFeMale.setOnClickListener(this)
        binding.btnTg.setOnClickListener(this)
        binding.btnAddFamilyMember.setOnClickListener(this)
        if (memberData != null) {
            setData()
        }
    }

    private fun setData() {
        if (memberData != null) {
            binding.etFirstName.setText(memberData!!.firstName)
            binding.etLastName.setText(memberData!!.lastName)
            binding.etDateOfBirth.text = memberData!!.dob
            binding.etRelation.text = memberData!!.relationName
            lastSelectRelation = RelationData(
                name = memberData!!.relationName,
                value = memberData!!.relationId.toString()
            )
            when (memberData!!.genderId) {
                1 -> {
                    binding.btnMale.performClick()
                }
                2 -> {
                    binding.btnFeMale.performClick()
                }
                3 -> {
                    binding.btnTg.performClick()
                }
            }
        }
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
            R.id.etRelation -> {
                requireActivity().showRelationDialog(false, ::relationClick)
            }
            R.id.btnAddFamilyMember -> {
                if (checkValidation()) {
                    addMemberAPICall()
                }
            }
            R.id.etDateOfBirth -> {
                openDatePicker()
            }
            R.id.btnMale -> {
                genderId = 1
                binding.btnMale.selectGender(
                    requireActivity(),
                    binding.btnFeMale,
                    binding.btnTg
                )
            }
            R.id.btnFeMale -> {
                genderId = 2
                binding.btnFeMale.selectGender(
                    requireActivity(),
                    binding.btnMale,
                    binding.btnTg
                )
            }
            R.id.btnTg -> {
                genderId = 3
                binding.btnTg.selectGender(
                    requireActivity(),
                    binding.btnFeMale,
                    binding.btnMale
                )
            }
        }
    }

    private fun addMemberAPICall() {
        val memberRequest = MemberRequest(
            firstName = binding.etFirstName.getTextFromEt(),
            lastName = binding.etLastName.getTextFromEt(),
            relationID = lastSelectRelation!!.value,
            relationName = lastSelectRelation!!.name,
            dob = myCalendar.time.localToServerDate(),
            genderID = genderId
        )
        patientViewModel.createFamilyMember(memberRequest)
            .observeOnce(viewLifecycleOwner) { baseResponse ->
                if (baseResponse.success) {
                    requireActivity().showToast(baseResponse.message)
                    findNavController().navigate(AddFamilyMemberFragmentDirections.actionFamilyMembers())
                }
            }

    }

    private fun checkValidation(): Boolean {
        val isValid: Boolean = when {
            binding.etFirstName.text.toString().isEmpty() -> {
                requireActivity().showToast(getString(R.string.please_enter_firstname))
                false
            }
            binding.etLastName.text.toString().isEmpty() -> {
                requireActivity().showToast(getString(R.string.please_enter_lastname))
                false
            }
            binding.etRelation.text.toString().isEmpty() -> {
                requireActivity().showToast(getString(R.string.please_select_relation))
                false
            }
            binding.etDateOfBirth.text.toString().isEmpty() -> {
                requireActivity().showToast(getString(R.string.please_enter_dob))
                false
            }
            lastSelectRelation == null -> {
                requireActivity().showToast(getString(R.string.please_select_relation))
                false
            }
            else -> {
                true
            }
        }
        return isValid
    }


    private fun relationClick(relationData: RelationData, relationDialog: Dialog) {
        lastSelectRelation = relationData
        binding.etRelation.text = lastSelectRelation!!.name
        relationDialog.dismiss()
    }

    private fun openDatePicker() {
        requireActivity().showDatePicker { mCalendar ->
            myCalendar = mCalendar
            binding.etDateOfBirth.text = myCalendar.time.formatDate()
            binding.btnAddFamilyMember.isEnabled = true
            binding.btnAddFamilyMember.isSelected = true
        }

    }
}