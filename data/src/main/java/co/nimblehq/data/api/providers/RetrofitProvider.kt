package co.nimblehq.data.api.providers

import co.nimblehq.data.api.common.secrets.Secrets
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

object RetrofitProvider {

    fun getRetrofitBuilder(
        converterFactory: Converter.Factory,
        okHttpClient: OkHttpClient
    ) : Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(Secrets.apiEndpointUrl)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
    }
}
