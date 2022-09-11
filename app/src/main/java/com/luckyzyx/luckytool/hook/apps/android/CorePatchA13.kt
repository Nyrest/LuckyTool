@file:Suppress("unused", "LocalVariableName", "DEPRECATION", "UNCHECKED_CAST", "UNUSED_VALUE")

package com.luckyzyx.luckytool.hook.apps.android

import android.app.AndroidAppHelper
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.highcapable.yukihookapi.hook.log.loggerE
import com.highcapable.yukihookapi.hook.type.java.*
import com.luckyzyx.luckytool.BuildConfig
import com.luckyzyx.luckytool.utils.tools.XposedPrefs
import java.security.cert.Certificate
import java.util.*
import java.util.zip.ZipEntry

class CorePatchA13 : YukiBaseHooker() {
    override fun onHook() {
        val SIGNATURE = "308203c6308202aea003020102021426d148b7c65944abcf3a683b4c3dd3b139c4ec85300d06092a864886f70d01010b05003074310b3009060355040613025553311330110603550408130a43616c69666f726e6961311630140603550407130d4d6f756e7461696e205669657731143012060355040a130b476f6f676c6520496e632e3110300e060355040b1307416e64726f69643110300e06035504031307416e64726f6964301e170d3139303130323138353233385a170d3439303130323138353233385a3074310b3009060355040613025553311330110603550408130a43616c69666f726e6961311630140603550407130d4d6f756e7461696e205669657731143012060355040a130b476f6f676c6520496e632e3110300e060355040b1307416e64726f69643110300e06035504031307416e64726f696430820122300d06092a864886f70d01010105000382010f003082010a028201010087fcde48d9beaeba37b733a397ae586fb42b6c3f4ce758dc3ef1327754a049b58f738664ece587994f1c6362f98c9be5fe82c72177260c390781f74a10a8a6f05a6b5ca0c7c5826e15526d8d7f0e74f2170064896b0cf32634a388e1a975ed6bab10744d9b371cba85069834bf098f1de0205cdee8e715759d302a64d248067a15b9beea11b61305e367ac71b1a898bf2eec7342109c9c5813a579d8a1b3e6a3fe290ea82e27fdba748a663f73cca5807cff1e4ad6f3ccca7c02945926a47279d1159599d4ecf01c9d0b62e385c6320a7a1e4ddc9833f237e814b34024b9ad108a5b00786ea15593a50ca7987cbbdc203c096eed5ff4bf8a63d27d33ecc963990203010001a350304e300c0603551d13040530030101ff301d0603551d0e04160414a361efb002034d596c3a60ad7b0332012a16aee3301f0603551d23041830168014a361efb002034d596c3a60ad7b0332012a16aee3300d06092a864886f70d01010b0500038201010022ccb684a7a8706f3ee7c81d6750fd662bf39f84805862040b625ddf378eeefae5a4f1f283deea61a3c7f8e7963fd745415153a531912b82b596e7409287ba26fb80cedba18f22ae3d987466e1fdd88e440402b2ea2819db5392cadee501350e81b8791675ea1a2ed7ef7696dff273f13fb742bb9625fa12ce9c2cb0b7b3d94b21792f1252b1d9e4f7012cb341b62ff556e6864b40927e942065d8f0f51273fcda979b8832dd5562c79acf719de6be5aee2a85f89265b071bf38339e2d31041bc501d5e0c034ab1cd9c64353b10ee70b49274093d13f733eb9d3543140814c72f8e003f301c7a00b1872cc008ad55e26df2e8f07441002c4bcb7dc746745f0db"

        // 允许降级
        findClass("com.android.server.pm.PackageManagerServiceUtils").hook {
            injectMember {
                method {
                    name = "checkDowngrade"
                }
                if (prefs(XposedPrefs).getBoolean("downgrade", true)) replaceTo(null)
            }
        }.ignoredHookClassNotFoundFailure()

        // apk内文件修改后 digest校验会失败
        findClass("android.util.jar.StrictJarVerifier").hook {
            injectMember {
                allMethods("verifyMessageDigest")
                if (prefs(XposedPrefs).getBoolean("authcreak", true)) replaceToTrue()
            }
        }.ignoredHookClassNotFoundFailure()
        findClass("android.util.jar.StrictJarVerifier").hook {
            injectMember {
                allMethods("verify")
                if (prefs(XposedPrefs).getBoolean("authcreak", true)) replaceToTrue()
            }
        }.ignoredHookClassNotFoundFailure()

        // Targeting R+ (version " + Build.VERSION_CODES.R + " and above) requires"
        // + " the resources.arsc of installed APKs to be stored uncompressed"
        // + " and aligned on a 4-byte boundary
        // target >=30 的情况下 resources.arsc 必须是未压缩的且4K对齐
        findClass("android.content.res.AssetManager").hook {
            injectMember {
                allMethods("containsAllocatedTable")
                if (prefs(XposedPrefs).getBoolean("authcreak", true)) replaceToFalse()
            }
        }.ignoredHookClassNotFoundFailure()

        findClass("android.util.apk.ApkSignatureVerifier").hook {
            injectMember {
                method {
                    name = "getMinimumSignatureSchemeVersionForTargetSdk"
                }
                if (prefs(XposedPrefs).getBoolean("authcreak", true)) replaceTo(1)
            }
        }.ignoredHookClassNotFoundFailure()

        // Package " + packageName + " signatures do not match previously installed version; ignoring!"
        // public boolean checkCapability(String sha256String, @CertCapabilities int flags) {
        // public boolean checkCapability(SigningDetails oldDetails, @CertCapabilities int flags)
        findClass("android.content.pm.PackageParser.SigningDetails").hook {
            injectMember {
                allMethods("checkCapability")
                beforeHook {
                    // Don't handle PERMISSION (grant SIGNATURE permissions to pkgs with this cert)
                    // Or applications will have all privileged permissions
                    // https://cs.android.com/android/platform/superproject/+/master:frameworks/base/core/java/android/content/pm/PackageParser.java;l=5947?q=CertCapabilities
                    if (prefs(XposedPrefs).getBoolean("authcreak", true)) {
                        if (args(1).cast<Int>() != 4) {
                            result = true
                        }
                    }
                }
            }
        }.ignoredHookClassNotFoundFailure()

        // 当verifyV1Signature抛出转换异常时，替换一个签名作为返回值
        // 如果用户已安装apk，并且其定义了私有权限，则安装时会因签名与模块内硬编码的不一致而被拒绝。尝试从待安装apk中获取签名。如果其中apk的签名和已安装的一致（只动了内容）就没有问题。此策略可能有潜在的安全隐患。
        val pkc = ("sun.security.pkcs.PKCS7").clazz
        val constructor = //XposedHelpers.findConstructorExact(pkc, ByteArray::class.java)
            pkc.getConstructor(ByteArrayClass)
        constructor.isAccessible = true


        val ASV = ("android.util.apk.ApkSignatureVerifier").clazz
        val sJarClass = ("android.util.jar.StrictJarFile").clazz
//        val constructorExact = XposedHelpers.findConstructorExact(
//            sJarClass,
//            String::class.java,
//            Boolean::class.javaPrimitiveType,
//            Boolean::class.javaPrimitiveType
//        )
        val constructorExact = sJarClass.getConstructor(
            String::class.java,
            Boolean::class.javaPrimitiveType,
            Boolean::class.javaPrimitiveType)
        constructorExact.isAccessible = true


        val signingDetails = ("android.content.pm.PackageParser.SigningDetails").clazz
//        val findConstructorExact = XposedHelpers.findConstructorExact(
//            signingDetails,
//            Array<Signature>::class.java, Integer.TYPE
//        )
        val findConstructorExact = signingDetails.getConstructor(Array<Signature>::class.java, IntType)
        findConstructorExact.isAccessible = true

        val packageParserException = ("android.content.pm.PackageParser.PackageParserException").clazz
//        val error = XposedHelpers.findField(packageParserException, "error")
        val error = packageParserException.getField("error")
        error.isAccessible = true

        val signingDetailsArgs = arrayOfNulls<Any>(2)
        signingDetailsArgs[1] = 1

        findClass("android.util.jar.StrictJarVerifier").hook {
            injectMember {
                allMethods("verifyBytes")
                afterHook {
                    if (prefs.getBoolean("digestCreak", true)) {
                        if (!prefs.getBoolean("UsePreSig", false)) {
                            val block = constructor.newInstance(args[0])
//                            val infos = XposedHelpers.callMethod(args[1], "verify") as Array<*>
                            var info: Any? = null
                            args(1).current {
                                val infos = method {
                                    name = "verify"
                                }.call() as Array<*>
                                info = infos[0]
                            }
//                            val verifiedSignerCertChain = XposedHelpers.callMethod(info, "getCertificateChain", block) as List<X509Certificate>
                            info?.current {
                                val verifiedSignerCertChain = method {
                                    name = "getCertificateChain"
                                }.call(block) as List<*>
                                result = verifiedSignerCertChain.toTypedArray()
                            }
                        }
                    }
                }
            }
        }.ignoredHookClassNotFoundFailure()

        findClass("android.util.apk.ApkSignatureVerifier").hook {
            injectMember {
                allMethods("verifySignaturesInternal")
                afterHook {
                    if (prefs.getBoolean("authcreak", true)){
                        var lastSigs: Array<Signature?>? = null
                        if (prefs.getBoolean("UsePreSig", false)) {
                            val PM = AndroidAppHelper.currentApplication().packageManager
                            if (PM == null) {
                                loggerE(msg = BuildConfig.APPLICATION_ID + " Cannot get the PackageManager... Are you using MiUI?")
                            } else {
                                val pI = PM.getPackageArchiveInfo(args[1] as String, 0)
                                val InstpI = PM.getPackageInfo(pI!!.packageName, PackageManager.GET_SIGNATURES)
                                lastSigs = InstpI.signatures
                            }
                        } else {
                            if (prefs.getBoolean("digestCreak", true)) {
                                val origJarFile = constructorExact.newInstance(args[0], true, false)
//                                val manifestEntry = XposedHelpers.callMethod(origJarFile, "findEntry", "AndroidManifest.xml") as ZipEntry
                                origJarFile.current {
                                    val manifestEntry = method {
                                        name = "findEntry"
                                    }.call("AndroidManifest.xml") as ZipEntry
//                                    val lastCerts = XposedHelpers.callStaticMethod(ASV, "loadCertificates", origJarFile, manifestEntry) as Array<Array<Certificate>>
                                    ASV.current {
                                        val lastCerts = method {
                                            name = "loadCertificates"
                                        }.call(origJarFile,manifestEntry) as Array<Array<Certificate>>
//                                        lastSigs = XposedHelpers.callStaticMethod(ASV, "convertToSignatures", lastCerts as Any) as Array<Signature?>
                                        ASV.current {
                                            lastSigs = method {
                                                name = "convertToSignatures"
                                            }.call(lastCerts) as Array<Signature?>
                                        }
                                    }
                                }
                            }
                        }

                        if (lastSigs != null) {
                            signingDetailsArgs[0] = lastSigs
                        } else {
                            signingDetailsArgs[0] = arrayOf(Signature(SIGNATURE))
                        }
                        var newInstance = findConstructorExact.newInstance(*signingDetailsArgs)

                        //修复 java.lang.ClassCastException: Cannot cast android.content.pm.PackageParser$SigningDetails to android.util.apk.ApkSignatureVerifier$SigningDetailsWithDigests
                        val signingDetailsWithDigests = ("android.util.apk.ApkSignatureVerifier.SigningDetailsWithDigests").clazz
//                        val signingDetailsWithDigestsConstructorExact = XposedHelpers.findConstructorExact(signingDetailsWithDigests, signingDetails, MutableMap::class.java)
                        val signingDetailsWithDigestsConstructorExact = signingDetailsWithDigests.getConstructor(signingDetails, MapClass)
                        signingDetailsWithDigestsConstructorExact.isAccessible = true
                        newInstance = signingDetailsWithDigestsConstructorExact.newInstance(newInstance, null)

                        val PR = ("android.content.pm.parsing.result.ParseResult").clazz
                        PR.current {
                            method {
                                name = "getResult"
                            }.call()
                        }
//                        result = newInstance

                    }
                }
            }
        }.ignoredHookClassNotFoundFailure()

        if (prefs(XposedPrefs).getBoolean("digestCreak", true) && prefs(XposedPrefs).getBoolean("UsePreSig", false)) {
            findClass("com.android.server.pm.InstallPackageHelper").hook {
                injectMember {
                    method {
                        name = "doesSignatureMatchForPermissions"
                    }
                    afterHook {
                        //If we decide to crack this then at least make sure they are same apks, avoid another one that tries to impersonate.
                        //如果我们决定破解它，那么至少要确保它们是相同的 apk，避免另一个试图冒充的 apk。
                        if (result == false) {
                            args(1).current {
                                val pPname = method {
                                    name = "getPackageName"
                                }.call() as String
                                if (pPname.contentEquals(args(0).cast<String>())) {
                                    result = true
                                }
                            }
                        }
                    }
                }
            }.ignoredHookClassNotFoundFailure()
        }

        // if app is system app, allow to use hidden api, even if app not using a system signature
        // 如果应用是系统应用，允许使用隐藏的 api，即使应用不使用系统签名
        findClass("android.content.pm.ApplicationInfo").hook {
            injectMember {
                method {
                    name = "isPackageWhitelistedForHiddenApis"
                }
                beforeHook {
                    if (prefs(XposedPrefs).getBoolean("digestCreak", true)) {
                        val info = instance as ApplicationInfo
                        if (info.flags and ApplicationInfo.FLAG_SYSTEM != 0 || info.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP != 0) {
                            result = true
                        }
                    }
                }
            }
        }.ignoredHookClassNotFoundFailure()
    }
}
