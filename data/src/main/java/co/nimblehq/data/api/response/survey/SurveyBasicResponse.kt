package co.nimblehq.data.api.response.survey

import co.nimblehq.data.api.operators.Operators
import com.squareup.moshi.Json
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource

@JsonApi(type = "survey")
data class SurveyBasicResponse(
    @field: Json(name = "title") var title: String? = null,
    @field: Json(name = "description") var description: String? = null,
    @field: Json(name = "cover_image_url") var coverImageUrl: String? = null
) : Resource()

data class SurveysMetaResponse(
    @field: Json(name = "page") var page: Int? = null,
    @field: Json(name = "pages") var pages: Int? = null
)

val List<SurveyBasicResponse>.meta: SurveysMetaResponse?
    get() = when {
        isEmpty() -> SurveysMetaResponse()
        else -> first().document.meta.get<SurveysMetaResponse>(
            Operators.moshi.adapter(SurveysMetaResponse::class.java)
        ) as? SurveysMetaResponse
    }
