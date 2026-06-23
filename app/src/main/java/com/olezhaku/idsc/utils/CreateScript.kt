package com.olezhaku.idsc.utils

import com.olezhaku.idsc.data.Device

private const val PERSIST_PATH = "/data/adb/post-fs-data.d/00-product-props.sh"

private fun shellQuote(value: String): String {
    return "'${value.replace("'", "'\\''")}'"
}

private fun normalizeMarketingName(value: String): String {
    return value.replace(
        Regex("[\\u00A0\\u1680\\u2000-\\u200A\\u202F\\u205F\\u3000]"),
        " "
    )
}

private fun resetPropLines(props: List<String>, value: String): String {
    return props.joinToString("\n") { prop ->
        "resetprop $prop \"$value\""
    }
}

fun createScript(device: Device): String {
    val randomizedBuildDate = generateBuildDate(device.build_id)
    val incremental = device.fingerprint
        .split("/")
        .getOrNull(4)
        ?.split(":")
        ?.getOrNull(0)
        .orEmpty()

    val marketingName = normalizeMarketingName(device.marketing_name)

    val propsBody = """
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
DISPLAY_ID="${'$'}{BRAND}_${'$'}{DEVICE}_${'$'}{BUILD_DATE}"
VERSION_TIME="${'$'}{DEVICE}_user_${'$'}{BUILD_DATE}_${'$'}{BUILD_TIME}"

# Fingerprints
${
        resetPropLines(
            listOf(
                "ro.product.build.fingerprint",
                "ro.build.fingerprint",
                "ro.vendor.build.fingerprint",
                "ro.system.build.fingerprint",
                "ro.system_ext.build.fingerprint",
                "ro.odm.build.fingerprint",
                "ro.build.version.base_os",
                "ro.odm_dlkm.build.fingerprint",
                "ro.vendor_dlkm.build.fingerprint",
                "ro.product.vendor.fingerprint",
                "ro.product.system.fingerprint",
                "ro.product.system_ext.fingerprint",
                "ro.product.odm.fingerprint"
            ),
            "\$FP"
        )
    }

# Display
resetprop ro.build.display.id "${'$'}DISPLAY_ID"
resetprop ro.hxy.display.custom.version "${'$'}DISPLAY_ID"
resetprop ro.version.time "${'$'}VERSION_TIME"

# Build id
${
        resetPropLines(
            listOf(
                "ro.build.id",
                "ro.product.build.id",
                "ro.vendor.build.id",
                "ro.system.build.id",
                "ro.system_ext.build.id",
                "ro.odm.build.id",
                "ro.odm_dlkm.build.id",
                "ro.vendor_dlkm.build.id"
            ),
            "\$BUILD_ID"
        )
    }
resetprop ro.build.description "sys_mssi_64_cn_armv82-user 14 ${'$'}BUILD_ID ${'$'}BUILD_DATE release-keys"

# Incremental
${
        resetPropLines(
            listOf(
                "ro.build.version.incremental",
                "ro.product.build.version.incremental",
                "ro.system.build.version.incremental",
                "ro.system_ext.build.version.incremental",
                "ro.vendor.build.version.incremental",
                "ro.odm.build.version.incremental",
                "ro.odm_dlkm.build.version.incremental",
                "ro.vendor_dlkm.build.version.incremental"
            ),
            "\$INCREMENTAL"
        )
    }

# Unix timestamps
${
        resetPropLines(
            listOf(
                "ro.build.date.utc",
                "ro.product.build.date.utc",
                "ro.system.build.date.utc",
                "ro.system_ext.build.date.utc",
                "ro.vendor.build.date.utc",
                "ro.odm.build.date.utc",
                "ro.odm_dlkm.build.date.utc",
                "ro.vendor_dlkm.build.date.utc"
            ),
            "\$BUILD_UTC"
        )
    }

# Human-readable dates
${
        resetPropLines(
            listOf(
                "ro.build.date",
                "ro.product.build.date",
                "ro.system.build.date",
                "ro.system_ext.build.date",
                "ro.vendor.build.date",
                "ro.odm.build.date",
                "ro.odm_dlkm.build.date",
                "ro.vendor_dlkm.build.date"
            ),
            "\$BUILD_DATE_STR"
        )
    }

# Security patch
resetprop ro.build.version.security_patch "${'$'}SECURITY_PATCH"
resetprop ro.vendor.build.security_patch "${'$'}SECURITY_PATCH"

# Git build info
resetprop ro.git.buildtime "${'$'}GIT_BUILDTIME"

# Google client ID
resetprop ro.com.google.clientidbase "android-${'$'}BRAND_LOWER"

# CPU
resetprop ro.hardware "${'$'}CHIPSET"
resetprop ro.soc.model "${'$'}CHIPSET"
resetprop ro.vendor.soc.model "${'$'}CHIPSET"
resetprop ro.vendor.soc.model.external_name "${'$'}CHIPSET"
resetprop ro.vendor.soc.model.part_name "${'$'}CHIPSET"

# Serial
resetprop ro.boot.serialno "${'$'}SERIAL"
resetprop ro.serialno "${'$'}SERIAL"

# Device identity
resetprop ro.sys.hxy_bt "${'$'}MARKETING"
resetprop ro.wifi.hotspot "${'$'}MARKETING"

resetprop ro.product.board "${'$'}BOARD"
resetprop ro.product.brand "${'$'}BRAND"
resetprop ro.product.model "${'$'}MODEL"
resetprop ro.product.device "${'$'}DEVICE"
resetprop ro.product.name "${'$'}DEVICE"
resetprop ro.product.manufacturer "${'$'}MANUFACTURER"

# System partition
resetprop ro.product.system.brand "${'$'}BRAND"
resetprop ro.product.system.model "${'$'}MODEL"
resetprop ro.product.system.device "${'$'}DEVICE"
resetprop ro.product.system.name "${'$'}DEVICE"
resetprop ro.product.system.manufacturer "${'$'}MANUFACTURER"

# Vendor partition
resetprop ro.product.vendor.brand "${'$'}BRAND"
resetprop ro.product.vendor.model "${'$'}MODEL"
resetprop ro.product.vendor.device "${'$'}DEVICE"
resetprop ro.product.vendor.name "${'$'}DEVICE"
resetprop ro.product.vendor.manufacturer "${'$'}MANUFACTURER"

# Product partition
resetprop ro.product.product.brand "${'$'}BRAND"
resetprop ro.product.product.model "${'$'}MODEL"
resetprop ro.product.product.device "${'$'}DEVICE"
resetprop ro.product.product.name "${'$'}DEVICE"
resetprop ro.product.product.manufacturer "${'$'}MANUFACTURER"

# System_ext partition
resetprop ro.product.system_ext.brand "${'$'}BRAND"
resetprop ro.product.system_ext.model "${'$'}MODEL"
resetprop ro.product.system_ext.device "${'$'}DEVICE"
resetprop ro.product.system_ext.name "${'$'}DEVICE"
resetprop ro.product.system_ext.manufacturer "${'$'}MANUFACTURER"

# ODM partition
resetprop ro.product.odm.brand "${'$'}BRAND"
resetprop ro.product.odm.model "${'$'}MODEL"
resetprop ro.product.odm.device "${'$'}DEVICE"
resetprop ro.product.odm.name "${'$'}DEVICE"
resetprop ro.product.odm.manufacturer "${'$'}MANUFACTURER"

# ODM DLKM partition
resetprop ro.product.odm_dlkm.brand "${'$'}BRAND"
resetprop ro.product.odm_dlkm.model "${'$'}MODEL"
resetprop ro.product.odm_dlkm.device "${'$'}DEVICE"
resetprop ro.product.odm_dlkm.name "${'$'}DEVICE"
resetprop ro.product.odm_dlkm.manufacturer "${'$'}MANUFACTURER"

# Vendor DLKM partition
resetprop ro.product.vendor_dlkm.brand "${'$'}BRAND"
resetprop ro.product.vendor_dlkm.model "${'$'}MODEL"
resetprop ro.product.vendor_dlkm.device "${'$'}DEVICE"
resetprop ro.product.vendor_dlkm.name "${'$'}DEVICE"
resetprop ro.product.vendor_dlkm.manufacturer "${'$'}MANUFACTURER"

# USB gadget & device name
echo "${'$'}MANUFACTURER" >/config/usb_gadget/g1/strings/0x409/manufacturer 2>/dev/null || true
echo "${'$'}MARKETING" >/config/usb_gadget/g1/strings/0x409/product 2>/dev/null || true
echo "${'$'}SERIAL" >/config/usb_gadget/g1/strings/0x409/serialnumber 2>/dev/null || true
""".trimIndent()

    val persistScript = """
#!/system/bin/sh
$propsBody
""".trimIndent()

    val persistDir = PERSIST_PATH.substringBeforeLast("/")

    return """
#!/system/bin/sh
# 1. Apply props immediately
$propsBody

# 2. Persist for next boot via post-fs-data hook
mkdir -p ${shellQuote(persistDir)}
cat > ${shellQuote(PERSIST_PATH)} <<'EOF_PERSIST'
$persistScript
EOF_PERSIST
chmod 755 ${shellQuote(PERSIST_PATH)}

# 3. Settings that apply now (no reboot required)
settings put global device_name "${'$'}MARKETING"
settings put global wifi_ap_ssid "${'$'}MARKETING"
settings put global wifi_p2p_device_name "${'$'}MARKETING"

sleep 1
reboot
""".trimIndent()
}
