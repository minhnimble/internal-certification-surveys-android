package co.nimblehq.data.api.operators

import co.nimblehq.data.error.JsonApiException
import co.nimblehq.data.error.UnknownException
import com.squareup.moshi.Moshi
import io.reactivex.FlowableOperator
import moe.banana.jsonapi2.Document
import okio.BufferedSource
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import retrofit2.Response
import java.io.IOException

class ApiErrorOperator<T>
internal constructor(private val moshi: Moshi) : FlowableOperator<T, Response<T>> {

    override fun apply(subscriber: Subscriber<in T>): Subscriber<in Response<T>> {
        return object : Subscriber<Response<T>> {
            override fun onComplete() {
                subscriber.onComplete()
            }

            override fun onSubscribe(s: Subscription) {
                subscriber.onSubscribe(s)
            }

            override fun onNext(response: Response<T>) {
                if (!response.isSuccessful || response.body() == null) {
                    emitError(subscriber, response)
                } else {
                    subscriber.onNext(response.body())
                    subscriber.onComplete()
                }
            }

            override fun onError(throwable: Throwable) {
                subscriber.onError(throwable)
            }
        }
    }

    private fun parseJsonApiDocumentError(source: BufferedSource?): Document? {
        return source?.let {
            moshi.adapter(Document::class.java).fromJson(source)
        }
    }

    private fun emitError(subscriber: Subscriber<in T>, response: Response<T>?) {
        try {
            val errorResponse = response?.errorBody()?.source()
            val documentError = parseJsonApiDocumentError(errorResponse)
            if (documentError?.errors != null) {
                // Returning the errors[0] from the response, also including the original response
                subscriber.onError(
                    response?.let { JsonApiException(documentError.errors.first(), it) }
                )
            } else {
                subscriber.onError(UnknownException)
            }
        } catch (exception: IOException) {
            subscriber.onError(exception)
        }
    }
}
