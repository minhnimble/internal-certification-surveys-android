package co.nimblehq.extension

fun <T> List<T>.isValidIndex(index: Int): Boolean = index in this.indices

fun <T> List<T>.isNotValidIndex(index: Int): Boolean = !this.isValidIndex(index)
