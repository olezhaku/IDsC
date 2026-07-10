package com.olezhaku.idsc;

import java.io.FileDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MtpNameHook implements IXposedHookLoadPackage {
  private static final String TARGET_PACKAGE = "com.android.mtp";
  private static final String NICKNAME_PROP = "ro.product.nickname";

  @Override
  public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
    if (!TARGET_PACKAGE.equals(lpparam.packageName)) {
      return;
    }

    hookMtpServer();
  }

  private static synchronized void hookMtpServer() {
    try {
      Class<?> mtpServerClass = XposedHelpers.findClass("android.mtp.MtpServer", null);
      Class<?> mtpDatabaseClass = XposedHelpers.findClass("android.mtp.MtpDatabase", null);

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

              if (newModel != null && !newModel.isEmpty()) {
                param.args[5] = newModel;
              }
            }
          });
    } catch (Throwable t) {
      XposedBridge.log("MtpNameHook: MtpServer hook failed: " + t);
    }
  }

  private static String getSystemProperty(String key, String fallback) {
    try {
      Class<?> systemPropertiesClass = Class.forName("android.os.SystemProperties");
      Method getMethod = systemPropertiesClass.getDeclaredMethod(
          "get",
          String.class,
          String.class);

      return (String) getMethod.invoke(null, key, fallback);
    } catch (Throwable ignored) {
      return fallback;
    }
  }
}
