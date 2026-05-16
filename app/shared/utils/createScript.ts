import { IDevice } from "../types";
import { generateBuildDate } from "./buildDate";

export const createScript = (device: IDevice) => {
  const PERSIST_PATH = "/data/adb/post-fs-data.d/00-product-props.sh";

  const randomizedBuildDate = generateBuildDate(device.build_id);
  const incremental = device.fingerprint.split("/")[4]?.split(":")[0] ?? "";
  const marketingName = device.marketing_name.replace(
    /[\u00A0\u1680\u2000-\u200A\u202F\u205F\u3000]/g,
    " ",
  );
  const shellQuote = (value: string) => `'${value.replace(/'/g, `'\\''`)}'`;

  const propsBody = `
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
BRAND_LOWER=${shellQuote(device.brand.toLowerCase())}
SECURITY_PATCH=${shellQuote(randomizedBuildDate.securityPatch)}
GIT_BUILDTIME=${shellQuote(randomizedBuildDate.gitBuildTime)}
DISPLAY_ID="\${BRAND}_\${DEVICE}_\${BUILD_DATE}"
VERSION_TIME="\${DEVICE}_user_\${BUILD_DATE}_\${BUILD_TIME}"

# Fingerprints
resetprop ro.product.build.fingerprint "\$FP"
resetprop ro.build.fingerprint "\$FP"
resetprop ro.vendor.build.fingerprint "\$FP"
resetprop ro.system.build.fingerprint "\$FP"
resetprop ro.system_ext.build.fingerprint "\$FP"
resetprop ro.odm.build.fingerprint "\$FP"
resetprop ro.build.version.base_os "\$FP"
resetprop ro.odm_dlkm.build.fingerprint "\$FP"
resetprop ro.vendor_dlkm.build.fingerprint "\$FP"
resetprop ro.product.vendor.fingerprint "\$FP"
resetprop ro.product.system.fingerprint "\$FP"
resetprop ro.product.system_ext.fingerprint "\$FP"
resetprop ro.product.odm.fingerprint "\$FP"

# Display
resetprop ro.build.display.id "\$DISPLAY_ID"
resetprop ro.hxy.display.custom.version "\$DISPLAY_ID"
resetprop ro.version.time "\$VERSION_TIME"

# Build id
resetprop ro.build.id "\$BUILD_ID"
resetprop ro.product.build.id "\$BUILD_ID"
resetprop ro.vendor.build.id "\$BUILD_ID"
resetprop ro.system.build.id "\$BUILD_ID"
resetprop ro.system_ext.build.id "\$BUILD_ID"
resetprop ro.odm.build.id "\$BUILD_ID"
resetprop ro.odm_dlkm.build.id "\$BUILD_ID"
resetprop ro.vendor_dlkm.build.id "\$BUILD_ID"
resetprop ro.build.description "sys_mssi_64_cn_armv82-user 14 \$BUILD_ID \$BUILD_DATE release-keys"

# Incremental
resetprop ro.build.version.incremental "\$INCREMENTAL"
resetprop ro.product.build.version.incremental "\$INCREMENTAL"
resetprop ro.system.build.version.incremental "\$INCREMENTAL"
resetprop ro.system_ext.build.version.incremental "\$INCREMENTAL"
resetprop ro.vendor.build.version.incremental "\$INCREMENTAL"
resetprop ro.odm.build.version.incremental "\$INCREMENTAL"
resetprop ro.odm_dlkm.build.version.incremental "\$INCREMENTAL"
resetprop ro.vendor_dlkm.build.version.incremental "\$INCREMENTAL"

# Unix timestamps
resetprop ro.build.date.utc "\$BUILD_UTC"
resetprop ro.product.build.date.utc "\$BUILD_UTC"
resetprop ro.system.build.date.utc "\$BUILD_UTC"
resetprop ro.system_ext.build.date.utc "\$BUILD_UTC"
resetprop ro.vendor.build.date.utc "\$BUILD_UTC"
resetprop ro.odm.build.date.utc "\$BUILD_UTC"
resetprop ro.odm_dlkm.build.date.utc "\$BUILD_UTC"
resetprop ro.vendor_dlkm.build.date.utc "\$BUILD_UTC"

# Human-readable dates
resetprop ro.build.date "\$BUILD_DATE_STR"
resetprop ro.product.build.date "\$BUILD_DATE_STR"
resetprop ro.system.build.date "\$BUILD_DATE_STR"
resetprop ro.system_ext.build.date "\$BUILD_DATE_STR"
resetprop ro.vendor.build.date "\$BUILD_DATE_STR"
resetprop ro.odm.build.date "\$BUILD_DATE_STR"
resetprop ro.odm_dlkm.build.date "\$BUILD_DATE_STR"
resetprop ro.vendor_dlkm.build.date "\$BUILD_DATE_STR"

# Security patch
resetprop ro.build.version.security_patch "\$SECURITY_PATCH"
resetprop ro.vendor.build.security_patch "\$SECURITY_PATCH"

# Git build info
resetprop ro.git.buildtime "\$GIT_BUILDTIME"

# Google client ID
resetprop ro.com.google.clientidbase "android-\$BRAND_LOWER"

# CPU
resetprop ro.hardware "\$CHIPSET"
resetprop ro.soc.model "\$CHIPSET"
resetprop ro.vendor.soc.model "\$CHIPSET"
resetprop ro.vendor.soc.model.external_name "\$CHIPSET"
resetprop ro.vendor.soc.model.part_name "\$CHIPSET"

# Serial
resetprop ro.boot.serialno "\$SERIAL"
resetprop ro.serialno "\$SERIAL"

# Device identity
resetprop ro.sys.hxy_bt "\$MARKETING"
resetprop ro.wifi.hotspot "\$MARKETING"

resetprop ro.product.board "\$BOARD"
resetprop ro.product.brand "\$BRAND"
resetprop ro.product.model "\$MODEL"
resetprop ro.product.device "\$DEVICE"
resetprop ro.product.name "\$DEVICE"
resetprop ro.product.manufacturer "\$MANUFACTURER"

# System partition
resetprop ro.product.system.brand "\$BRAND"
resetprop ro.product.system.model "\$MODEL"
resetprop ro.product.system.device "\$DEVICE"
resetprop ro.product.system.name "\$DEVICE"
resetprop ro.product.system.manufacturer "\$MANUFACTURER"

# Vendor partition
resetprop ro.product.vendor.brand "\$BRAND"
resetprop ro.product.vendor.model "\$MODEL"
resetprop ro.product.vendor.device "\$DEVICE"
resetprop ro.product.vendor.name "\$DEVICE"
resetprop ro.product.vendor.manufacturer "\$MANUFACTURER"

# Product partition
resetprop ro.product.product.brand "\$BRAND"
resetprop ro.product.product.model "\$MODEL"
resetprop ro.product.product.device "\$DEVICE"
resetprop ro.product.product.name "\$DEVICE"
resetprop ro.product.product.manufacturer "\$MANUFACTURER"

# System_ext partition
resetprop ro.product.system_ext.brand "\$BRAND"
resetprop ro.product.system_ext.model "\$MODEL"
resetprop ro.product.system_ext.device "\$DEVICE"
resetprop ro.product.system_ext.name "\$DEVICE"
resetprop ro.product.system_ext.manufacturer "\$MANUFACTURER"

# ODM partition
resetprop ro.product.odm.brand "\$BRAND"
resetprop ro.product.odm.model "\$MODEL"
resetprop ro.product.odm.device "\$DEVICE"
resetprop ro.product.odm.name "\$DEVICE"
resetprop ro.product.odm.manufacturer "\$MANUFACTURER"

# ODM DLKM partition
resetprop ro.product.odm_dlkm.brand "\$BRAND"
resetprop ro.product.odm_dlkm.model "\$MODEL"
resetprop ro.product.odm_dlkm.device "\$DEVICE"
resetprop ro.product.odm_dlkm.name "\$DEVICE"
resetprop ro.product.odm_dlkm.manufacturer "\$MANUFACTURER"

# Vendor DLKM partition
resetprop ro.product.vendor_dlkm.brand "\$BRAND"
resetprop ro.product.vendor_dlkm.model "\$MODEL"
resetprop ro.product.vendor_dlkm.device "\$DEVICE"
resetprop ro.product.vendor_dlkm.name "\$DEVICE"
resetprop ro.product.vendor_dlkm.manufacturer "\$MANUFACTURER"

# USB gadget & device name
echo "\$MANUFACTURER" >/config/usb_gadget/g1/strings/0x409/manufacturer 2>/dev/null || true
echo "\$MARKETING" >/config/usb_gadget/g1/strings/0x409/product 2>/dev/null || true
echo "\$SERIAL" >/config/usb_gadget/g1/strings/0x409/serialnumber 2>/dev/null || true
`;

  const persistScript = `#!/system/bin/sh
${propsBody}`;

  return `#!/system/bin/sh
# 1. Apply props immediately
${propsBody}

# 2. Persist for next boot via post-fs-data hook
mkdir -p ${shellQuote(PERSIST_PATH.substring(0, PERSIST_PATH.lastIndexOf("/")))}
cat > ${shellQuote(PERSIST_PATH)} <<'EOF_PERSIST'
${persistScript}
EOF_PERSIST
chmod 755 ${shellQuote(PERSIST_PATH)}

# 3. Settings that apply now (no reboot required)
settings put global device_name "$MARKETING"
settings put global wifi_ap_ssid "$MARKETING"
settings put global wifi_p2p_device_name "$MARKETING"

sleep 1
reboot
`;
};
