package com.prismsoft.intrepidnetworksswapi.di

import com.prismsoft.intrepidnetworksswapi.MainViewModel
import com.prismsoft.intrepidnetworksswapi.viewmodel.KodeinViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

val viewModelModule = Kodein.Module("viewModelModule") {
    bind<KodeinViewModelFactory>() with singleton {
        KodeinViewModelFactory(kodein)
    }

    bind<MainViewModel>() with provider {
        MainViewModel(instance())
    }
}
