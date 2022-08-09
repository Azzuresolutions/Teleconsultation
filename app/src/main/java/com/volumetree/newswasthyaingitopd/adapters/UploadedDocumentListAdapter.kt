package com.volumetree.newswasthyaingitopd.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.volumetree.newswasthyaingitopd.R
import com.volumetree.newswasthyaingitopd.databinding.UploadedDocumentRowBinding
import com.volumetree.newswasthyaingitopd.model.responseData.patient.SelectedDocData
import com.volumetree.newswasthyaingitopd.utils.*


class UploadedDocumentListAdapter(
    val docList: ArrayList<SelectedDocData>,
    val isDelete: Boolean = true,
) :
    RecyclerView.Adapter<UploadedDocumentListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            UploadedDocumentRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(docList[position])
    }

    override fun getItemCount() = docList.size

    inner class ViewHolder(private val binding: UploadedDocumentRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(selectedDocData: SelectedDocData) {
            if (selectedDocData.bitmap == null) {
                if (Constants.imageExtensions.contains(selectedDocData.fileFlag)) {
                    binding.imgUploadedDocument.loadImageURL(
                        binding.root.context,
                        selectedDocData.base64
                    )
                } else {
                    binding.imgUploadedDocument.setIcon(
                        binding.root.context,
                        selectedDocData.fileFlag
                    )
                }
            } else {
                binding.imgUploadedDocument.setImageBitmap(selectedDocData.bitmap)
            }
            binding.tvDocName.text = selectedDocData.imgName
            binding.tvDocName.isSelected = true
            binding.imgDelete.isVisible = isDelete
            binding.imgDelete.setOnClickListener {
                binding.root.context.confirmationDialog(
                    "Delete",
                    binding.root.context.getString(R.string.delete_confirmation),
                    ::onDelete,
                    adapterPosition
                )
            }
        }
    }

    private fun onDelete(isDelete: Boolean, position: Int, context: Context) {
        if (isDelete) {
            context.showToast("It is successfully deleted")
            docList.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}