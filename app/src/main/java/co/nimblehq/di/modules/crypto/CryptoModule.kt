package co.nimblehq.di.modules.crypto

import co.nimblehq.data.crypto.AESCrypto
import co.nimblehq.data.crypto.AESCryptoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class CryptoModule {

    @Provides
    @Singleton
    fun provideAESCrypto(): AESCrypto = AESCryptoImpl()
}
