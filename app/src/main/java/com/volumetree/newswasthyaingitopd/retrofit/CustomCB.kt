package com.volumetree.newswasthyaingitopd.retrofit

import android.content.Context
import com.volumetree.newswasthyaingitopd.application.App
import com.volumetree.newswasthyaingitopd.model.responseData.comman.BaseResponse
import com.volumetree.newswasthyaingitopd.utils.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class CustomCB<T>(
    onAPIResponse: OnAPIResponse,
    val context: Context
) : Callback<T> {
    private var onAPIResponse: OnAPIResponse? = null

    interface OnAPIResponse {
        fun onResponse(bodyResponse: Any?)
    }

    override fun onResponse(
        call: Call<T>,
        response: Response<T>
    ) {
        if (response.code() == HttpURLConnection.HTTP_OK) {
            if (response.body() is BaseResponse) {
                val responseBody = response.body() as BaseResponse
                when (responseBody.requestCode) {
                    401 -> {
                        (context.applicationContext as App).confirmLogout(
                            true,
                            "CustomMessage"
                        )
                    }
                    else -> {
                        if (responseBody.success) {
                            onAPIResponse?.onResponse(responseBody)
                        } else {
                            onAPIResponse?.onResponse(responseBody)
                            context.showToast(responseBody.message)
                        }
                    }
                }
            } else {
                onAPIResponse?.onResponse(response.body())
            }
        } else if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED
        ) {
            (context.applicationContext as App).confirmLogout(true, "CustomMessage")
        } else {
            response.errorBody()?.string()?.let { context.showToast(it) }


        }
    }

    override fun onFailure(call: Call<T?>, t: Throwable) {
        context.showToast(t.message.toString())
    }

    init {
        this.onAPIResponse = onAPIResponse
    }
}