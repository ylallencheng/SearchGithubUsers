package com.ylallencheng.searchgithubusers.di

import com.ylallencheng.searchgithubusers.SGUApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityModule::class,
        AppModule::class]
)
interface AppComponent : AndroidInjector<SGUApplication> {

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance application: SGUApplication): AppComponent
    }
}