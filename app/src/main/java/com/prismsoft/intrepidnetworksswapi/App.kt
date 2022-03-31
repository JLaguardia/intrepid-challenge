package com.prismsoft.intrepidnetworksswapi

import android.app.Application
import com.prismsoft.intrepidnetworksswapi.di.dataModule
import com.prismsoft.intrepidnetworksswapi.di.networkModule
import com.prismsoft.intrepidnetworksswapi.di.viewModelModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class App() : Application(), KodeinAware {
    override val kodein by Kodein.lazy {
        bind<App>() with singleton { this@App }
        import(networkModule)
        import(viewModelModule)
        import(dataModule)
    }

}