package com.volumetree.newswasthyaingitopd.fragments.patient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.adapters.UploadedDocumentListAdapter
import com.volumetree.newswasthyaingitopd.databinding.FragmentReportsBinding
import com.volumetree.newswasthyaingitopd.utils.setVerticalLayoutManager

class ReportsFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentReportsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportsBinding.inflate(inflater, container, false)
        binding.layToolbar.tvActionbarTitle.text = getString(R.string.mediacal_records)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvDocuments.setVerticalLayoutManager(requireActivity())
//        val documents = ArrayList<String>()
//        documents.add("")
//        documents.add("")
//        documents.add("")
//        binding.rvDocuments.adapter = UploadedDocumentListAdapter(documents)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(clickedView: View) {

    }
}