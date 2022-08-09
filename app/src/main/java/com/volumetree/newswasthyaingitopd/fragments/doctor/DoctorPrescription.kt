package com.volumetree.newswasthyaingitopd.fragments.doctor

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.adapters.CommonStringListAdapter
import com.volumetree.newswasthyaingitopd.databinding.FragmentAddPrescriptionBinding
import com.volumetree.newswasthyaingitopd.utils.fullWidthDialog
import com.volumetree.newswasthyaingitopd.utils.manageAllergyButton
import com.volumetree.newswasthyaingitopd.utils.setVerticalLayoutManager
import com.volumetree.newswasthyaingitopd.utils.showBottomNavigationView

class DoctorPrescription : Fragment(), View.OnClickListener {

    private var _binding: FragmentAddPrescriptionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPrescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.showBottomNavigationView(false)
        binding.layToolbar.tvActionbarTitle.text = getString(R.string.add_prescription)
        binding.layToolbar.imgBack.setOnClickListener(this)
        binding.layAllergyTitle.setOnClickListener(this)
        binding.layGeneralExaminationTitle.setOnClickListener(this)
        binding.layPrescriptionTitle.setOnClickListener(this)
        binding.etDuration.setOnClickListener(this)
        binding.etSeverity.setOnClickListener(this)
        binding.etFrequency.setOnClickListener(this)

        binding.btnDiabetes.setOnClickListener(this)
        binding.btnAlcoholIntake.setOnClickListener(this)
        binding.btnHypertension.setOnClickListener(this)
        binding.btnSmoking.setOnClickListener(this)
        binding.btnAddToList.setOnClickListener(this)
        binding.btnViewAddedAllergy.setOnClickListener(this)
        binding.btnAddPrescriptionToList.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(clickedView: View) {
        when (clickedView.id) {
            R.id.layAllergyTitle -> {
                if (binding.layAllergyDetails.visibility == View.VISIBLE) {
                    binding.layAllergyDetails.visibility = View.GONE
                } else {
                    binding.layAllergyDetails.visibility = View.VISIBLE
                }
            }
            R.id.layGeneralExaminationTitle -> {
                if (binding.layGeneralExaminationDetails.visibility == View.VISIBLE) {
                    binding.layGeneralExaminationDetails.visibility = View.GONE
                } else {
                    binding.layGeneralExaminationDetails.visibility = View.VISIBLE
                }
            }
            R.id.layPrescriptionTitle -> {
                if (binding.layPrescriptionDetails.visibility == View.VISIBLE) {
                    binding.layPrescriptionDetails.visibility = View.GONE
                } else {
                    binding.layPrescriptionDetails.visibility = View.VISIBLE
                }
            }
            R.id.etDuration -> {
                showDurationDialog()
            }
            R.id.etSeverity -> {
                showSeverityDialog()
            }
            R.id.etFrequency -> {
                showFrequencyDialog()
            }
            R.id.btnDiabetes -> {
                binding.btnDiabetes.manageAllergyButton(
                    requireActivity(),
                    binding.btnAlcoholIntake,
                    binding.btnHypertension,
                    binding.btnSmoking,
                )
            }
            R.id.btnSmoking -> {
                binding.btnSmoking.manageAllergyButton(
                    requireActivity(),
                    binding.btnAlcoholIntake,
                    binding.btnHypertension,
                    binding.btnDiabetes,
                )
            }
            R.id.btnAlcoholIntake -> {
                binding.btnAlcoholIntake.manageAllergyButton(
                    requireActivity(),
                    binding.btnSmoking,
                    binding.btnHypertension,
                    binding.btnDiabetes,
                )
            }
            R.id.btnHypertension -> {
                binding.btnHypertension.manageAllergyButton(
                    requireActivity(),
                    binding.btnAlcoholIntake,
                    binding.btnSmoking,
                    binding.btnDiabetes,
                )
            }
            R.id.imgBack -> {
                findNavController().popBackStack()
            }
            R.id.btnAddToList -> {
                binding.btnViewAddedAllergy.visibility = View.VISIBLE
            }
            R.id.btnViewAddedAllergy -> {
                findNavController().navigate(DoctorPrescriptionDirections.actionAddedAllergy())
            }
            R.id.btnAddPrescriptionToList -> {
                showPrescriptionList()
            }
        }
    }

