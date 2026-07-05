package com.olezhaku.idsc.utils


fun shellQuote(value: String): String {
    return "'${value.replace("'", "'\\''")}'"
}

fun normalizeMarketingName(value: String): String {
    return value.replace(
        Regex("[\\u00A0\\u1680\\u2000-\\u200A\\u202F\\u205F\\u3000]"),
        " "
    )
}
