package co.nimblehq.extension

fun String.isEmail() = this.matches(EMAIL_PATTERN.toRegex())

private const val EMAIL_PATTERN = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
        "\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\." +
        "[a-zA-Z0-9][a-zA-Z0-9\\-]{1,25})+"
