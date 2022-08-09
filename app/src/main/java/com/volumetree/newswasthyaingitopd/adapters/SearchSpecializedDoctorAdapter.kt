package com.volumetree.newswasthyaingitopd.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.volumetree.newswasthyaingitopd.databinding.SpecializedDrRowBinding
import com.volumetree.newswasthyaingitopd.model.responseData.patient.DoctorSpecializationData
import com.volumetree.newswasthyaingitopd.utils.loadImageURL
import com.volumetree.newswasthyaingitopd.utils.manageOnlineOfflineStatusWithColor

open class SearchSpecializedDoctorAdapter :
    RecyclerView.Adapter<SearchSpecializedDoctorAdapter.ViewHolder>(), Filterable {

    var doctorList: ArrayList<DoctorSpecializationData> = ArrayList()
    var doctorListFiltered: ArrayList<DoctorSpecializationData> = ArrayList()

    var onItemClick: ((DoctorSpecializationData) -> Unit)? = null

    inner class ViewHolder(private val binding: SpecializedDrRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(doctorSpecializationData: DoctorSpecializationData) {
            binding.tvDrOnlineState.manageOnlineOfflineStatusWithColor(
                binding.root.context,
                doctorSpecializationData
            )
            binding.imgDr.loadImageURL(
                binding.root.context,
                doctor = true,
                imagePath = doctorSpecializationData.doctorimage
            )

            binding.tvExperience.visibility = View.VISIBLE
            binding.imgExperienceDot.visibility = View.VISIBLE
            binding.tvHospitalName.visibility = View.VISIBLE
            binding.imgHospitalDot.visibility = View.VISIBLE
            binding.tvRating.text = doctorSpecializationData.doctor_rating.toString()

            binding.tvHospitalName.text = doctorSpecializationData.hospitalName.ifEmpty {
                binding.tvHospitalName.visibility = View.GONE
                binding.imgHospitalDot.visibility = View.GONE
                ""
            }

            binding.tvExperience.text = doctorSpecializationData.experience.ifEmpty {
                binding.tvExperience.visibility = View.GONE
                binding.imgExperienceDot.visibility = View.GONE
                ""
            } + " yrs exp"

            binding.tvDrName.text = doctorSpecializationData.doctor_Name
            binding.root.setOnClickListener {
                onItemClick?.invoke(doctorSpecializationData)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SpecializedDrRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(doctorListFiltered[position])
    }

    override fun getItemCount(): Int = doctorListFiltered.size

    fun addData(list: List<DoctorSpecializationData>) {
        doctorList = list as ArrayList<DoctorSpecializationData>
        doctorListFiltered = doctorList
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString()?.lowercase() ?: ""
                doctorListFiltered = if (charString.isEmpty()) doctorList else {
                    val filteredList = ArrayList<DoctorSpecializationData>()
                    doctorList
                        .filter {
                            (it.doctor_Name.lowercase().contains(constraint.toString().lowercase()))
                        }
                        .forEach { filteredList.add(it) }
                    filteredList

                }
                return FilterResults().apply { values = doctorListFiltered }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                doctorListFiltered = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<DoctorSpecializationData>
                notifyDataSetChanged()
            }
        }
    }
}


