package com.volumetree.newswasthyaingitopd.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.databinding.VitalListRowBinding
import com.volumetree.newswasthyaingitopd.model.requestData.cho.LstConsultationTestResultsModel
import com.volumetree.newswasthyaingitopd.utils.confirmationDialog
import com.volumetree.newswasthyaingitopd.utils.showToast

class VitalListAdapter(
    val vitalList: ArrayList<LstConsultationTestResultsModel>
) :
    RecyclerView.Adapter<VitalListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            VitalListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(vitalList[position])
    }

    override fun getItemCount() = vitalList.size

    inner class ViewHolder(private val binding: VitalListRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(vitalData: LstConsultationTestResultsModel) {
            binding.tvVitalName.text = vitalData.categoryName.ifEmpty { vitalData.name }
            binding.tvResultValue.text =
                "${vitalData.result} ${vitalData.units ?: ""}"
            binding.imgDelete.setOnClickListener {

                binding.root.context.confirmationDialog(
                    "Vital",
                    binding.root.context.getString(R.string.vital_delete_confirmation),
                    ::onDelete,
                    adapterPosition
                )

            }
        }
    }


    private fun onDelete(isDelete: Boolean, position: Int, context: Context) {
        if (isDelete) {
            context.showToast("It is successfully deleted")
            vitalList.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}