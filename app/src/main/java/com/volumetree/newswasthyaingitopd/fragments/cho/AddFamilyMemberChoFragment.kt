package com.volumetree.newswasthyaingitopd.fragments.cho

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.databinding.FragmentAddMemberBinding
import com.volumetree.newswasthyaingitopd.model.requestData.cho.ChoMemberRequest
import com.volumetree.newswasthyaingitopd.model.responseData.cho.ChoPatientData
import com.volumetree.newswasthyaingitopd.model.responseData.patient.RelationData
import com.volumetree.newswasthyaingitopd.utils.*
import com.volumetree.newswasthyaingitopd.viewmodel.ChoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class AddFamilyMemberChoFragment(
    private val patientId: Int,
    private val familyMemberData: ChoPatientData?,
    private val memberUpdate: (Int, ChoPatientData?) -> Unit
) : DialogFragment(),
    View.OnClickListener {

    private var _binding: FragmentAddMemberBinding? = null
    private val binding get() = _binding!!
    private lateinit var myCalendar: Calendar
    private val choViewModel: ChoViewModel by viewModel()
    private var lastSelectRelation: RelationData? = null
    private var genderId = 0

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

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

        binding.layToolbar.tvActionbarTitle.text = getString(R.string.add_family_members)
        binding.layToolbar.imgBack.setOnClickListener(this)
        binding.etRelation.setOnClickListener(this)
        binding.etDateOfBirth.setOnClickListener(this)
        binding.etDateOfBirth.setOnClickListener(this)
        binding.btnMale.setOnClickListener(this)
        binding.btnFeMale.setOnClickListener(this)
        binding.btnTg.setOnClickListener(this)
        binding.btnAddFamilyMember.setOnClickListener(this)
        binding.layToolbar.imgDelete.setOnClickListener(this)
        setMemberData()
    }

    private fun setMemberData() {
        if (familyMemberData != null) {
            binding.layToolbar.tvActionbarTitle.text = getString(R.string.update_family_member)
            binding.layToolbar.imgDelete.visibility = View.VISIBLE
            binding.btnAddFamilyMember.text = getString(R.string.update)
            binding.btnAddFamilyMember.isEnabled = true
            binding.btnAddFamilyMember.isSelected = true
            binding.etFirstName.setText(familyMemberData.firstName)
            binding.etLastName.setText(familyMemberData.lastName)

            myCalendar = Calendar.getInstance()
            myCalendar.time = familyMemberData.dob.fromDOBToDate()
            binding.etDateOfBirth.text = myCalendar.time.formatDate()
            binding.etRelation.text = familyMemberData.relationName
            lastSelectRelation = RelationData(
                name = familyMemberData.relationName,
                value = familyMemberData.relationId
            )
            when (familyMemberData.genderId) {
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
            R.id.imgDelete -> {
                deleteMember()
            }
            R.id.imgBack -> {
                memberUpdate.invoke(1, null)
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

    private fun deleteMember() {
        choViewModel.deleteFamilyMember(
            patientId.toString(),
            familyMemberId = familyMemberData?.patientInfoId.toString()
        ).observeOnce(viewLifecycleOwner) { baseResponse ->
            if (baseResponse.success) {
                requireActivity().showToast(baseResponse.message)
                dismiss()
            }
        }
    }

    private fun addMemberAPICall() {
        val memberRequest = ChoMemberRequest(
            firstName = binding.etFirstName.getTextFromEt(),
            lastName = binding.etLastName.getTextFromEt(),
            relationID = lastSelectRelation!!.value,
            relationName = lastSelectRelation!!.name,
            dob = myCalendar.time.localToServerDate(),
            genderID = genderId,
            patientInfoId = familyMemberData?.patientInfoId ?: patientId
        )
        val apiRequest = if (familyMemberData == null) {
            choViewModel.createFamilyMember(memberRequest)
        } else {
            choViewModel.updateFamilyMember(memberRequest)
        }
        apiRequest.observeOnce(viewLifecycleOwner) { baseResponse ->
            if (baseResponse.success) {
                requireActivity().showToast(baseResponse.message)
                memberUpdate.invoke(
                    if (familyMemberData == null) {
                        1
                    } else {
                        2
                    }, baseResponse.model
                )
            }
        }

    }

    private fun checkValidation(): Boolean {
        val selectedAge = myCalendar.time.localToServerDate().getIntAge()
        val selectedRelationValue: Int = ((lastSelectRelation?.value ?: "0").toInt())

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
            selectedRelationValue == 1 -> {
                if (binding.btnFeMale.isSelected) {
                    requireActivity().showToast(getString(R.string.please_select_gender_according))
                    false
                } else {
                    true
                }
            }
            selectedRelationValue == 2 -> {
                if (binding.btnMale.isSelected) {
                    requireActivity().showToast(getString(R.string.please_select_gender_according))
                    false
                } else {
                    true
                }
            }
            selectedRelationValue == 3 -> {
                if (binding.btnFeMale.isSelected) {
                    requireActivity().showToast(getString(R.string.please_select_gender_according))
                    false
                } else {
                    true
                }
            }
            selectedRelationValue == 4 -> {
                if (binding.btnMale.isSelected) {
                    requireActivity().showToast(getString(R.string.please_select_gender_according))
                    false
                } else {
                    true
                }
            }
            selectedRelationValue == 5 -> {
                if (binding.btnFeMale.isSelected) {
                    requireActivity().showToast(getString(R.string.please_select_gender_according))
                    false
                } else {
                    if (selectedAge < 18) {
                        requireActivity().showToast(getString(R.string.age_should_18))
                        false
                    } else {
                        true
                    }
                }
            }

            selectedRelationValue == 6 -> {
                if (binding.btnMale.isSelected) {
                    requireActivity().showToast(getString(R.string.please_select_gender_according))
                    false
                } else {
                    if (selectedAge < 18) {
                        requireActivity().showToast(getString(R.string.age_should_18))
                        false
                    } else {
                        true
                    }
                }
            }
            selectedRelationValue == 7 -> {
                if (binding.btnFeMale.isSelected) {
                    requireActivity().showToast(getString(R.string.please_select_gender_according))
                    false
                } else {
                    if (selectedAge < 18) {
                        requireActivity().showToast(getString(R.string.age_should_18))
                        false
                    } else {
                        true
                    }
                }
            }
            selectedRelationValue == 8 -> {
                if (binding.btnMale.isSelected) {
                    requireActivity().showToast(getString(R.string.please_select_gender_according))
                    false
                } else {
                    if (selectedAge < 18) {
                        requireActivity().showToast(getString(R.string.age_should_18))
                        false
                    } else {
                        true
                    }
                }
            }
            selectedRelationValue == 9 -> {

                if (selectedAge < 18) {
                    requireActivity().showToast(getString(R.string.age_should_18))
                    false
                } else {
                    true
                }
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