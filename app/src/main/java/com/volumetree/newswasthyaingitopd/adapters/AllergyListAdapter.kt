package com.volumetree.newswasthyaingitopd.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.databinding.AllergyListRowBinding
import com.volumetree.newswasthyaingitopd.model.requestData.cho.LstConsultationAllergyModel
import com.volumetree.newswasthyaingitopd.utils.confirmationDialog
import com.volumetree.newswasthyaingitopd.utils.showToast

class AllergyListAdapter(
    val allergyList: ArrayList<LstConsultationAllergyModel>
) :
    RecyclerView.Adapter<AllergyListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AllergyListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(allergyList[position])
    }

    override fun getItemCount() = allergyList.size

    inner class ViewHolder(private val binding: AllergyListRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(allergyData: LstConsultationAllergyModel) {
            binding.tvAllergyName.text = allergyData.name
            binding.tvStillHave.text = when (allergyData.isStill) {
                "0" -> {
                    "Yes"
                }
                "1" -> {
                    "No"
                }
                "2" -> {
                    "Maybe"
                }
                else -> {
                    "Yes"
                }
            }
            binding.tvDuration.text = allergyData.allergyDuration
            binding.tvSeverity.text = allergyData.allergySeverityName
            binding.imgDelete.setOnClickListener {
                binding.root.context.confirmationDialog(
                    "Allergy",
                    binding.root.context.getString(R.string.allergy_delete_confirmation),
                    ::onDelete,
                    adapterPosition
                )

            }
        }
    }

    private fun onDelete(isDelete: Boolean, position: Int, context: Context) {
        if (isDelete) {
            context.showToast("It is successfully deleted")
            allergyList.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}