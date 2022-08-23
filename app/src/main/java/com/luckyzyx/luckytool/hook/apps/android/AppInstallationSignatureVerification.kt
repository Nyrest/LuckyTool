package com.luckyzyx.luckytool.hook.apps.android

import android.os.Build.VERSION.SDK_INT
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerE
import com.luckyzyx.luckytool.utils.XposedPrefs

class AppInstallationSignatureVerification : YukiBaseHooker() {
    override fun onHook() {
        if (SDK_INT >= 33) {
            loadHooker(AppInstallationSignatureVerificationV13())
            return
        }
        findClass("com.android.server.pm.PackageManagerService").hook {
            injectMember {
                method {
                    name = "doesSignatureMatchForPermissions"
                    paramCount = 3
                }
                beforeHook {
                    prefs.clearCache()
                    if (prefs(XposedPrefs).getBoolean("disable_apk_signature_verification",false)) resultTrue()
                }
            }.onNoSuchMemberFailure {
                loggerE(msg = "NoSuchMember -> doesSignatureMatchForPermissions")
            }
        }.onHookClassNotFoundFailure {
            loggerE(msg = "ClassNotFound -> PackageManagerService")
        }
    }
}
class AppInstallationSignatureVerificationV13 : YukiBaseHooker() {
    override fun onHook() {
        findClass("com.android.server.pm.InstallPackageHelper").hook {
            injectMember {
                method {
                    name = "doesSignatureMatchForPermissions"
                    paramCount = 3
                }
                beforeHook {
                    prefs.clearCache()
                    if (prefs(XposedPrefs).getBoolean("disable_apk_signature_verification",false)) resultTrue()
                }
            }.onNoSuchMemberFailure {
                loggerE(msg = "NoSuchMember -> doesSignatureMatchForPermissions")
            }
        }.onHookClassNotFoundFailure {
            loggerE(msg = "ClassNotFound -> PackageManagerService")
        }
    }
}