package com.volumetree.newswasthyaingitopd.adapters

import android.app.Dialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.volumetree.newswasthyaingitopd.databinding.DialogListRowBinding
import com.volumetree.newswasthyaingitopd.model.responseData.comman.DistrictModelData

class DistrictAdapter(
    private val docList: ArrayList<DistrictModelData>,
    val reconsult: (districtModelData: DistrictModelData, districtDialog: Dialog) -> Unit,
    val districtDialog: Dialog
) :
    RecyclerView.Adapter<DistrictAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DialogListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(docList[position])
    }

    override fun getItemCount() = docList.size

    inner class ViewHolder(private val binding: DialogListRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(districtModelData: DistrictModelData) {

            binding.tvRowTitle.text = districtModelData.districtName
            binding.tvRowTitle.setOnClickListener {
                reconsult.invoke(districtModelData, districtDialog)
            }
        }
    }
}