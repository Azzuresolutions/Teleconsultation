package com.volumetree.newswasthyaingitopd.adapters

import android.app.Dialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.volumetree.newswasthyaingitopd.databinding.DialogListRowBinding
import com.volumetree.newswasthyaingitopd.model.responseData.comman.BloodGroupData
import com.volumetree.newswasthyaingitopd.model.responseData.comman.MaritalData

class BloodGroupAdapter(
    private val bloodGroupList: ArrayList<BloodGroupData>,
    val click: (maritalData: BloodGroupData, dialog: Dialog) -> Unit,
    val dialog: Dialog
) :
    RecyclerView.Adapter<BloodGroupAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DialogListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(bloodGroupList[position])
    }

    override fun getItemCount() = bloodGroupList.size

    inner class ViewHolder(private val binding: DialogListRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(bloodGroupData: BloodGroupData) {

            binding.tvRowTitle.text = bloodGroupData.bloodGroupName
            binding.tvRowTitle.setOnClickListener {
                click.invoke(bloodGroupData, dialog)
            }
        }
    }
}