package com.volumetree.newswasthyaingitopd.adapters

import android.app.Dialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.volumetree.newswasthyaingitopd.databinding.DialogListRowBinding
import com.volumetree.newswasthyaingitopd.model.responseData.comman.BloodGroupData
import com.volumetree.newswasthyaingitopd.model.responseData.comman.DurationData
import com.volumetree.newswasthyaingitopd.model.responseData.comman.SeverityData

class SeverityAdapter(
    private val list: ArrayList<SeverityData>,
    val click: (severityData: SeverityData, dialog: Dialog) -> Unit,
    val dialog: Dialog
) :
    RecyclerView.Adapter<SeverityAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DialogListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(private val binding: DialogListRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(listData: SeverityData) {

            binding.tvRowTitle.text = listData.allergySeverityName
            binding.tvRowTitle.setOnClickListener {
                click.invoke(listData, dialog)
            }
        }
    }
}