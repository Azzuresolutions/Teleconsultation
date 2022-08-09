package com.volumetree.newswasthyaingitopd.adapters

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.application.App
import com.volumetree.newswasthyaingitopd.databinding.CasesRowBinding
import com.volumetree.newswasthyaingitopd.enums.CaseType
import com.volumetree.newswasthyaingitopd.model.responseData.cho.CaseData
import com.volumetree.newswasthyaingitopd.utils.fromStrToDate
import com.volumetree.newswasthyaingitopd.utils.loadImageURL

class DoctorCasesListAdapter(
    private val cases: ArrayList<CaseData>,
    val viewDetails: ((Int) -> Unit)?,
    val addPrescription: ((CaseData) -> Unit)?
) :
    RecyclerView.Adapter<DoctorCasesListAdapter.ViewHolder>() {

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
                CaseType.COMPLETED.type -> {
                    binding.tvStatus.text = Html.fromHtml(
                        "Status: <font color='#26A65B'>${caseData.statusName}</font>"
                    )

                    binding.tvConsultBy.text = Html.fromHtml(
                        "Consulted By: <font color='#818082'>${caseData.sendByName}</font>"
                    )
                    binding.tvConsultDate.visibility = View.VISIBLE
                    binding.tvConsultDate.text = Html.fromHtml(
                        "Consulted On: <font color='#818082'>${caseData.receivedOn.fromStrToDate()}</font>"
                    )
                    binding.tvChat.text = binding.root.context.getString(R.string.view_details)
                    binding.btnTryAgain.visibility = View.GONE
                    binding.tvChat.setOnClickListener {
                        viewDetails?.invoke(caseData.consultationId.toInt())
                    }
                }
                CaseType.INCOMPLETED.type -> {
                    binding.tvStatus.text = Html.fromHtml(
                        "Status: <font color='#ED5A4B'>${caseData.statusName}</font>"
                    )

                    binding.tvConsultBy.text = Html.fromHtml(
                        "Consulted By: <font color='#818082'>${caseData.sendByName}</font>"
                    )
                    binding.tvConsultDate.visibility = View.VISIBLE
                    binding.tvConsultDate.text = Html.fromHtml(
                        "Consulted On: <font color='#818082'>${caseData.receivedOn.fromStrToDate()}</font>"
                    )
                    binding.tvChat.visibility = View.GONE
                    binding.btnTryAgain.visibility = View.VISIBLE
                    binding.btnTryAgain.text =
                        binding.root.context.getString(R.string.add_prescription)
                    binding.btnTryAgain.setOnClickListener {
                        App.consultationId = caseData.consultationId.toInt()
                        addPrescription?.invoke(caseData)
                    }
                }
                else -> {
                    binding.tvStatus.text = ""
                }
            }
        }
    }
}