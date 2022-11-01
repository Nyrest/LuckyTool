package com.luckyzyx.luckytool.ui.activity

import android.os.Bundle
import android.util.ArrayMap
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.drake.net.Post
import com.drake.net.utils.scopeNet
import com.luckyzyx.luckytool.databinding.ActivityOtaBinding
import com.luckyzyx.luckytool.utils.tools.MoshiUtil
import com.luckyzyx.luckytool.utils.tools.getGuid
import okhttp3.Response
import okio.ByteString.Companion.decodeHex
import org.json.JSONObject

@Suppress("MemberVisibilityCanBePrivate", "LocalVariableName")
class OTAActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtaBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        initPy()
        initOTA()
    }

    var region = 1
    var ruiVersion = 4
    val model = "MT2110_CH"
    val otaVersion = "MT2110_11.F.06_2060_202210081720"
    val nvId = "10010111"
    var guid = getGuid

    val areaList = arrayOf("GL", "CN", "IN", "EU")
    val ruiList = arrayOf("V1", "V2", "V3", "V4")

    val defaultHeaders = ArrayMap<String,Any>().apply {
        put("language", "en-EN")
        put("romVersion", "unknown")
        put("otaVersion", "unknown")
        put("androidVersion", "unknown")
        put("colorOSVersion", "unknown")
        put("model", "unknown")
        put("infVersion", "1")
        put("operator", "unknown")
        put("nvCarrier", "unknown")
        put("uRegion", "unknown")
        put("trackRegion", "unknown")
        put("imei", "000000000000000")
        put("deviceId", "0")
        put("mode", "0")
        put("Accept", "application/json")
        put("Content-Type", "application/json")
        put("User-Agent", "NULL")
    }
    val defaultBody = ArrayMap<String, Any>().apply {
        put("language", "en-EN")
        put("romVersion", "unknown")
        put("otaVersion", "unknown")
        put("androidVersion", "unknown")
        put("colorOSVersion", "unknown")
        put("model", "unknown")
        put("productName", "unknown")
        put("operator", "unknown")
        put("uRegion", "unknown")
        put("trackRegion", "unknown")
        put("imei", "000000000000000")
        put("mode", "0")
        put("registrationId", "unknown")
        put("deviceId", "0")
        put("version", "3")
        put("type", "1")
        put("otaPrefix", "unknown")
        put("isRealme", "unknown")
        put("time", "0")
        put("canCheckSelf", "0")
    }
    val urls = ArrayList<ArrayList<String>>().apply {
        add(0, arrayListOf())
        add(
            1, arrayListOf(
                "https://ifota.realmemobile.com/post/Query_Update",    // GL
                "https://iota.coloros.com/post/Query_Update",          // CN
                "https://ifota-in.realmemobile.com/post/Query_Update", // IN
                "https://ifota-eu.realmemobile.com/post/Query_Update"  // EU
            )
        )
        add(
            2, arrayListOf(
                "https://component-ota-f.coloros.com/update/v1",       // GL
                "https://component-ota.coloros.com/update/v1",         // CN
                "https://component-ota-in.coloros.com/update/v1",      // IN
                "https://component-ota-eu.coloros.com/update/v1"       // EU
            )
        )
        add(
            3, arrayListOf(
                "https://component-ota-f.coloros.com/update/v2",       // GL
                "https://component-ota.coloros.com/update/v2",         // CN
                "https://component-ota-in.coloros.com/update/v2",      // IN
                "https://component-ota-eu.coloros.com/update/v2"       // EU
            )
        )
        add(
            4, arrayListOf(
                "https://component-ota-f.coloros.com/update/v2",       // GL
                "https://component-ota.coloros.com/update/v2",         // CN
                "https://component-ota-in.coloros.com/update/v2",      // IN
                "https://component-ota-eu.coloros.com/update/v2"       // EU
            )
        )
    }
    val keys = arrayListOf("oppo1997", "baed2017", "java7865", "231uiedn", "09e32ji6",
        "0oiu3jdy", "0pej387l", "2dkliuyt", "20odiuye", "87j3id7w")

    var body: ArrayMap<String, Any>? = null
    var header: ArrayMap<String, Any>? = null
    var respKey: String = ""

    var ota: PyObject? = null

    private fun initPy(){
        if (!Python.isStarted()) Python.start(AndroidPlatform(this))
        val py = Python.getInstance()
        ota = py.getModule("realme_ota/main")
    }

    private fun initOTA() {
        binding.otaArea.apply {
            adapter = ArrayAdapter(this.context, android.R.layout.simple_list_item_1, areaList)
            setSelection(region)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    region = position
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }
        binding.ruiVersion.apply {
            adapter = ArrayAdapter(this.context, android.R.layout.simple_list_item_1, ruiList)
            setSelection(ruiVersion - 1)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    ruiVersion = position + 1
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }
        binding.isGuid.apply {
            setOnCheckedChangeListener { buttonView, isChecked ->
                if (buttonView.isPressed) {
                    binding.editGuidView.isVisible = isChecked
                    guid = if (isChecked) getGuid else "0"
                    binding.editGuid.setText(guid)
                }
            }
        }
        binding.editGuidView.apply {
            hint = "GUID"
            isHintEnabled = true
            isHintAnimationEnabled = true
        }
        binding.modelView.apply {
            hint = "ro.product.name"
            isHintEnabled = true
            isHintAnimationEnabled = true
        }
        binding.model.setText(model)
        binding.otaVersionView.apply {
            hint = "ro.build.version.ota"
            isHintEnabled = true
            isHintAnimationEnabled = true
        }
        binding.otaVersion.setText(otaVersion)
        binding.nvIdView.apply {
            hint = "ro.build.oplus_nv_id"
            isHintEnabled = true
            isHintAnimationEnabled = true
        }
        binding.nvId.setText(nvId)
        binding.otaBtn.apply {
            text = "获取版本信息"
            setOnClickListener {
                initLoad()
            }
        }
    }

    fun initLoad(){
        guid = binding.editGuid.text.toString()
        binding.otaTv.text = null


        addText(ota?.callAttr("main").toString())

        return
        addText("装载 $model -> OTA_V$ruiVersion")
        setBodys()
        setHeaders()
        addText("设置请求变量")
        postOTA(urls[ruiVersion][region], header!!, body!!)
    }

    fun setBodys(){
        val arrays = defaultBody
        val keys = arrays.keys
        for (key in keys){
            //定制参数
            if (key == "language"){
                if (region == 1) arrays["language"] = "zh-CN"
            }
            if (key == "trackRegion"){
                arrays["trackRegion"] = when(region){
                    1 -> "CN"
                    2 -> "IN"
                    else -> "GL"
                }
            }
            if (key == "uRegion"){
                arrays["uRegion"] = when(region){
                    1 -> "CN"
                    2 -> "IN"
                    else -> "GL"
                }
            }
            if (key == "androidVersion"){
                arrays["androidVersion"] = "Android"+when(ruiVersion){
                    1 -> "10.0"
                    2 -> "11.0"
                    else -> "12.0"
                }
            }
            if (key == "colorOSVersion"){
                arrays["colorOSVersion"] = "ColorOS"+when(ruiVersion){
                    1 -> "7"
                    2 -> "11"
                    else -> "12"
                }
            }
            if (key == "nvCarrier"){
                arrays["nvCarrier"] = if (nvId != "0") nvId else {
                    if (region == 1) "10010111" else "00011011"
                }
            }
            if (key == "partCarrier"){
                arrays["partCarrier"] = if (nvId != "0") nvId else {
                    if (region == 1) "10010111" else "00011011"
                }
            }
            if (key == "localCarrier"){
                arrays["localCarrier"] = if (nvId != "0") nvId else {
                    if (region == 1) "10010111" else "00011011"
                }
            }
            if (key == "isRealme"){
                arrays["isRealme"] = if ("RMX" in model) "1" else "0"
            }
            if (key == "romPrefix"){
                arrays["romPrefix"] = otaVersion.split("_")[0]+"_"+otaVersion.split("_")[1]
            }
            if (key == "romVersion"){
                arrays["romVersion"] = otaVersion.split("_")[0]+"_"+otaVersion.split("_")[1]
            }
            if (key == "otaPrefix"){
                arrays["otaPrefix"] = otaVersion.split("_")[0]+"_"+otaVersion.split("_")[1]
            }
            if (key == "resp_key"){
                arrays["resp_key"] = if (ruiVersion == 1) "resps" else "body"
                respKey = if (ruiVersion == 1) "resps" else "body"
            }

            //默认参数
            if (key == "otaVersion") arrays["otaVersion"] = otaVersion
            if (key == "model") arrays["model"] = model
            if (key == "productName") arrays["productName"] = model
            if (key == "operator") arrays["operator"] = model
        }
//        body = ArrayMap<String,Any>().apply {
//            put("params",encrypt(defaultBody.toString()))
//        }
//        addText(body.toString())
    }

    fun setHeaders(){
        val arrays = defaultHeaders
        val keys = arrays.keys
        for (key in keys){
            //定制参数
            if (key == "language"){
                if (region == 1) arrays["language"] = "zh-CN"
            }
            if (key == "trackRegion"){
                arrays["trackRegion"] = when(region){
                    1 -> "CN"
                    2 -> "IN"
                    else -> "GL"
                }
            }
            if (key == "uRegion"){
                arrays["uRegion"] = when(region){
                    1 -> "CN"
                    2 -> "IN"
                    else -> "GL"
                }
            }
            if (key == "androidVersion"){
                arrays["androidVersion"] = "Android"+when(ruiVersion){
                    1 -> "10.0"
                    2 -> "11.0"
                    else -> "12.0"
                }
            }
            if (key == "colorOSVersion"){
                arrays["colorOSVersion"] = "ColorOS"+when(ruiVersion){
                    1 -> "7"
                    2 -> "11"
                    else -> "12"
                }
            }
            if (key == "nvCarrier"){
                arrays["nvCarrier"] = if (nvId != "0") nvId else {
                    if (region == 1) "10010111" else "00011011"
                }
            }
            if (key == "partCarrier"){
                arrays["partCarrier"] = if (nvId != "0") nvId else {
                    if (region == 1) "10010111" else "00011011"
                }
            }
            if (key == "localCarrier"){
                arrays["localCarrier"] = if (nvId != "0") nvId else {
                    if (region == 1) "10010111" else "00011011"
                }
            }
            if (key == "isRealme"){
                arrays["isRealme"] = if ("RMX" in model) "1" else "0"
            }
            if (key == "romPrefix"){
                arrays["romPrefix"] = otaVersion.split("_")[0]+"_"+otaVersion.split("_")[1]
            }
            if (key == "romVersion"){
                arrays["romVersion"] = otaVersion.split("_")[0]+"_"+otaVersion.split("_")[1]
            }
            if (key == "otaPrefix"){
                arrays["otaPrefix"] = otaVersion.split("_")[0]+"_"+otaVersion.split("_")[1]
            }
            if (key == "resp_key"){
                arrays["resp_key"] = if (ruiVersion == 1) "resps" else "body"
            }
            if (key == "deviceId"){
//                arrays["deviceId"] = if (binding.isGuid.isChecked) sha256(guid) else "0"
            }

            //默认参数
            if (key == "otaVersion") arrays["otaVersion"] = otaVersion
            if (key == "model") arrays["model"] = model
            if (key == "productName") arrays["productName"] = model
            if (key == "operator") arrays["operator"] = model
        }
        header = defaultHeaders
    }

    private fun postOTA(url: String, headers: ArrayMap<String, Any>,body: ArrayMap<String,Any>){
        scopeNet {
            val response = Post<Response>(url) {
                headers.forEach {
                    addHeader(it.key,it.value.toString())
                }
                json(body)
            }.await()
            addText("解析返回数据")
            validateData(response)
        }
    }

    fun validateData(response: Response) {
        val code = response.code
        val json = response.body?.string()
        if (code != 200 || json?.contains("responseCode") == true && JSONObject(json).getInt("responseCode") != 200){
            if (code != 200){
                addText("响应状态不匹配，预期为 200 ,实际为 $code")
            }else{
                addText("响应状态不匹配，预期为 200 ,实际为 ${JSONObject(json!!).getInt("responseCode")}\n${JSONObject(
                    json
                ).getString("errMsg")}")
            }
        }
    }

    fun addText(str:Any){
        binding.otaTv.apply {
            text = "$text\n$str"
        }
    }

}