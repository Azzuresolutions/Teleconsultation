package com.volumetree.newswasthyaingitopd.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.volumetree.newswasthyaingitopd.databinding.FamilyMemberRowBinding
import com.volumetree.newswasthyaingitopd.model.responseData.patient.FamilyMemberData
import com.volumetree.newswasthyaingitopd.utils.getAge

class MemberListAdapter(
    private val memberList: ArrayList<FamilyMemberData>,
    private val memberEditClick: (FamilyMemberData) -> Unit,
    private val memberDeleteClick: (Int) -> Unit
) :
    RecyclerView.Adapter<MemberListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FamilyMemberRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(memberList[position])
    }

    override fun getItemCount() = memberList.size
    fun deleteFromList(memberId: Int) {
        val foundData = memberList.find { it.patientInfoId == memberId }
        memberList.remove(foundData)
        notifyItemRemoved(memberList.indexOf(foundData))
    }

    inner class ViewHolder(private val binding: FamilyMemberRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(familyMember: FamilyMemberData) {
            binding.tvName.text = "${familyMember.firstName} ${familyMember.lastName}"
            binding.tvAge.text = familyMember.age.getAge()
            binding.tvRelation.text = familyMember.relationName

            binding.tvDelete.setOnClickListener {
                notifyItemRemoved(adapterPosition)
            }
            binding.tvEdit.setOnClickListener {
                memberEditClick.invoke(familyMember)
            }

            binding.tvDelete.setOnClickListener {
                memberDeleteClick.invoke(familyMember.patientInfoId)
            }

            binding.layMemberTop.setOnClickListener {
                if (binding.layMemberDetails.visibility == View.GONE) {
                    binding.layMemberDetails.visibility = View.VISIBLE
                } else {
                    binding.layMemberDetails.visibility = View.GONE
                }
            }
        }
    }
}