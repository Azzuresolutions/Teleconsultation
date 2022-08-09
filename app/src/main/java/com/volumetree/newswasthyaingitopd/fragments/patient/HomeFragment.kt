package com.volumetree.newswasthyaingitopd.fragments.patient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.databinding.FragmentHomeBinding
import com.volumetree.newswasthyaingitopd.utils.PrefUtils
import com.volumetree.newswasthyaingitopd.utils.observeOnce
import com.volumetree.newswasthyaingitopd.utils.setGradientTextColor
import com.volumetree.newswasthyaingitopd.utils.showBottomNavigationView
import com.volumetree.newswasthyaingitopd.viewmodel.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel: ProfileViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.showBottomNavigationView(true)
        profileViewModel.getPatientProfile().observeOnce(viewLifecycleOwner) { profileViewModel ->
            binding.layToolbar.tvLoginUserName.text = profileViewModel.model?.firstName
            PrefUtils.setPatientUserData(requireActivity(), profileViewModel.model)
            PrefUtils.setLoginToken(requireActivity(), profileViewModel.token)
        }

        binding.tvGuidelineHome.setGradientTextColor(
            R.color.text_start,
            R.color.text_end
        )

        binding.btnFindDoctor.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(clickedView: View) {
        when (clickedView.id) {
            R.id.btnFindDoctor -> {
                val extras = FragmentNavigatorExtras(
                    binding.btnFindDoctor to "btnTransition"
                )
                findNavController().navigate(HomeFragmentDirections.actionFindDoctor(), extras)
            }
        }
    }
}