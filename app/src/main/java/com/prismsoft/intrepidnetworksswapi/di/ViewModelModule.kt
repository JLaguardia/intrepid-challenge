package com.prismsoft.intrepidnetworksswapi.di

import com.prismsoft.intrepidnetworksswapi.MainViewModel
import com.prismsoft.intrepidnetworksswapi.viewmodel.DiViewModelFactory
import org.kodein.di.*

val viewModelModule = DI.Module("viewModelModule") {
    bind<DiViewModelFactory>() with singleton {
        DiViewModelFactory(di)
    }

    bind<MainViewModel>() with provider {
        MainViewModel(instance())
    }
}
