package com.volumetree.newswasthyaingitopd.fragments.cho

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.databinding.FragmentChoCaseHistoryBinding
import com.volumetree.newswasthyaingitopd.utils.showBottomNavigationView

class ChoCasesHistoryFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentChoCaseHistoryBinding? = null
    private val binding get() = _binding!!
    private val args: ChoCasesHistoryFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChoCaseHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.showBottomNavigationView(false)
        binding.tvActionbarTitle.text =
            "${getString(R.string.consultations_of)} ${args.patientData?.firstName} ${args.patientData?.lastName}"
        binding.btnCreateCase.setOnClickListener(this)
        binding.imgBack.setOnClickListener(this)

        showNoCases()
//        binding.rvCases.setVerticalLayoutManager(requireActivity())
//        val documents = ArrayList<String>()
//        documents.add("")
//        documents.add("")
//        documents.add("")
//        documents.add("")
//        documents.add("")
//        binding.rvCases.adapter = CasesListAdapter(documents)
    }

    private fun showNoCases() {
        binding.layNoCases.visibility = View.VISIBLE
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
            R.id.btnCreateCase -> {
                findNavController().navigate(ChoCasesHistoryFragmentDirections.actionCreateCase(args.patientData,0))
            }
        }
    }

}