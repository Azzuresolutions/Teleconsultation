package com.volumetree.newswasthyaingitopd.fragments.common

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.activities.ChatCallActivity
import com.volumetree.newswasthyaingitopd.adapters.ChatCallViewPagerAdapter
import com.volumetree.newswasthyaingitopd.application.App
import com.volumetree.newswasthyaingitopd.databinding.FragmentChatCallBinding
import com.volumetree.newswasthyaingitopd.fragments.patient.PreviewCase
import com.volumetree.newswasthyaingitopd.model.responseData.cho.DraftConsultationResponse
import com.volumetree.newswasthyaingitopd.model.responseData.comman.AllergyData
import com.volumetree.newswasthyaingitopd.model.responseData.patient.SelectedDocData
import com.volumetree.newswasthyaingitopd.utils.observeOnce
import com.volumetree.newswasthyaingitopd.utils.showBottomNavigationView
import com.volumetree.newswasthyaingitopd.viewmodel.ChoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChatCallFragment(
    val consultationId: Int,
    val doctorId: String,
    private val doctorName: String,
    private val toType: Int,
    private val notEditable: Boolean
) :
    Fragment(), View.OnClickListener {

    private var _binding: FragmentChatCallBinding? = null
    private val binding get() = _binding!!
    private var chatFragment: ChatFragment? = null
    private var prescriptionFragment: ChatPrescriptionFragment? = null
    private var previewCase: PreviewCase? = null
    private var caseRecordFragment: ChatCaseRecordFragment? = null
    private val choViewModel: ChoViewModel by viewModel()

    private lateinit var caseData: DraftConsultationResponse
    private var selectedProblems: ArrayList<AllergyData> = ArrayList()
    private val selectedDocuments = ArrayList<SelectedDocData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatCallBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imgBack.setOnClickListener(this)
//        binding.imgVideoCall.setOnClickListener(this)
        activity?.showBottomNavigationView(false)
        binding.tvActionbarTitle.text = doctorName

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

                    caseData.model!!.lstConsultationImagesModel.forEach {
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

        if (notEditable) {
            binding.imgVideoCall.visibility = View.GONE
            binding.imgAudioCall.visibility = View.GONE
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
//            R.id.imgVideoCall -> {
//                if (requireActivity() is ChatCallActivity) {
//                    (requireActivity() as ChatCallActivity).onEndCall()
//                }
//            }
        }
    }

    private val titles = ArrayList<String>()

    private fun setupViewPager() {
        titles.add(getString(R.string.chats))
        titles.add(getString(R.string.prescription))
        titles.add(getString(R.string.case_record))

        chatFragment = ChatFragment(doctorId, toType, consultationId, !notEditable)
        if (caseData.model!!.lstConsultationMessageModel.size >= 2) {
            ChatCallActivity.lastConsultationId = consultationId
        }
        prescriptionFragment =
            ChatPrescriptionFragment()
        if (!notEditable) {
            caseRecordFragment = ChatCaseRecordFragment(consultationId, doctorId, !notEditable)
        } else {
            previewCase = PreviewCase(
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
                caseData.model?.patientConsultationModel, false
            )
        }

        binding.viewPager.adapter =
            ChatCallViewPagerAdapter(
                requireActivity(),
                titles,
                chatFragment!!,
                prescriptionFragment!!,
                caseRecordFragment,
                previewCase
            )
        binding.viewPager.isUserInputEnabled = false
        TabLayoutMediator(
            binding.chatTabLayout, binding.viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = titles[position]
        }.attach()

    }

    fun updateCurrentFragment(defaultFragment: Int) {
        Handler(Looper.getMainLooper()).postDelayed({
            binding.viewPager.currentItem = defaultFragment
        }, 200)
    }

    fun updateChatList() {
        chatFragment?.updateChatList()
    }
}