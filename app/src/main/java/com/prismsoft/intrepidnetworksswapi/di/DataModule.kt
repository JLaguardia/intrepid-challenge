package com.prismsoft.intrepidnetworksswapi.di

import com.prismsoft.intrepidnetworksswapi.dto.EpisodeListNavArg
import com.prismsoft.intrepidnetworksswapi.dto.EpisodeNavArg
import com.prismsoft.intrepidnetworksswapi.storage.*
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton


val dataModule = DI.Module("dataModule") {
    bind<EpisodeDatabase>() with singleton {
        AppDatabase.initialize(instance())
    }
    bind<EpisodeDao>() with singleton {
        instance<EpisodeDatabase>().episodeDao()
    }
    bind<EpisodeRepository>() with singleton {
        EpisodeRepositoryImpl(instance(), instance())
    }
    bind<EpisodeListNavArg>() with singleton {
        EpisodeListNavArg(instance())
    }
    bind<EpisodeNavArg>() with singleton {
        EpisodeNavArg(instance())
    }
}