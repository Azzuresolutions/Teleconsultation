package com.volumetree.newswasthyaingitopd.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.volumetree.newswasthyaingitopd.databinding.RowDiagnosticViewBinding
import com.volumetree.newswasthyaingitopd.model.requestData.cho.LstConsultationTestResultsModel

class DiagnosticsListAdapter(
    private val diagnostics: ArrayList<LstConsultationTestResultsModel>
) :
    RecyclerView.Adapter<DiagnosticsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowDiagnosticViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(diagnostics[position])
    }

    override fun getItemCount() = diagnostics.size

    inner class ViewHolder(private val binding: RowDiagnosticViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(diagnosticData: LstConsultationTestResultsModel) {
            binding.tvDiagnosticsName.text =
                diagnosticData.categoryName.ifEmpty { diagnosticData.name }
            binding.tvDiagnosticsValue.text = "${diagnosticData.result} ${diagnosticData.units}"
        }
    }
}