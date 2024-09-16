package eu.anifantakis.project.library.masterdetailmodern.auth.presentation.di

import eu.anifantakis.project.library.masterdetailmodern.auth.presentation.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val authViewModelModule = module {
    viewModelOf(::RegisterViewModel)
}