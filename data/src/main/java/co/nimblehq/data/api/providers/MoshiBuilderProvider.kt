package co.nimblehq.data.api.providers

import co.nimblehq.data.api.parser.SurveyResponseParser
import co.nimblehq.data.api.parser.SurveysListResponseParser
import co.nimblehq.data.api.response.survey.SurveyResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.util.*

object MoshiBuilderProvider {

    val moshiBuilder: Moshi.Builder
        get() {
            return Moshi.Builder()
                .add(Date::class.java, Rfc3339DateJsonAdapter())
                .add(SurveyResponse::class.java, SurveyResponseParser())
                .add(Types.newParameterizedType(List::class.java, SurveyResponse::class.java), SurveysListResponseParser())
                .add(KotlinJsonAdapterFactory())
        }
}
