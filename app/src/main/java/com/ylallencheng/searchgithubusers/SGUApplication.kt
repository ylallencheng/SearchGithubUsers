package com.ylallencheng.searchgithubusers

import com.ylallencheng.searchgithubusers.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class SGUApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication>? =
        DaggerAppComponent.factory().create(this)
}