    private fun showPrescriptionList() {

//        binding.prescriptionTitle.visibility = View.VISIBLE
//        binding.rvPrescription.visibility = View.VISIBLE
//        binding.rvPrescription.setVerticalLayoutManager(requireActivity())
//        val hospitalName = ArrayList<String>()
//        hospitalName.add("QID")
//        hospitalName.add("SOS")
//        hospitalName.add("RDS")
//        binding.rvPrescription.adapter = PrescriptionListAdapter(hospitalName)
    }

    private lateinit var frequencyDialog: Dialog
    private fun showFrequencyDialog() {
        frequencyDialog = Dialog(requireActivity())
        frequencyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        frequencyDialog.window?.setBackgroundDrawableResource(R.color.transparent)
        frequencyDialog.setContentView(R.layout.layout_list_dialog)
        val rvDialogList =
            frequencyDialog.findViewById<RecyclerView>(R.id.rvDialogList)

        frequencyDialog.setCancelable(true)
        frequencyDialog.setCanceledOnTouchOutside(true)
        frequencyDialog.show()
        severityDialog.fullWidthDialog()
        rvDialogList.setVerticalLayoutManager(requireActivity())
        val list = ArrayList<String>()
        list.add("BD")
        list.add("HS")
        list.add("OS")
        list.add("QID")
        list.add("SOS")
        list.add("RDS")
        rvDialogList.adapter = CommonStringListAdapter(list, ::frequencyClick)
    }

    private fun frequencyClick(frequencyName: String) {
        binding.etFrequency.text = frequencyName
        frequencyDialog.dismiss()
    }

    private lateinit var severityDialog: Dialog
    private fun showSeverityDialog() {
        severityDialog = Dialog(requireActivity())
        severityDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        severityDialog.window?.setBackgroundDrawableResource(R.color.transparent)
        severityDialog.setContentView(R.layout.layout_list_dialog)
        val rvDialogList =
            severityDialog.findViewById<RecyclerView>(R.id.rvDialogList)

        severityDialog.setCancelable(true)
        severityDialog.setCanceledOnTouchOutside(true)
        severityDialog.show()
        severityDialog.fullWidthDialog()
        rvDialogList.setVerticalLayoutManager(requireActivity())
        val hospitalName = ArrayList<String>()
        hospitalName.add("Mild")
        hospitalName.add("Moderate")
        hospitalName.add("Severe")
        hospitalName.add("Trivial")
        hospitalName.add("Very severe")
        rvDialogList.adapter = CommonStringListAdapter(hospitalName, ::severityClick)
    }

    private fun severityClick(severityName: String) {
        binding.etSeverity.text = severityName
        severityDialog.dismiss()
    }


    private lateinit var durationDialog: Dialog
    private fun showDurationDialog() {
        durationDialog = Dialog(requireActivity())
        durationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        durationDialog.window?.setBackgroundDrawableResource(R.color.transparent)
        durationDialog.setContentView(R.layout.layout_list_dialog)
        val rvDialogList =
            durationDialog.findViewById<RecyclerView>(R.id.rvDialogList)

        durationDialog.setCancelable(true)
        durationDialog.setCanceledOnTouchOutside(true)
        durationDialog.show()
        durationDialog.fullWidthDialog()
        rvDialogList.setVerticalLayoutManager(requireActivity())
        val hospitalName = ArrayList<String>()
        hospitalName.add("Less than 3 months")
        hospitalName.add("3 to 6 months")
        hospitalName.add("6 to 12 months ")
        hospitalName.add("More than 12 months")
        rvDialogList.adapter = CommonStringListAdapter(hospitalName, ::relationClick)
    }

    private fun relationClick(durationName: String) {
        binding.etDuration.text = durationName
        durationDialog.dismiss()
    }


}