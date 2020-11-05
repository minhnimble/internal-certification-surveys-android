package co.nimblehq.data.api.operators

import co.nimblehq.data.api.providers.MoshiBuilderProvider
import com.squareup.moshi.Moshi

object Operators {
    val moshi: Moshi by lazy { MoshiBuilderProvider.moshiBuilder.build() }

    fun <T> apiError(): ApiErrorOperator<T> {
        return ApiErrorOperator(moshi)
    }
}
