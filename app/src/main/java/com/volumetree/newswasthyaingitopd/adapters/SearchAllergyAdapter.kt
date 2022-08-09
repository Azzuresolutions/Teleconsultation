package com.volumetree.newswasthyaingitopd.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.volumetree.newswasthyaingitopd.databinding.SpecialityRowBinding
import com.volumetree.newswasthyaingitopd.model.responseData.comman.AllergyData

open class SearchAllergyAdapter :
    RecyclerView.Adapter<SearchAllergyAdapter.ViewHolder>() {

    var allergyList: ArrayList<AllergyData> = ArrayList()

    var onItemClick: ((AllergyData) -> Unit)? = null

    inner class ViewHolder(private val binding: SpecialityRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(doctorSpecializationData: AllergyData) {

            binding.tvRowTitle.text = doctorSpecializationData.term
            binding.root.setOnClickListener {
                onItemClick?.invoke(doctorSpecializationData)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SpecialityRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(allergyList[position])
    }

    override fun getItemCount(): Int = allergyList.size

    fun addData(list: ArrayList<AllergyData>) {
        allergyList.clear()
        allergyList = list
        notifyDataSetChanged()
    }
    }