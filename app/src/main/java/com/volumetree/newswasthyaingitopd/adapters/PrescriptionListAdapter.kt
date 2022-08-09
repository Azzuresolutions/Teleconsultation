package com.volumetree.newswasthyaingitopd.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.databinding.PrescriptionListRowBinding
import com.volumetree.newswasthyaingitopd.model.requestData.cho.LstConsultationMedicineModel
import com.volumetree.newswasthyaingitopd.utils.confirmationDialog
import com.volumetree.newswasthyaingitopd.utils.showToast

class PrescriptionListAdapter(
    private val prescriptionList: ArrayList<LstConsultationMedicineModel>,
    private val deleteMedicine: (Int) -> Unit

) :
    RecyclerView.Adapter<PrescriptionListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            PrescriptionListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(prescriptionList[position])
    }

    override fun getItemCount() = prescriptionList.size

    inner class ViewHolder(private val binding: PrescriptionListRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(medicineModel: LstConsultationMedicineModel) {
            binding.tvPrescription.text = medicineModel.rxName
            binding.tvFrequency.text = medicineModel.frequency
            binding.tvType.text = medicineModel.durationtype
            binding.tvDuration.text = medicineModel.durationvalue
            binding.imgDelete.setOnClickListener {
                binding.root.context.confirmationDialog(
                    "Medicine",
                    binding.root.context.getString(R.string.medicine_delete_confirmation),
                    ::onDelete,
                    adapterPosition
                )
            }
        }
    }

    private fun onDelete(isDelete: Boolean, position: Int, context: Context) {
        if (isDelete) {
            context.showToast("It is successfully deleted")
            prescriptionList.removeAt(position)
            notifyItemRemoved(position)
            deleteMedicine.invoke(position)
        }
    }
}