package com.nimbl3.data.errors

import moe.banana.jsonapi2.Error
import retrofit2.HttpException
import retrofit2.Response

data class JsonApiException(val error: Error, val response: Response<*>) : HttpException(response) {

    val hasInvalidGrantCode = error.code == CODE_INVALID_GRANT
}

const val CODE_INVALID_GRANT = "invalid_grant"
