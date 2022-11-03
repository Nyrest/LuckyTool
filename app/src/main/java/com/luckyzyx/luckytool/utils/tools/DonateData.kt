package com.luckyzyx.luckytool.utils.tools

import java.io.Serializable

data class DonateInfo(
    val name: String,
    val money: Double
) : Serializable

object DonateData {
    fun getDonateList(): ArrayList<DonateInfo> {
        return ArrayList<DonateInfo>().apply {
            add(DonateInfo("是小奶糖啊", 21.0))
            add(DonateInfo("Kimjaejiang", 5.0))
            add(DonateInfo("午时已到", 20.0))
            add(DonateInfo("邹王", 15.0))
            add(DonateInfo("す", 20.0))
            add(DonateInfo("楠", 10.0))
            add(DonateInfo("天伞桜", 88.8))
            add(DonateInfo("北风是不是冷", 15.0))
            add(DonateInfo("ssd.风格", 5.0))
            add(DonateInfo("智", 15.0))
            add(DonateInfo("松花蛋", 10.0))
            add(DonateInfo("佘樂", 20.0))
            add(DonateInfo("风冷涂的蜡", 10.0))
            add(DonateInfo("才", 10.0))
            add(DonateInfo("灯", 5.0))
            add(DonateInfo("G", 66.0))
            add(DonateInfo("荣", 8.80))
            add(DonateInfo("斯已", 15.0))
            add(DonateInfo("冰梦", 20.0))
            add(DonateInfo("?", 8.88))
            add(DonateInfo("晨钟酱", 100.0))
            add(DonateInfo("小李.", 15.0))
        }
    }
}