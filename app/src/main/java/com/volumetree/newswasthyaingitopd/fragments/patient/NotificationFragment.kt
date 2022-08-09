package com.volumetree.newswasthyaingitopd.fragments.patient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.databinding.FragmentNotificationBinding
import com.volumetree.newswasthyaingitopd.utils.showBottomNavigationView

class NotificationFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.showBottomNavigationView(false)
        binding.layToolbar.imgBack.setOnClickListener(this)
        binding.layToolbar.tvActionbarTitle.text = getString(R.string.notification)
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
        }
    }


}