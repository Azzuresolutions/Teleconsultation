package com.volumetree.newswasthyaingitopd.adapters

import android.app.Dialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.volumetree.newswasthyaingitopd.databinding.DialogListRowBinding
import com.volumetree.newswasthyaingitopd.model.responseData.comman.BloodGroupData
import com.volumetree.newswasthyaingitopd.model.responseData.comman.DurationData

class DurationAdapter(
    private val list: ArrayList<DurationData>,
    val click: (maritalData: DurationData, dialog: Dialog) -> Unit,
    val dialog: Dialog
) :
    RecyclerView.Adapter<DurationAdapter.ViewHolder>() {

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

        fun bind(listData: DurationData) {

            binding.tvRowTitle.text = listData.allergyDuration
            binding.tvRowTitle.setOnClickListener {
                click.invoke(listData, dialog)
            }
        }
    }
}