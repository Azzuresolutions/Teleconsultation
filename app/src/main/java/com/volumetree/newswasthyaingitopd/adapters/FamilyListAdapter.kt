package com.volumetree.newswasthyaingitopd.adapters

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.volumetree.newswasthyaingitopd.databinding.FamilyMemberListRowBinding
import com.volumetree.newswasthyaingitopd.model.responseData.cho.ChoPatientData

class FamilyListAdapter(
    private val familyMembers: ArrayList<ChoPatientData>,
    val click: (ChoPatientData, Dialog, Int) -> Unit,
    val familyDialog: Dialog
) :
    RecyclerView.Adapter<FamilyListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FamilyMemberListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(familyMembers[position])
    }

    override fun getItemCount() = familyMembers.size

    inner class ViewHolder(private val binding: FamilyMemberListRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(familyMemberData: ChoPatientData) {
            binding.tvPatientName.text =
                "${familyMemberData.firstName} ${familyMemberData.lastName}"
            binding.tvRelation.text = familyMemberData.relationName
            binding.root.setOnClickListener { click.invoke(familyMemberData, familyDialog, 1) }
            binding.imgEdit.setOnClickListener { click.invoke(familyMemberData, familyDialog, 2) }
            if (familyMemberData.relationName == "Myself") {
                binding.imgEdit.visibility = View.INVISIBLE
            } else {
                binding.imgEdit.visibility = View.VISIBLE
            }
        }
    }
}