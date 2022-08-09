package com.volumetree.newswasthyaingitopd.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.volumetree.newswasthyaingitopd.databinding.RegisteredPatientRowBinding
import com.volumetree.newswasthyaingitopd.model.responseData.cho.ChoPatientData
import com.volumetree.newswasthyaingitopd.model.responseData.cho.ChoPatientResponse
import com.volumetree.newswasthyaingitopd.utils.*

class RegisteredPatientListAdapter(
    private val patientList: ArrayList<ChoPatientData>,
    val createCase: (ChoPatientData) -> Unit,
    val viewHistory: (ChoPatientData) -> Unit,
    val edit: (ChoPatientData) -> Unit
) : RecyclerView.Adapter<RegisteredPatientListAdapter.ViewHolder>() {

    fun addList(newList: ArrayList<ChoPatientData>) {
        patientList.clear()
        patientList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RegisteredPatientRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(patientList[position])
    }

    override fun getItemCount() = patientList.size

    inner class ViewHolder(private val binding: RegisteredPatientRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(patientData: ChoPatientData) {
            binding.tvPatientName.text =
                "${patientData.firstName} ${patientData.lastName}".splitName()
            binding.tvPatientId.text = "Patient ID: ${patientData.patientInfoId}"
            binding.tvPhoneNumber.text = patientData.mobile
            if (patientData.mobile == null || patientData.mobile.isEmpty()) {
                binding.imgPhone.visibility = View.GONE
            } else {
                binding.imgPhone.visibility = View.VISIBLE
            }
            binding.tvDOB.text = patientData.createdDate.fromStrToDate()
            binding.tvGender.text = patientData.genderId.getGenderNameFromId()
            binding.btnCreateCase.setOnClickListener {
                createCase.invoke(patientData)
            }
            binding.tvViewHistory.setOnClickListener {
                viewHistory.invoke(patientData)
            }
            binding.tvEdit.setOnClickListener { edit.invoke(patientData) }
            binding.imgDr.loadImageURL(binding.root.context, patientData.imagePath, user = true)
        }
    }
}