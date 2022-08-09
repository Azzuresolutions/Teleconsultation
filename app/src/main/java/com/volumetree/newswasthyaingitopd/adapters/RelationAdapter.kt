package com.volumetree.newswasthyaingitopd.adapters

import android.app.Dialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.volumetree.newswasthyaingitopd.databinding.DialogListRowBinding
import com.volumetree.newswasthyaingitopd.model.responseData.patient.RelationData
import java.util.ArrayList

class RelationAdapter(
    private val relationList: ArrayList<RelationData>,
    private val relationClick: (RelationData, Dialog) -> Unit,
    private val dialog: Dialog
) :
    RecyclerView.Adapter<RelationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DialogListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RelationAdapter.ViewHolder, position: Int) {
        holder.bind(relationList[position])
    }

    override fun getItemCount() = relationList.size

    inner class ViewHolder(private val binding: DialogListRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(relationData: RelationData) {
            binding.tvRowTitle.text = relationData.name
            binding.root.setOnClickListener {
                relationClick.invoke(relationData, dialog)
            }
        }
    }
}