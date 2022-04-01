package com.prismsoft.intrepidnetworksswapi

import android.app.Application
import com.prismsoft.intrepidnetworksswapi.di.dataModule
import com.prismsoft.intrepidnetworksswapi.di.networkModule
import com.prismsoft.intrepidnetworksswapi.di.viewModelModule
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.bind
import org.kodein.di.singleton

class App() : Application(), DIAware {
    override val di by DI.lazy {
        bind<App>() with singleton { this@App }
        import(networkModule)
        import(viewModelModule)
        import(dataModule)
    }
}