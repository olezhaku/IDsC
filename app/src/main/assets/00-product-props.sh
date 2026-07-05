#!/system/bin/sh

DIR="/data/adb/idsc"
CONFIG="$DIR/product-props.conf"
LOG="$DIR/product-props.log"

mkdir -p "$DIR" 2>/dev/null || true

log() {
  echo "$(date '+%Y-%m-%d %H:%M:%S') $*" >> "$LOG" 2>/dev/null || true
}

log "script started"

if [ ! -f "$CONFIG" ]; then
  log "config not found: $CONFIG"
  exit 0
fi

. "$CONFIG"

log "config loaded"

required="FP BRAND MARKETING DEVICE MODEL BOARD MANUFACTURER SERIAL BUILD_ID CHIPSET BUILD_DATE BUILD_TIME BUILD_DATE_STR BUILD_UTC INCREMENTAL BRAND_LOWER SECURITY_PATCH GIT_BUILDTIME"

for key in $required; do
  eval "value=\${$key}"
  if [ -z "$value" ]; then
    log "missing or empty required value: $key"
    exit 0
  fi
done

DISPLAY_ID="${BRAND}_${DEVICE}_${BUILD_DATE}"
VERSION_TIME="${DEVICE}_user_${BUILD_DATE}_${BUILD_TIME}"

rp() {
  resetprop "$1" "$2" 2>/dev/null || true
}

# Fingerprints
for prop in \
  ro.product.build.fingerprint \
  ro.build.fingerprint \
  ro.vendor.build.fingerprint \
  ro.system.build.fingerprint \
  ro.system_ext.build.fingerprint \
  ro.odm.build.fingerprint \
  ro.build.version.base_os \
  ro.odm_dlkm.build.fingerprint \
  ro.vendor_dlkm.build.fingerprint \
  ro.product.vendor.fingerprint \
  ro.product.system.fingerprint \
  ro.product.system_ext.fingerprint \
  ro.product.odm.fingerprint
do
  rp "$prop" "$FP"
done

# Display
rp ro.build.display.id "$DISPLAY_ID"
rp ro.hxy.display.custom.version "$DISPLAY_ID"
rp ro.version.time "$VERSION_TIME"

# Build id
for prop in \
  ro.build.id \
  ro.product.build.id \
  ro.vendor.build.id \
  ro.system.build.id \
  ro.system_ext.build.id \
  ro.odm.build.id \
  ro.odm_dlkm.build.id \
  ro.vendor_dlkm.build.id
do
  rp "$prop" "$BUILD_ID"
done

rp ro.build.description "sys_mssi_64_cn_armv82-user 14 $BUILD_ID $BUILD_DATE release-keys"

# Incremental
for prop in \
  ro.build.version.incremental \
  ro.product.build.version.incremental \
  ro.system.build.version.incremental \
  ro.system_ext.build.version.incremental \
  ro.vendor.build.version.incremental \
  ro.odm.build.version.incremental \
  ro.odm_dlkm.build.version.incremental \
  ro.vendor_dlkm.build.version.incremental
do
  rp "$prop" "$INCREMENTAL"
done

# Unix timestamps
for prop in \
  ro.build.date.utc \
  ro.product.build.date.utc \
  ro.system.build.date.utc \
  ro.system_ext.build.date.utc \
  ro.vendor.build.date.utc \
  ro.odm.build.date.utc \
  ro.odm_dlkm.build.date.utc \
  ro.vendor_dlkm.build.date.utc
do
  rp "$prop" "$BUILD_UTC"
done

# Human-readable dates
for prop in \
  ro.build.date \
  ro.product.build.date \
  ro.system.build.date \
  ro.system_ext.build.date \
  ro.vendor.build.date \
  ro.odm.build.date \
  ro.odm_dlkm.build.date \
  ro.vendor_dlkm.build.date
do
  rp "$prop" "$BUILD_DATE_STR"
done

# Security patch
rp ro.build.version.security_patch "$SECURITY_PATCH"
rp ro.vendor.build.security_patch "$SECURITY_PATCH"

# Git build info
rp ro.git.buildtime "$GIT_BUILDTIME"

# Google client ID
rp ro.com.google.clientidbase "android-$BRAND_LOWER"

# CPU
rp ro.hardware "$CHIPSET"
rp ro.soc.model "$CHIPSET"
rp ro.vendor.soc.model "$CHIPSET"
rp ro.vendor.soc.model.external_name "$CHIPSET"
rp ro.vendor.soc.model.part_name "$CHIPSET"

# Serial
rp ro.boot.serialno "$SERIAL"
rp ro.serialno "$SERIAL"

# Device identity
rp ro.sys.hxy_bt "$MARKETING"
rp ro.wifi.hotspot "$MARKETING"

rp ro.product.nickname "$MARKETING"
rp ro.product.board "$BOARD"
rp ro.product.brand "$BRAND"
rp ro.product.model "$MODEL"
rp ro.product.device "$DEVICE"
rp ro.product.name "$DEVICE"
rp ro.product.manufacturer "$MANUFACTURER"

# System partition
rp ro.product.system.brand "$BRAND"
rp ro.product.system.model "$MODEL"
rp ro.product.system.device "$DEVICE"
rp ro.product.system.name "$DEVICE"
rp ro.product.system.manufacturer "$MANUFACTURER"

# Vendor partition
rp ro.product.vendor.brand "$BRAND"
rp ro.product.vendor.model "$MODEL"
rp ro.product.vendor.device "$DEVICE"
rp ro.product.vendor.name "$DEVICE"
rp ro.product.vendor.manufacturer "$MANUFACTURER"

# Product partition
rp ro.product.product.brand "$BRAND"
rp ro.product.product.model "$MODEL"
rp ro.product.product.device "$DEVICE"
rp ro.product.product.name "$DEVICE"
rp ro.product.product.manufacturer "$MANUFACTURER"

# System_ext partition
rp ro.product.system_ext.brand "$BRAND"
rp ro.product.system_ext.model "$MODEL"
rp ro.product.system_ext.device "$DEVICE"
rp ro.product.system_ext.name "$DEVICE"
rp ro.product.system_ext.manufacturer "$MANUFACTURER"

# ODM partition
rp ro.product.odm.brand "$BRAND"
rp ro.product.odm.model "$MODEL"
rp ro.product.odm.device "$DEVICE"
rp ro.product.odm.name "$DEVICE"
rp ro.product.odm.manufacturer "$MANUFACTURER"

# ODM DLKM partition
rp ro.product.odm_dlkm.brand "$BRAND"
rp ro.product.odm_dlkm.model "$MODEL"
rp ro.product.odm_dlkm.device "$DEVICE"
rp ro.product.odm_dlkm.name "$DEVICE"
rp ro.product.odm_dlkm.manufacturer "$MANUFACTURER"

# Vendor DLKM partition
rp ro.product.vendor_dlkm.brand "$BRAND"
rp ro.product.vendor_dlkm.model "$MODEL"
rp ro.product.vendor_dlkm.device "$DEVICE"
rp ro.product.vendor_dlkm.name "$DEVICE"
rp ro.product.vendor_dlkm.manufacturer "$MANUFACTURER"

# USB gadget can be unavailable during post-fs-data.
(
  echo "$MANUFACTURER" >/config/usb_gadget/g1/strings/0x409/manufacturer 2>/dev/null || true
  echo "$MARKETING" >/config/usb_gadget/g1/strings/0x409/product 2>/dev/null || true
  echo "$SERIAL" >/config/usb_gadget/g1/strings/0x409/serialnumber 2>/dev/null || true
) &

log "props applied successfully"
exit 0
