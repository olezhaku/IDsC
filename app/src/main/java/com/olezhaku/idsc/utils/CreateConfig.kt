package com.olezhaku.idsc.utils

import com.olezhaku.idsc.data.Device


private fun shellQuote(value: String): String {
    return "'${value.replace("'", "'\\''")}'"
}

private fun normalizeMarketingName(value: String): String {
    return value.replace(
        Regex("[\\u00A0\\u1680\\u2000-\\u200A\\u202F\\u205F\\u3000]"),
        " "
    )
}

fun createConfig(device: Device): String {
    val randomizedBuildDate = generateBuildDate(device.build_id)

    val incremental = device.fingerprint
        .split("/")
        .getOrNull(4)
        ?.split(":")
        ?.getOrNull(0)
        .orEmpty()

    val marketingName = normalizeMarketingName(device.marketing_name)

    return """
FP=${shellQuote(device.fingerprint)}
BRAND=${shellQuote(device.brand)}
MARKETING=${shellQuote(marketingName)}
DEVICE=${shellQuote(device.device)}
MODEL=${shellQuote(device.model)}
BOARD=${shellQuote(device.board)}
MANUFACTURER=${shellQuote(device.manufacturer)}
SERIAL=${shellQuote(device.serial)}
BUILD_ID=${shellQuote(device.build_id)}
CHIPSET=${shellQuote(device.chipset)}
BUILD_DATE=${shellQuote(randomizedBuildDate.buildDate)}
BUILD_TIME=${shellQuote(randomizedBuildDate.buildTime)}
BUILD_DATE_STR=${shellQuote(randomizedBuildDate.buildDateStr)}
BUILD_UTC=${shellQuote(randomizedBuildDate.buildUtc)}
INCREMENTAL=${shellQuote(incremental)}
BRAND_LOWER=${shellQuote(device.brand.lowercase())}
SECURITY_PATCH=${shellQuote(randomizedBuildDate.securityPatch)}
GIT_BUILDTIME=${shellQuote(randomizedBuildDate.gitBuildTime)}
""".trimIndent()
}