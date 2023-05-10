package co.nimblehq.di.modules

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class GsonModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()
}
