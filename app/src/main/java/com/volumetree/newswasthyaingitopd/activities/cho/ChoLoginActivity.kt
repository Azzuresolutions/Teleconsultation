package com.volumetree.newswasthyaingitopd.activities.cho

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.activities.BaseActivity
import com.volumetree.newswasthyaingitopd.databinding.ActivityChoLoginBinding
import com.volumetree.newswasthyaingitopd.model.requestData.cho.SingInRequest
import com.volumetree.newswasthyaingitopd.utils.*
import com.volumetree.newswasthyaingitopd.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChoLoginActivity : BaseActivity(), TextWatcher {
    private lateinit var binding: ActivityChoLoginBinding
    private val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChoLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etUserName.addTextChangedListener(this)
        binding.etPassword.addTextChangedListener(this)

        binding.backLay.imgBack.setOnClickListener {
            onBackPressed()
        }

        binding.imgPasswordVisibility.setOnClickListener {
            binding.etPassword.showHidePassword(
                this,
                binding.imgPasswordVisibility
            )
        }

        binding.btnLogin.setOnClickListener {
            if (checkValueValidation()) {
                viewModel.signInCHO(
                    SingInRequest(
                        binding.etUserName.getTextFromEt(),
                        binding.etPassword.getTextFromEt()
                    )
                ).observeOnce(this) { choProfileResponse ->
                    if (choProfileResponse.success) {
                        PrefUtils.setLogin(this@ChoLoginActivity, 3)
                        PrefUtils.setLoginToken(this@ChoLoginActivity, choProfileResponse.token)
                        startActivity(
                            Intent(this, ChoDashBoardActivity::class.java)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        )
                        finish()
                    }
                }

            }
        }

    }

    private fun checkValueValidation(): Boolean {
        return if (binding.etUserName.getTextFromEt().isEmpty()) {
            showToast(getString(R.string.please_enter_username))
            false
        } else if (binding.etPassword.getTextFromEt().isEmpty()) {
            showToast(getString(R.string.please_enter_password))
            false
        } else {
            true
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        checkValidation()
    }

    override fun afterTextChanged(p0: Editable?) {

    }


    private fun checkValidation() {
        val userName = binding.etUserName.text.toString()
        val password = binding.etPassword.text.toString()
        var isValid = false
        if (userName.isNotEmpty() && password.isNotEmpty()) {
            isValid = true
        }
        binding.btnLogin.isSelected = isValid
    }
}