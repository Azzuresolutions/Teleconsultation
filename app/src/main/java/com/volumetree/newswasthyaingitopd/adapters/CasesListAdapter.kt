package com.volumetree.newswasthyaingitopd.adapters

import android.content.Intent
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.activities.ChatCallActivity
import com.volumetree.newswasthyaingitopd.application.App
import com.volumetree.newswasthyaingitopd.databinding.CasesRowBinding
import com.volumetree.newswasthyaingitopd.enums.CaseType
import com.volumetree.newswasthyaingitopd.enums.UserTypes
import com.volumetree.newswasthyaingitopd.model.responseData.cho.CaseData
import com.volumetree.newswasthyaingitopd.model.responseData.cho.ChoPatientData
import com.volumetree.newswasthyaingitopd.utils.Constants
import com.volumetree.newswasthyaingitopd.utils.SignalR
import com.volumetree.newswasthyaingitopd.utils.fromStrToDate
import com.volumetree.newswasthyaingitopd.utils.loadImageURL

class CasesListAdapter(
    private val cases: ArrayList<CaseData>,
    val updateCase: ((ChoPatientData, Int) -> Unit)?,
    val tryAgain: ((CaseData) -> Unit)?
) :
    RecyclerView.Adapter<CasesListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CasesRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }


    fun addList(newList: ArrayList<CaseData>) {
        cases.clear()
        cases.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cases[position])
    }

    override fun getItemCount() = cases.size

    inner class ViewHolder(private val binding: CasesRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(caseData: CaseData) {
            binding.imgDr.loadImageURL(binding.root.context, caseData.imagepath, user = true)
            binding.tvDrName.text = "${caseData.patientFirstName} ${caseData.patientLastName}"
            binding.tvQueryValue.text = caseData.recentQuery
            when (caseData.statusId) {
                CaseType.DRAFTED.type -> {
                    binding.tvStatus.text = Html.fromHtml(
                        "Status: <font color='#26A65B'>${caseData.statusName}</font>"
                    )

                    binding.tvConsultBy.text = Html.fromHtml(
                        "Drafted On: <font color='#818082'>${caseData.referredOn.fromStrToDate()}</font>"
                    )
                    binding.tvConsultDate.visibility = View.GONE
                    binding.tvChat.text =
                        binding.root.context.getString(R.string.update_case_record)
                    binding.btnTryAgain.visibility = View.GONE

                    binding.tvChat.setOnClickListener {

                        updateCase?.invoke(
                            ChoPatientData(
                                patientInfoId = caseData.patientInfoID.toInt(),
                                firstName = caseData.patientFirstName,
                                lastName = caseData.patientLastName,
                                imagePath = caseData.imagepath,
                                mobile = caseData.patientMobile,
                                dob = caseData.patientDOB,
                                genderId = caseData.patientGenderId.toInt()
                            ),
                            caseData.consultationId.toInt()
                        )
                    }
                }
                CaseType.COMPLETED.type -> {
                    binding.tvStatus.text = Html.fromHtml(
                        "Status: <font color='#26A65B'>${caseData.statusName}</font>"
                    )

                    binding.tvConsultBy.text = Html.fromHtml(
                        "Consulted By: <font color='#818082'>${caseData.sendToName}</font>"
                    )
                    binding.tvConsultDate.visibility = View.VISIBLE
                    binding.tvConsultDate.text = Html.fromHtml(
                        "Consulted On: <font color='#818082'>${caseData.referredOn.fromStrToDate()}</font>"
                    )
                    binding.tvChat.text =
                        binding.root.context.getString(R.string.view_details)
                    binding.btnTryAgain.visibility = View.GONE
                    binding.tvChat.setOnClickListener {
                        App.consultationId = caseData.consultationId.toInt()
                        val intent = Intent(
                            binding.root.context,
                            ChatCallActivity::class.java
                        )
                        intent.putExtra(
                            Constants.MESSAGE_DATA,
                            SignalR.MessageModelSignalRReceived()
                        )

                            .putExtra(Constants.TO_TYPE, UserTypes.DOCTOR.type)
                            .putExtra(Constants.TO_DOCTOR_NAME, caseData.sendToName)
                            .putExtra(Constants.DOCTOR_PROFILE_PIC, "")
                            .putExtra(Constants.TO_DOCTOR_ID, caseData.sendTo.toInt())
                            .putExtra(Constants.IS_CONSULTATION_HISTORY, true)
                            .putExtra(Constants.CONSULTATION_ID, caseData.consultationId.toInt())
                        binding.root.context.startActivity(intent)
                    }
                }
                CaseType.INCOMPLETED.type -> {
                    binding.tvStatus.text = Html.fromHtml(
                        "Status: <font color='#ED5A4B'>${caseData.statusName}</font>"
                    )

                    binding.tvConsultBy.text = Html.fromHtml(
                        "Consulted By: <font color='#818082'>${caseData.sendToName}</font>"
                    )
                    binding.tvConsultDate.visibility = View.VISIBLE
                    binding.tvConsultDate.text = Html.fromHtml(
                        "Consulted On: <font color='#818082'>${caseData.referredOn.fromStrToDate()}</font>"
                    )
                    binding.tvChat.visibility = View.GONE
                    binding.btnTryAgain.visibility = View.VISIBLE
                    binding.btnTryAgain.setOnClickListener {
                        App.consultationId = caseData.consultationId.toInt()
                        tryAgain?.invoke(caseData)
                    }
                }
                else -> {
                    binding.tvStatus.text = ""
                }
            }
        }
    }
}