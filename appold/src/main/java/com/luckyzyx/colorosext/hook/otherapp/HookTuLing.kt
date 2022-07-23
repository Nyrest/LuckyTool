package com.luckyzyx.tools.hook.otherapp

import android.content.Context
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.classOf
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType

class HookTuLing : YukiBaseHooker() {
    override fun onHook() {
        findClass("com.qihoo.util.c").hook {
            injectMember {
                method {
                    name = "a"
                    param(ContextClass)
                }
                afterHook {
                    //获取到360的Context对象，通过这个对象来获取classloader
                    val context = args[0] as Context
                    //获取360的classloader，之后hook加固后的代码就使用这个classloader
                    val classLoader = context.classLoader
                    //替换classloader,hook加固后的真正代码
                    classOf("$packageName.model.bean.TUser",classLoader).hook(isUseAppClassLoader = false) {
                        injectMember {
                            method {
                                name = "getLocalVIP"
                            }
                            replaceToTrue()
                        }
                        injectMember {
                            method {
                                name = "getVIP"
                            }
                            replaceToTrue()
                        }
                        injectMember {
                            method {
                                name = "getUserType"
                            }
                            replaceTo(2)
                        }
                    }
                    classOf("$packageName.model.bean.MyUser",classLoader).hook(isUseAppClassLoader = false) {
                        injectMember {
                            method {
                                name = "getVipTime"
                            }
                            replaceTo("永久会员")
                        }
                    }
                }
            }
        }
    }
}