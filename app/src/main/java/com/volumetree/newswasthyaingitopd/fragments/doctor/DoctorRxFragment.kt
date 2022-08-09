package com.volumetree.newswasthyaingitopd.fragments.doctor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.adapters.DoctorRXViewPagerAdapter
import com.volumetree.newswasthyaingitopd.application.App
import com.volumetree.newswasthyaingitopd.databinding.FragmentRxDoctorBinding
import com.volumetree.newswasthyaingitopd.fragments.patient.PreviewCase
import com.volumetree.newswasthyaingitopd.model.responseData.cho.DraftConsultationResponse
import com.volumetree.newswasthyaingitopd.model.responseData.comman.AllergyData
import com.volumetree.newswasthyaingitopd.model.responseData.patient.SelectedDocData
import com.volumetree.newswasthyaingitopd.utils.observeOnce
import com.volumetree.newswasthyaingitopd.utils.showBottomNavigationView
import com.volumetree.newswasthyaingitopd.viewmodel.ChoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DoctorRxFragment :
    Fragment(), View.OnClickListener {
    companion object {
        var toUserId: String = ""
        var remoteUserName: String = ""
    }

    private var _binding: FragmentRxDoctorBinding? = null
    private val binding get() = _binding!!
    private val choViewModel: ChoViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRxDoctorBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var caseData: DraftConsultationResponse
    private var selectedProblems: ArrayList<AllergyData> = ArrayList()
    private val selectedDocuments = ArrayList<SelectedDocData>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imgBack.setOnClickListener(this)
        binding.tvActionbarTitle.text = remoteUserName
        activity?.showBottomNavigationView(false)
        choViewModel.getConsultation(App.consultationId)
            .observeOnce(viewLifecycleOwner) { consultationData ->
                this.caseData = consultationData
                if (this.caseData.model != null) {

                    val lstConsultationProblemsModel = caseData.model?.lstConsultationProblemsModel
                    if (lstConsultationProblemsModel != null && lstConsultationProblemsModel.isNotEmpty()) {
                        lstConsultationProblemsModel.forEach { problemData ->
                            selectedProblems.add(
                                AllergyData(
                                    term = problemData.name,
                                    id = problemData.consultationProblemId
                                )
                            )
                        }
                    }

                    caseData.model.lstConsultationImagesModel.forEach {
                        selectedDocuments.add(
                            SelectedDocData(
                                bitmap = null,
                                imgName = it.fileName,
                                base64 = it.filePath,
                                "",
                                fileFlag = it.fileFlag
                            )
                        )
                    }


                    setupViewPager()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(clickedView: View) {
        when (clickedView.id) {
            R.id.imgBack -> {
                requireActivity().onBackPressed()
            }
        }
    }

    private fun setupViewPager() {
        val titles = ArrayList<String>()

        titles.add(getString(R.string.detail_filled_cho))
        titles.add(getString(R.string.rx_patient))



        binding.viewPager.adapter =
            DoctorRXViewPagerAdapter(
                requireActivity(),
                titles,
                PreviewCase(
                    caseData.model?.lstConsultationAllergyModel!!,
                    caseData.model?.lstConsultationTestResultsModel!!,
                    selectedProblems,
                    selectedDocuments,
                    caseData.model?.consultationModel?.queryDesc.toString(),
                    caseData.model?.consultationModel?.additionalProblem.toString(),
                    caseData.model?.consultationModel?.additionalAllergy.toString(),
                    caseData.model?.consultationModel?.gerneralExamination.toString(),
                    caseData.model?.consultationModel?.isSmoker ?: false,
                    caseData.model?.consultationModel?.isAlcoholic ?: false,
                    caseData.model?.consultationModel?.isHypertension ?: false,
                    caseData.model?.consultationModel?.isDiabetic ?: false,
                    null,
                    caseData.model?.consultationModel,
                    caseData.model?.patientConsultationModel
                ),
                DoctorCallPrescription(caseData, toUserId)
            )
        binding.viewPager.isUserInputEnabled = false
        TabLayoutMediator(
            binding.chatTabLayout, binding.viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = titles[position]
        }.attach()

    }

}