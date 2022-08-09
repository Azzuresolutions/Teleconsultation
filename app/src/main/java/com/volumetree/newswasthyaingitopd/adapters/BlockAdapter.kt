package com.volumetree.newswasthyaingitopd.adapters

import android.app.Dialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.volumetree.newswasthyaingitopd.databinding.DialogListRowBinding
import com.volumetree.newswasthyaingitopd.model.responseData.comman.BlockModelData

class BlockAdapter(
    private val blockList: ArrayList<BlockModelData>,
    val reconsult: (blockModelData: BlockModelData, blockDialog: Dialog) -> Unit,
    val blockDialog: Dialog
) :
    RecyclerView.Adapter<BlockAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DialogListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(blockList[position])
    }

    override fun getItemCount() = blockList.size

    inner class ViewHolder(private val binding: DialogListRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(blockModelData: BlockModelData) {

            binding.tvRowTitle.text = blockModelData.blockName
            binding.tvRowTitle.setOnClickListener {
                reconsult.invoke(blockModelData, blockDialog)
            }
        }
    }
}