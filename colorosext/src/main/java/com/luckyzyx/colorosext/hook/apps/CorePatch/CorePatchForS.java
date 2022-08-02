package com.luckyzyx.colorosext.hook.apps.CorePatch;

import androidx.annotation.NonNull;

import java.lang.reflect.InvocationTargetException;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class CorePatchForS extends CorePatchForR {
    @Override
    public void handleLoadPackage(@NonNull XC_LoadPackage.LoadPackageParam loadPackageParam) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        super.handleLoadPackage(loadPackageParam);
        if (prefs.getBoolean("digestCreak", true) && prefs.getBoolean("UsePreSig", false)) {
            Class<?> pmClass = findClass("com.android.server.pm.PackageManagerService", loadPackageParam.classLoader);
            Class<?> pPClass = findClass("com.android.server.pm.parsing.pkg.ParsedPackage", loadPackageParam.classLoader);
            XposedHelpers.findAndHookMethod(pmClass, "doesSignatureMatchForPermissions", String.class, pPClass, int.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) {
                    // If we decide to crack this then at least make sure they are same apks, avoid another one that tries to impersonate.
                    // 如果我们决定破解这个，那么至少要确保它们是相同的apk，避免另一个试图冒充的apk。
                    if (param.getResult().equals(false)) {
                        String pPname = (String) XposedHelpers.callMethod(param.args[1], "getPackageName");
                        if (pPname.contentEquals((String) param.args[0])) {
                            param.setResult(true);
                        }
                    }
                }
            });
        }
    }
}
