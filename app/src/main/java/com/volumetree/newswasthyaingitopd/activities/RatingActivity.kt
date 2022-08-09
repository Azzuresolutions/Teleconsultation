package com.volumetree.newswasthyaingitopd.activities

import android.content.Intent
import android.os.Bundle
import android.text.Html
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.activities.cho.ChoDashBoardActivity
import com.volumetree.newswasthyaingitopd.databinding.ActivityRatingsBinding
import com.volumetree.newswasthyaingitopd.enums.UserTypes
import com.volumetree.newswasthyaingitopd.model.requestData.cho.RatingRequest
import com.volumetree.newswasthyaingitopd.utils.PrefUtils
import com.volumetree.newswasthyaingitopd.utils.loadImageURL
import com.volumetree.newswasthyaingitopd.utils.observeOnce
import com.volumetree.newswasthyaingitopd.utils.showToast
import com.volumetree.newswasthyaingitopd.viewmodel.MasterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RatingActivity :
    BaseActivity() {

    private var _binding: ActivityRatingsBinding? = null
    private val binding get() = _binding!!
    private val masterViewModel: MasterViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRatingsBinding.inflate(layoutInflater)
        setContentView(_binding!!.root)

        binding.tvSkip.setOnClickListener {
            backToDashboard()
        }
        binding.imgDr.loadImageURL(this, "", doctor = true)
        binding.tvDrName.text = intent.getStringExtra("remoteUserName")
        binding.tvRating.text = Html.fromHtml(
            "${getString(R.string.how_was_your_experience_with_dr_banerjee)} <font color='#2CB7DF'>${
                intent.getStringExtra(
                    "remoteUserName"
                )
            }</font>?"
        )
        binding.ratingBar.setOnRatingBarChangeListener { ratingBar, fl, b ->
            val loginUserType = PrefUtils.getLoginUserType(this)
            val patientId = if (loginUserType == UserTypes.CHO.type) {
                PrefUtils.getChoData(this)?.memberId
            } else {
                PrefUtils.getPatientUserData(this)?.patientInfoId
            }.toString()
            masterViewModel.patientRating(
                RatingRequest(
                    patientInfoId = patientId,
                    memberId = intent.getStringExtra("toUserId").toString(),
                    consultationId = intent.getStringExtra("consultationId").toString(),
                    rating = binding.ratingBar.rating.toString(),
                    feedback = ""
                )
            ).observeOnce(this) {
                showToast(it.message)
                backToDashboard()
            }
        }
    }

    private fun backToDashboard() {
        startActivity(
            Intent(this, ChoDashBoardActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onBackPressed() {
        backToDashboard()
    }
}