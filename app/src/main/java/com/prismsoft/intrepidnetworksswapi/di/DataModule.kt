package com.prismsoft.intrepidnetworksswapi.di

import com.prismsoft.intrepidnetworksswapi.dto.EpisodeListNavArg
import com.prismsoft.intrepidnetworksswapi.dto.EpisodeNavArg
import com.prismsoft.intrepidnetworksswapi.storage.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton


val dataModule = Kodein.Module("dataModule") {
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