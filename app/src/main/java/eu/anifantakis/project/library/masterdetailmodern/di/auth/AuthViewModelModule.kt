package eu.anifantakis.project.library.masterdetailmodern.di.auth

import eu.anifantakis.project.library.masterdetailmodern.auth.presentation.login.LoginViewModel
import eu.anifantakis.project.library.masterdetailmodern.auth.presentation.register.RegisterViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authViewModelModule = module {
    viewModelOf(::RegisterViewModel)
    viewModelOf(::LoginViewModel)
}