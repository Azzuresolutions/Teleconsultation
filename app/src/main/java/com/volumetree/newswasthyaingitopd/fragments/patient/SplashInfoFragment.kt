package com.volumetree.newswasthyaingitopd.fragments.patient


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.volumetree.newswasthyaingitopd.databinding.FragmentSplashInfoBinding
import com.volumetree.newswasthyaingitopd.model.responseData.comman.SplashInfo

class SplashInfoFragment(val splashInfo: SplashInfo) : Fragment() {

    lateinit var fragmentSplashInfoBinding: FragmentSplashInfoBinding

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState:
        Bundle?
    ): View {
        fragmentSplashInfoBinding = FragmentSplashInfoBinding.inflate(inflater, container, false)
        return fragmentSplashInfoBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragmentSplashInfoBinding.splashTitle.text = splashInfo.title
        fragmentSplashInfoBinding.splashDesc.text = splashInfo.desc
        fragmentSplashInfoBinding.imgSplash.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                splashInfo.img
            ))
    }

}
