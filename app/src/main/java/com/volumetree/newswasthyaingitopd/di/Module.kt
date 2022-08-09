package com.volumetree.newswasthyaingitopd.di

import com.volumetree.newswasthyaingitopd.repository.cho.ChoRepository
import com.volumetree.newswasthyaingitopd.repository.cho.ChoRepositoryImpl
import com.volumetree.newswasthyaingitopd.repository.doctor.DoctorRepository
import com.volumetree.newswasthyaingitopd.repository.doctor.DoctorRepositoryImpl
import com.volumetree.newswasthyaingitopd.repository.login.LoginRepository
import com.volumetree.newswasthyaingitopd.repository.login.LoginRepositoryImpl
import com.volumetree.newswasthyaingitopd.repository.master.MasterRepository
import com.volumetree.newswasthyaingitopd.repository.master.MasterRepositoryImpl
import com.volumetree.newswasthyaingitopd.repository.patient.PatientRepository
import com.volumetree.newswasthyaingitopd.repository.patient.PatientRepositoryImpl
import com.volumetree.newswasthyaingitopd.repository.profile.ProfileRepository
import com.volumetree.newswasthyaingitopd.repository.profile.ProfileRepositoryImpl
import com.volumetree.newswasthyaingitopd.viewmodel.*
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val repositoryModule = module {
    single<LoginRepository> { LoginRepositoryImpl(get(), get(), get(), androidContext()) }
    single<ProfileRepository> { ProfileRepositoryImpl(get(), get(), androidContext()) }
    single<MasterRepository> { MasterRepositoryImpl(get(), androidContext()) }
    single<PatientRepository> { PatientRepositoryImpl(get(), androidContext()) }
    single<ChoRepository> { ChoRepositoryImpl(get(), androidContext()) }
    single<DoctorRepository> { DoctorRepositoryImpl(get(), androidContext()) }
}

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { MasterViewModel(get()) }
    viewModel { PatientViewModel(get()) }
    viewModel { ChoViewModel(get()) }
    viewModel { DoctorViewModel(get()) }
}



