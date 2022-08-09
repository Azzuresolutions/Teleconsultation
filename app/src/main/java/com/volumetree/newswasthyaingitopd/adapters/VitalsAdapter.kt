package com.volumetree.newswasthyaingitopd.adapters

import android.app.Dialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.volumetree.newswasthyaingitopd.databinding.DialogListRowBinding
import com.volumetree.newswasthyaingitopd.model.responseData.comman.VitalData

class VitalsAdapter(
    private val list: ArrayList<VitalData>,
    val click: (vitalData: VitalData, dialog: Dialog) -> Unit,
    val dialog: Dialog
) :
    RecyclerView.Adapter<VitalsAdapter.ViewHolder>() {

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

        fun bind(listData: VitalData) {

            binding.tvRowTitle.text = listData.categoryName
            binding.tvRowTitle.setOnClickListener {
                click.invoke(listData, dialog)
            }
        }
    }
}