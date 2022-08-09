package com.volumetree.newswasthyaingitopd.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.volumetree.newswasthyaingitopd.databinding.ChatPrescriptionListRowBinding
import com.volumetree.newswasthyaingitopd.model.responseData.comman.PrescriptionData

class ChatPrescriptionListAdapter(
    private val prescriptionList: ArrayList<PrescriptionData>,
    private val prescriptionClick: (Int) -> Unit
) :
    RecyclerView.Adapter<ChatPrescriptionListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ChatPrescriptionListRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(prescriptionList[position])
    }

    override fun getItemCount() = prescriptionList.size

    inner class ViewHolder(private val binding: ChatPrescriptionListRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(prescriptionData: PrescriptionData) {
            binding.tvTime.text = "consult on ${prescriptionData.consultationDate}"
            binding.tvDrName.text = prescriptionData.doctorName
            binding.root.setOnClickListener {
                prescriptionClick.invoke(prescriptionData.consultationId)
            }
        }
    }
}