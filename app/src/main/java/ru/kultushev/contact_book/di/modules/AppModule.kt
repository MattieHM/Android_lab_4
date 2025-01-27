package ru.kultushev.contact_book.di.modules

import android.content.Context
import toothpick.config.Module
import toothpick.ktp.binding.bind

class AppModule(applicationContext: Context) : Module() {

    init {
        bind<Context>().toInstance(applicationContext)
    }

}