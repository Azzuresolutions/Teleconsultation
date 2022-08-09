package com.volumetree.newswasthyaingitopd.interfaces

import com.volumetree.newswasthyaingitopd.model.responseData.patient.SelectedDocData

interface PicDocumentSelector {
    fun onProfilePicSelected(selectedDocData: SelectedDocData, type: Int)
}