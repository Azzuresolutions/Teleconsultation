package com.volumetree.newswasthyaingitopd.adapters

import android.app.Dialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.volumetree.newswasthyaingitopd.databinding.DialogListRowBinding
import java.util.ArrayList

class CommonStringListDialogAdapter(
    private val hospitalList: ArrayList<String>,
    private val dialog: Dialog,
    private val hospitalClick: (String, Dialog) -> Unit
) :
    RecyclerView.Adapter<CommonStringListDialogAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DialogListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(hospitalList[position])
    }

    override fun getItemCount() = hospitalList.size

    inner class ViewHolder(private val binding: DialogListRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(hospitalName: String) {
            binding.tvRowTitle.text = hospitalName
            binding.root.setOnClickListener {
                hospitalClick.invoke(hospitalName, dialog)
            }
        }
    }
}