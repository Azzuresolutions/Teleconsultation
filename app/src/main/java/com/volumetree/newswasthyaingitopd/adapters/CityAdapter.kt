package com.volumetree.newswasthyaingitopd.adapters

import android.app.Dialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.volumetree.newswasthyaingitopd.databinding.DialogListRowBinding
import com.volumetree.newswasthyaingitopd.model.responseData.comman.CityModelData

class CityAdapter(
    private val docList: ArrayList<CityModelData>,
    val reconsult: (cityModelData: CityModelData, cityDialog: Dialog) -> Unit,
    val cityDialog: Dialog
) :
    RecyclerView.Adapter<CityAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DialogListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(docList[position])
    }

    override fun getItemCount() = docList.size

    inner class ViewHolder(private val binding: DialogListRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cityModelData: CityModelData) {

            binding.tvRowTitle.text = cityModelData.cityName
            binding.tvRowTitle.setOnClickListener {
                reconsult.invoke(cityModelData, cityDialog)
            }
        }
    }
}