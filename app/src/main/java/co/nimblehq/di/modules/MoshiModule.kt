package co.nimblehq.di.modules

import co.nimblehq.data.api.providers.MoshiBuilderProvider
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class MoshiModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = MoshiBuilderProvider.moshiBuilder.build()
}
