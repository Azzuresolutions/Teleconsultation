package com.volumetree.newswasthyaingitopd.adapters

import android.app.Dialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.volumetree.newswasthyaingitopd.databinding.DialogListRowBinding
import com.volumetree.newswasthyaingitopd.model.responseData.comman.MaritalData

class MaritalStatusAdapter(
    private val maritalStatus: ArrayList<MaritalData>,
    val click: (maritalData: MaritalData, dialog: Dialog) -> Unit,
    val dialog: Dialog
) :
    RecyclerView.Adapter<MaritalStatusAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DialogListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(maritalStatus[position])
    }

    override fun getItemCount() = maritalStatus.size

    inner class ViewHolder(private val binding: DialogListRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(maritalData: MaritalData) {

            binding.tvRowTitle.text = maritalData.typeName
            binding.tvRowTitle.setOnClickListener {
                click.invoke(maritalData, dialog)
            }
        }
    }
}