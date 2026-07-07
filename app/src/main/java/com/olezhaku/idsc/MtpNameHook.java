package com.olezhaku.idsc;

import java.io.FileDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public final class MtpNameHook implements IXposedHookLoadPackage {
  private static final String TAG = "MtpNameHook";

  private static final String ANDROID_PACKAGE = "android";
  private static final String SYSTEM_PACKAGE = "system";
  private static final String TARGET_PACKAGE = "com.android.mtp";

  private static final String NICKNAME_PROP = "ro.product.nickname";

  private static boolean mtpHooked = false;

  @Override
  public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
    if (!TARGET_PACKAGE.equals(lpparam.packageName)
        && !ANDROID_PACKAGE.equals(lpparam.packageName)
        && !SYSTEM_PACKAGE.equals(lpparam.packageName)) {
      return;
    }

    XposedBridge.log(TAG + ": loaded in "
        + lpparam.packageName + "/" + lpparam.processName);

    hookMtpServer(lpparam);
  }

  private static synchronized void hookMtpServer(final XC_LoadPackage.LoadPackageParam lpparam) {
    if (mtpHooked) {
      XposedBridge.log(TAG + ": MtpServer already hooked in "
          + lpparam.packageName + "/" + lpparam.processName);
      return;
    }

    try {
      Class<?> mtpDatabaseClass = XposedHelpers.findClass(
          "android.mtp.MtpDatabase",
          null);

      Class<?> mtpServerClass = XposedHelpers.findClass(
          "android.mtp.MtpServer",
          null);

      for (Constructor<?> constructor : mtpServerClass.getDeclaredConstructors()) {
        XposedBridge.log(TAG + ": MtpServer constructor: " + constructor);
      }

      Constructor<?> constructor = mtpServerClass.getDeclaredConstructor(
          mtpDatabaseClass,
          FileDescriptor.class,
          boolean.class,
          Runnable.class,
          String.class,
          String.class,
          String.class);

      constructor.setAccessible(true);

      XposedBridge.hookMethod(
          constructor,
          new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
              String oldModel = (String) param.args[5];
              String newModel = getSystemProperty(NICKNAME_PROP, oldModel);

              if (newModel != null
                  && !newModel.isEmpty()
                  && !newModel.equals(oldModel)) {
                param.args[5] = newModel;

                XposedBridge.log(TAG + ": MtpServer model changed: "
                    + oldModel + " -> " + newModel
                    + " in " + lpparam.packageName + "/" + lpparam.processName);
              } else {
                XposedBridge.log(TAG + ": MtpServer model unchanged: old="
                    + oldModel + " new=" + newModel
                    + " in " + lpparam.packageName + "/" + lpparam.processName);
              }
            }
          });

      mtpHooked = true;

      XposedBridge.log(TAG + ": hooked android.mtp.MtpServer in "
          + lpparam.packageName + "/" + lpparam.processName);

    } catch (Throwable t) {
      XposedBridge.log(TAG + ": MtpServer hook failed in "
          + lpparam.packageName + "/" + lpparam.processName + ": " + t);
    }
  }

  private static String getSystemProperty(String key, String fallback) {
    try {
      Class<?> systemPropertiesClass = Class.forName("android.os.SystemProperties");
      Method getMethod = systemPropertiesClass.getDeclaredMethod(
          "get",
          String.class,
          String.class);

      return (String) getMethod.invoke(null, key, fallback != null ? fallback : "");
    } catch (Throwable ignored) {
      return fallback;
    }
  }
}
