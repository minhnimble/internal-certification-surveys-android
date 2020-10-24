package co.nimblehq.data.api.parser

import com.squareup.moshi.JsonReader

fun JsonReader.skipNameAndValue() {
    skipName()
    skipValue()
}

fun JsonReader.isNextNull(): Boolean = peek() == JsonReader.Token.NULL

fun JsonReader.nextStringOrEmpty(): String {
    return if (isNextNull()) {
        nextNull<String>()
        ""
    } else {
        nextString()
    }
}

fun JsonReader.nextIntOrEmpty(): Int {
    return if (isNextNull()) {
        nextNull<Int>()
        0
    } else {
        nextInt()
    }
}

inline fun JsonReader.readObject(body: () -> Unit) {
    beginObject()
    while (hasNext()) {
        body()
    }
    endObject()
}

inline fun JsonReader.readArray(body: () -> Unit) {
    beginArray()
    while (hasNext()) {
        body()
    }
    endArray()
}

inline fun <T : Any> JsonReader.readArrayToList(body: () -> T?): List<T> {
    val result = mutableListOf<T>()
    beginArray()
    while (hasNext()) {
        body()?.let { result.add(it) }
    }
    endArray()
    return result
}
