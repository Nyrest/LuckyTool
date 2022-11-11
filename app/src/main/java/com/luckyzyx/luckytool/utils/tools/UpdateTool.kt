package com.luckyzyx.luckytool.utils.tools

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.SeekBar
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import com.drake.net.Get
import com.drake.net.component.Progress
import com.drake.net.interfaces.ProgressListener
import com.drake.net.scope.NetCoroutineScope
import com.drake.net.utils.scopeNet
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import com.luckyzyx.luckytool.R
import org.json.JSONObject
import java.io.File

object UpdateTool {
    fun checkUpdate(context: Context, @Suppress("UNUSED_PARAMETER") versionName: String, versionCode: Int, result: (String, Int, () -> Unit) -> Unit) {
        scopeNet {
            val latestUrl = "https://api.github.com/repos/Xposed-Modules-Repo/com.luckyzyx.luckytool/releases/latest"
            val getJson = Get<String>(latestUrl).await()
            JSONObject(getJson).apply {
                val name = optString("name")
                val code = optString("tag_name").split("-")[0]
                val changeLog = optString("body")
                val fileName = getJSONArray("assets").getJSONObject(0).optString("name")
                val downloadUrl = getJSONArray("assets").getJSONObject(0).optString("browser_download_url")
                val downloadPage = optString("html_url")
                val downloadCount = getJSONArray("assets").getJSONObject(0).optString("download_count")
//                val updateTime = optString("published_at").replace("T", " ").replace("Z", "")
                //版本号大于等于云端,提示最新版本
                if (versionCode >= code.toInt()) {
//                    context.toast("已是最新版本v${versionName}!")
                    return@scopeNet
                }
                result(name, code.toInt()) {
                    MaterialAlertDialogBuilder(context)
                        .setTitle(context.getString(R.string.check_update_hint))
                        .setView(
                            NestedScrollView(context).apply {
                                addView(
                                    MaterialTextView(context).apply {
                                        setPadding(20.dp, 0, 20.dp, 0)
//                                        text = "${context.getString(R.string.version_name)}: $name($code)\n${context.getString(R.string.update_time)}: $updateTime\n${context.getString(R.string.update_content)}: \n$changeLog"
                                        text = """
                                            ${context.getString(R.string.version_name)}: $name($code)
                                            ${context.getString(R.string.download_count)}: $downloadCount
                                            ${context.getString(R.string.update_content)}: 
                                            $changeLog
                                        """.trimIndent()
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

    const val coolmarketUrl = "https://dl.coolapk.com/down?pn=com.coolapk.market&id=NDU5OQ&h=46bb9d98&from=from-web"
    fun downloadFile(context: Context, apkName: String, url: String) {
        var downloadScope: NetCoroutineScope = scopeNet { }
        val downloadDialog = MaterialAlertDialogBuilder(context).apply {
            setTitle(context.getString(R.string.downloading))
            setCancelable(false)
            setView(R.layout.layout_download_dialog)
        }.show()
        downloadScope = scopeNet {
            File("/sdcard/Download/$apkName").apply {
                if (this.exists()) {
                    installApk(context, this)
                    downloadDialog.dismiss()
                    return@scopeNet
                }
            }
            downloadDialog.findViewById<MaterialButton>(R.id.cancel_button)?.apply {
                text = context.getString(R.string.cancel_button)
                setOnClickListener {
                    downloadScope.cancel()
                    downloadDialog.dismiss()
                }
            }
            val apkFile = Get<File>(url) {
                setDownloadFileName(apkName)
                setDownloadDir("/sdcard/Download/")
                setDownloadMd5Verify()
                setDownloadTempFile()
                addDownloadListener(object : ProgressListener(100){
                    override fun onProgress(p: Progress) {
                        val downSeek = downloadDialog.findViewById<SeekBar>(R.id.down_seek)
                        val downTv = downloadDialog.findViewById<MaterialTextView>(R.id.down_tv)
                        downSeek?.post {
                            val progress = p.progress()
                            downSeek.progress = progress
                            downTv?.text = """
                                ${context.getString(R.string.download_progress)}: $progress%
                                ${context.getString(R.string.download_speed)}: ${p.speedSize()}
                                ${context.getString(R.string.remain_size)}: ${p.remainSize()}
                                ${context.getString(R.string.downloaded)}: ${p.currentSize()} / ${p.totalSize()}
                                ${context.getString(R.string.used_time)}: ${p.useTime()}  ${context.getString(R.string.remain_time)}: ${p.remainTime()}
                            """.trimIndent()
                        }
                    }
                })
            }.await()
            downloadDialog.findViewById<MaterialButton>(R.id.install_button)?.apply {
                isVisible = true
                text = context.getString(R.string.install_button)
                setOnClickListener{
                    downloadDialog.setCancelable(true)
                    installApk(context, apkFile)
                }
            }
            installApk(context, apkFile)
            downloadDialog.dismiss()
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
