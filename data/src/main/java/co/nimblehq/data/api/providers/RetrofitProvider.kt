package co.nimblehq.data.api.providers

import co.nimblehq.data.api.common.secrets.Secrets
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

object RetrofitProvider {

    fun getRetrofitBuilder(
        converterFactories: List<Converter.Factory>,
        okHttpClient: OkHttpClient
    ) : Retrofit.Builder {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(Secrets.apiEndpointUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
        converterFactories.forEach { retrofitBuilder.addConverterFactory(it) }
        return retrofitBuilder
    }
}
