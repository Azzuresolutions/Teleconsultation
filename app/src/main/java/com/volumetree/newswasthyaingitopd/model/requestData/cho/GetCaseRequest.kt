package com.volumetree.newswasthyaingitopd.model.requestData.cho


data class GetCaseRequest(
    val userId: Int = 0,
    val searchWord: String = "",
    val currentPage: Int = 1,
    val aParameter: Int = 0,
    val bParameter: String = "",
    val skip: Int = 0,
    val totalItems: Int = 0,
    val itemsPerPage: Int = 10,
    val EnterSearch: String = ""
)
