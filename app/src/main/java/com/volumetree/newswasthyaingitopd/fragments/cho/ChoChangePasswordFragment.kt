package com.volumetree.newswasthyaingitopd.fragments.cho

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.databinding.FragmentChangePasswordBinding
import com.volumetree.newswasthyaingitopd.model.requestData.cho.ChangePasswordRequest
import com.volumetree.newswasthyaingitopd.utils.*
import com.volumetree.newswasthyaingitopd.viewmodel.ChoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChoChangePasswordFragment : Fragment(), View.OnClickListener, TextWatcher {

    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!
    private val choViewModel: ChoViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.layToolbar.tvActionbarTitle.text = getString(R.string.change_password)
        binding.layToolbar.imgBack.setOnClickListener(this)
        binding.btnChangePassword.setOnClickListener(this)
        binding.imgPasswordVisibility.setOnClickListener(this)
        binding.imgPasswordVisibilityConfirmPass.setOnClickListener(this)
        binding.imgPasswordVisibilityNewPass.setOnClickListener(this)
        activity?.showBottomNavigationView(false)

        binding.etCurrentPassword.addTextChangedListener(this)
        binding.etNewPass.addTextChangedListener(this)
        binding.etConfirmPass.addTextChangedListener(this)
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
            R.id.btnChangePassword -> {
                if (checkValidation(true)) {
                    changePassword()
                }
            }
            R.id.imgPasswordVisibility -> {
                binding.etCurrentPassword.showHidePassword(
                    requireActivity(),
                    binding.imgPasswordVisibility
                )
            }
            R.id.imgPasswordVisibilityConfirmPass -> {
                binding.etConfirmPass.showHidePassword(
                    requireActivity(),
                    binding.imgPasswordVisibilityConfirmPass
                )
            }
            R.id.imgPasswordVisibilityNewPass -> {
                binding.etNewPass.showHidePassword(
                    requireActivity(),
                    binding.imgPasswordVisibilityNewPass
                )
            }

        }
    }

    private fun changePassword() {

        val changePasswordRequest = ChangePasswordRequest(
            memberId = PrefUtils.getChoData(requireActivity())?.memberId ?: 0,
            password = binding.etNewPass.getSHA512Text(),
            oldPassword = binding.etCurrentPassword.getSHA512Text()
        )
        choViewModel.changePassword(changePasswordRequest)
            .observeOnce(viewLifecycleOwner) { changePasswordResponse ->
                requireActivity().showToast(changePasswordResponse.message)
                if (changePasswordResponse.success) {
                    findNavController().popBackStack()
                }
            }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        checkValidation()
    }

    override fun afterTextChanged(p0: Editable?) {
    }


    private fun checkValidation(showToast: Boolean = false): Boolean {
        var isValid: Boolean = when {
            binding.etCurrentPassword.getTextFromEt().isEmpty() -> {
                if (showToast) {
                    requireActivity().showToast(getString(R.string.please_enter_old_password))
                }
                false
            }
            binding.etNewPass.getTextFromEt().isEmpty() -> {
                if (showToast) {
                    requireActivity().showToast(getString(R.string.please_enter_new_password))
                }
                false
            }
            binding.etConfirmPass.getTextFromEt().isEmpty() -> {
                if (showToast) {
                    requireActivity().showToast(getString(R.string.please_enter_confirm_password))
                }
                false
            }


            else -> {
                true
            }
        }

        binding.btnChangePassword.enableDisable(isValid)
        if (binding.etNewPass.getTextFromEt().length < 6) {
            if (showToast) {
                requireActivity().showToast(getString(R.string.password_length))
            }
            isValid = false
        } else if (binding.etConfirmPass.getTextFromEt().length < 6) {
            if (showToast) {
                requireActivity().showToast(getString(R.string.password_length))
            }
            isValid = false
        } else if (binding.etNewPass.getTextFromEt() != binding.etConfirmPass.getTextFromEt()) {
            if (showToast) {
                requireActivity().showToast(getString(R.string.confirm_password_not_match))
            }
            isValid = false
        }
        return isValid
    }
}