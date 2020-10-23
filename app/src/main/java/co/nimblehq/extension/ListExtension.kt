package co.nimblehq.extension

fun <T> List<T>.isValidIndex(index: Int): Boolean = index in this.indices
