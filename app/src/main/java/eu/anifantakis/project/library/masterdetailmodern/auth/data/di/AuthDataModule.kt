package eu.anifantakis.project.library.masterdetailmodern.auth.data.di

import eu.anifantakis.project.library.masterdetailmodern.auth.data.AuthRepositoryImpl
import eu.anifantakis.project.library.masterdetailmodern.auth.data.EmailPatternValidator
import eu.anifantakis.project.library.masterdetailmodern.auth.domain.AuthRepository
import eu.anifantakis.project.library.masterdetailmodern.auth.domain.PatternValidator
import eu.anifantakis.project.library.masterdetailmodern.auth.domain.UserDataValidator
import io.ktor.client.plugins.auth.Auth
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataModule = module {
    singleOf(::UserDataValidator)
    single<PatternValidator> { EmailPatternValidator }

    //single<AuthRepository> { AuthRepositoryImpl(get()) }
    // same as the following line
    //singleOf(::AuthRepositoryImpl).bind<AuthRepository>()

    single<AuthRepository> { AuthRepositoryImpl(get(named("AuthHttpClient"))) }
    // same as
    // single {  AuthRepositoryImpl(get(named("AuthHttpClient"))) } bind AuthRepository::class
}