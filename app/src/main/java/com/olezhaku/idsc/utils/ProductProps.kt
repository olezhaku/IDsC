package com.olezhaku.idsc.utils

import android.content.Context
import com.olezhaku.idsc.data.Device


private const val SCRIPT_ASSET_NAME = "00-product-props.sh"

private const val IDSC_DIR = "/data/adb/idsc"
private const val CONFIG_PATH = "$IDSC_DIR/product-props.conf"

private const val POST_FS_DATA_DIR = "/data/adb/post-fs-data.d"
private const val SCRIPT_PATH = "$POST_FS_DATA_DIR/$SCRIPT_ASSET_NAME"


object ProductPropsInstaller {
    suspend fun install(
        context: Context,
        device: Device
    ) {
        installScript(context)
        writeConfig(device)
        writeRuntimeSettings(device)
    }

    private suspend fun installScript(context: Context) {
        val script = context.assets
            .open(SCRIPT_ASSET_NAME)
            .bufferedReader()
            .use { it.readText() }

        // Keep the managed boot script in sync with the bundled asset on every apply.
        RootShell.run(
            """
mkdir -p '$IDSC_DIR'
mkdir -p '$POST_FS_DATA_DIR'

cat > '$SCRIPT_PATH' <<'EOF_IDSC_SCRIPT'
$script
EOF_IDSC_SCRIPT

chmod 755 '$SCRIPT_PATH'
            """.trimIndent()
        )
    }

    private suspend fun writeConfig(device: Device) {
        val config = createConfig(device)

        RootShell.run(
            """
mkdir -p '$IDSC_DIR'

cat > '$CONFIG_PATH' <<'EOF_IDSC_CONFIG'
$config
EOF_IDSC_CONFIG

chmod 644 '$CONFIG_PATH'
            """.trimIndent()
        )
    }

    private suspend fun writeRuntimeSettings(device: Device) {
        val marketingName = normalizeMarketingName(device.marketing_name)

        RootShell.run(
            """
settings put global device_name ${shellQuote(marketingName)} 2>/dev/null || true
settings put global wifi_ap_ssid ${shellQuote(marketingName)} 2>/dev/null || true
settings put global wifi_p2p_device_name ${shellQuote(marketingName)} 2>/dev/null || true
            """.trimIndent()
        )
    }
}
