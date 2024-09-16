package eu.anifantakis.project.library.masterdetailmodern.auth.data.di

import eu.anifantakis.project.library.masterdetailmodern.auth.data.AuthRepositoryImpl
import eu.anifantakis.project.library.masterdetailmodern.auth.data.EmailPatternValidator
import eu.anifantakis.project.library.masterdetailmodern.auth.domain.AuthRepository
import eu.anifantakis.project.library.masterdetailmodern.auth.domain.PatternValidator
import eu.anifantakis.project.library.masterdetailmodern.auth.domain.UserDataValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val authDataModule = module {
    singleOf(::UserDataValidator)
    single<PatternValidator> { EmailPatternValidator }

    single<AuthRepository> { AuthRepositoryImpl(get()) }
    // same as the following line
    //singleOf(::AuthRepositoryImpl).bind<AuthRepository>()

}