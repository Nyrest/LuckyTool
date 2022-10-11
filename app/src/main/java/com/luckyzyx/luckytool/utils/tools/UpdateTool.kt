package com.luckyzyx.luckytool.utils.tools

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.content.FileProvider
import androidx.core.widget.NestedScrollView
import com.drake.net.Get
import com.drake.net.utils.scopeNet
import com.drake.net.utils.withMain
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import com.luckyzyx.luckytool.R
import kotlinx.coroutines.Dispatchers
import org.json.JSONObject
import java.io.File

object UpdateTool {
    fun checkUpdate(context: Context, versionName: String, versionCode: Int, result: (String, Int, () -> Unit) -> Unit) {
        scopeNet(Dispatchers.IO) {
            val latestUrl = "https://api.github.com/repos/Xposed-Modules-Repo/com.luckyzyx.luckytool/releases/latest"
            val getJson = Get<String>(latestUrl).await()
            JSONObject(getJson).apply {
                val name = optString("name")
                val code = optString("tag_name").split("-")[0]
                val changeLog = optString("body")
                val fileName = getJSONArray("assets").getJSONObject(0).optString("name")
                val downloadUrl = getJSONArray("assets").getJSONObject(0).optString("browser_download_url")
                val downloadPage = optString("html_url")
                val updateTime = optString("published_at").replace("T", " ").replace("Z", "")
                if ((versionCode >= code.toInt()) || (versionName == name)) return@scopeNet
                withMain {
                    result(name, code.toInt()) {
                        MaterialAlertDialogBuilder(context)
                            .setTitle(context.getString(R.string.check_update_hint))
                            .setView(
                                NestedScrollView(context).apply {
                                    addView(
                                        MaterialTextView(context).apply {
                                            setPadding(20.dp, 0, 20.dp, 0)
                                            text = "${context.getString(R.string.version_name)}: $name($code)\n${context.getString(R.string.update_time)}: $updateTime\n${context.getString(R.string.update_content)}: \n$changeLog"
                                        }
                                    )
                                }
                            )
                            .setPositiveButton(context.getString(R.string.direct_update)) { _, _ ->
                                downloadFile(context, fileName, downloadUrl)
                            }
                            .setNeutralButton(context.getString(R.string.go_download_page)) { _, _ ->
                                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(downloadPage)))
                            }
                            .show()
                    }
                }
            }
        }
    }

    //https://dl.coolapk.com/down?pn=com.coolapk.market&id=NDU5OQ&h=46bb9d98&from=from-web
    private fun downloadFile(context: Context, apkName: String, url: String) {
        scopeNet(Dispatchers.IO) {
            File("sdcard/Download/$apkName").apply {
                if (this.exists()) {
                    installApk(context, this)
                    return@scopeNet
                }
            }
            val apkFile = Get<File>(url) {
                setDownloadFileName(apkName)
                setDownloadDir("sdcard/Download/")
                setDownloadMd5Verify()
                setDownloadTempFile()
            }.await()
            installApk(context, apkFile)
        }
    }

    private fun installApk(context: Context, apkFile: File) {
        if (context.packageManager.canRequestPackageInstalls()) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.FileProvider", apkFile)
            intent.setDataAndType(uri, "application/vnd.android.package-archive")
            context.startActivity(intent)
        } else {
            context.toast(context.getString(R.string.install_apk_toast))
            val intent = Intent(
                Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                Uri.parse("package:${context.packageName}")
            )
            context.startActivity(intent)
        }
    }
}
