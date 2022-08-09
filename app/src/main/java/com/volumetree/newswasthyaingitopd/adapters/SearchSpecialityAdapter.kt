package com.volumetree.newswasthyaingitopd.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.volumetree.newswasthyaingitopd.databinding.SpecialityRowBinding
import com.volumetree.newswasthyaingitopd.model.responseData.patient.SpecialityData

open class SearchSpecialityAdapter :
    RecyclerView.Adapter<SearchSpecialityAdapter.ViewHolder>(), Filterable {

    var specialityList: ArrayList<SpecialityData> = ArrayList()
    var specialityListFiltered: ArrayList<SpecialityData> = ArrayList()

    var onItemClick: ((SpecialityData) -> Unit)? = null

    inner class ViewHolder(private val binding: SpecialityRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(doctorSpecializationData: SpecialityData) {

            binding.tvRowTitle.text = doctorSpecializationData.specialityName
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
        holder.bind(specialityListFiltered[position])
    }

    override fun getItemCount(): Int = specialityListFiltered.size

    fun addData(list: ArrayList<SpecialityData>) {
        specialityList = list
        specialityListFiltered = specialityList
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString()?.lowercase() ?: ""
                specialityListFiltered = if (charString.isEmpty()) specialityList else {
                    val filteredList = ArrayList<SpecialityData>()
                    specialityList
                        .filter {
                            (it.specialityName.lowercase()
                                .contains(constraint.toString().lowercase()))
                        }
                        .forEach { filteredList.add(it) }
                    filteredList

                }
                return FilterResults().apply { values = specialityListFiltered }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                specialityListFiltered = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<SpecialityData>
                notifyDataSetChanged()
            }
        }
    }
}