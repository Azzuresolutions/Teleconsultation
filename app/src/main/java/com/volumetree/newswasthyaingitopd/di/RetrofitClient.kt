package com.volumetree.newswasthyaingitopd.di

import android.content.Context
import com.volumetree.newswasthyaingitopd.retrofit.ChoServices
import com.volumetree.newswasthyaingitopd.retrofit.CommonServices
import com.volumetree.newswasthyaingitopd.retrofit.DoctorServices
import com.volumetree.newswasthyaingitopd.retrofit.PatientServices
import com.volumetree.newswasthyaingitopd.utils.PrefUtils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

//const val BASE_URL = "https://api-swasthyaingit.limeberry.io/"
//const val BASE_URL = "http://13.126.219.97:5001/"

const val BASE_URL = "https://beta-api.swasthyaingit.in/"
const val API_URL = "${BASE_URL}api/"
const val BASE_URL_SIGNALR = "${BASE_URL}chat"

val networkModule = module {
    factory { getInterceptor(get()) }
    factory { provideOkHttpClient(get(), get()) }
    factory { provideLoggingInterceptor() }
    factory { provideRetrofit(get()) }
    factory { providePatientService(get()) }
    factory { provideCommonService(get()) }
    factory { provideChoService(get()) }
    factory { provideDoctorService(get()) }
}

private fun getInterceptor(context: Context): Interceptor {
    if (PrefUtils.getLogin(context) > 0) {
        return Interceptor { chain ->
            val original = chain.request()
            val builder = original.newBuilder()
            val request = builder
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer ${PrefUtils.getLoginToken(context)}")
                .method(original.method, original.body)
                .build()
            chain.proceed(request)
        }
    } else {
        return Interceptor { chain ->
            val original = chain.request()
            val builder = original.newBuilder()
            val request = builder
                .header("Content-Type", "application/json")
                .method(original.method, original.body)
                .build()
            chain.proceed(request)
        }
    }
}


fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().baseUrl(API_URL).client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()
}

fun provideOkHttpClient(
    interceptor: Interceptor,
    loggingInterceptor: HttpLoggingInterceptor
): OkHttpClient {
    return OkHttpClient().newBuilder()
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS).addInterceptor(interceptor)
        .addInterceptor(loggingInterceptor)
        .build()
}

fun provideLoggingInterceptor(): HttpLoggingInterceptor {
    val logger = HttpLoggingInterceptor()
    logger.level = HttpLoggingInterceptor.Level.BODY
    return logger
}

fun providePatientService(retrofit: Retrofit): PatientServices =
    retrofit.create(PatientServices::class.java)

fun provideDoctorService(retrofit: Retrofit): DoctorServices =
    retrofit.create(DoctorServices::class.java)

fun provideCommonService(retrofit: Retrofit): CommonServices =
    retrofit.create(CommonServices::class.java)

fun provideChoService(retrofit: Retrofit): ChoServices = retrofit.create(ChoServices::class.java)
