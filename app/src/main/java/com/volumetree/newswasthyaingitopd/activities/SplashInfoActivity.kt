package com.volumetree.newswasthyaingitopd.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.adapters.SplashPagerAdapter
import com.volumetree.newswasthyaingitopd.application.App.Companion.signalR
import com.volumetree.newswasthyaingitopd.databinding.ActivitySplashInfoBinding
import com.volumetree.newswasthyaingitopd.model.responseData.comman.SplashInfo

class SplashInfoActivity : BaseActivity(), View.OnClickListener {
    private lateinit var splashInfoBinding: ActivitySplashInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signalR?.chatHubConnection?.stop()

        splashInfoBinding = ActivitySplashInfoBinding.inflate(layoutInflater)
        setContentView(splashInfoBinding.root)
        setupViewPager()
        splashInfoBinding.tvSkip.setOnClickListener(this)
        splashInfoBinding.imgScroll.setOnClickListener(this)
    }

    private fun setupViewPager() {
        splashInfoBinding.vpSplashInfo.adapter =
            SplashPagerAdapter(supportFragmentManager, getPagerInfo())
        splashInfoBinding.tabLayout.setupWithViewPager(splashInfoBinding.vpSplashInfo)
    }

    private fun getPagerInfo(): ArrayList<SplashInfo> {
        val splashInfoList = ArrayList<SplashInfo>()
        splashInfoList.add(
            SplashInfo(
                title = getString(R.string.talk_to_doctor_therapist_face_to_face),
                desc = getString(R.string.app_enable_to_generate_token_and_look_for_the_consultation),
                img = R.drawable.splash_1
            )
        )
        splashInfoList.add(
            SplashInfo(
                title = getString(R.string.add_your_family_member),
                desc = getString(R.string.get_the_holistic_care_of_your_family),
                img = R.drawable.splash_2
            )
        )
        splashInfoList.add(
            SplashInfo(
                title = getString(R.string.your_prescription_record_organized_are_its_best),
                desc = getString(R.string.get_instant_list_access_all_your_mrs),
                img = R.drawable.splash_3
            )
        )
        return splashInfoList
    }

    override fun onClick(clickView: View) {
        when (clickView.id) {
            R.id.tvSkip -> {
                moveToLoginPage()
            }
            R.id.imgScroll -> {
                val currentItem = splashInfoBinding.vpSplashInfo.currentItem
                if (currentItem == 2) {
                    moveToLoginPage()
                } else {
                    splashInfoBinding.vpSplashInfo.currentItem = currentItem + 1
                }
            }
        }

    }

    private fun moveToLoginPage() {
        startActivity(Intent(this, SelectLoginTypeActivity::class.java))
        finish()
    }

}