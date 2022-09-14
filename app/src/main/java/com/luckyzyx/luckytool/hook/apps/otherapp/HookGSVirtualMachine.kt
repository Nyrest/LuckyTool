package com.luckyzyx.luckytool.hook.apps.otherapp

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class HookGSVirtualMachine : YukiBaseHooker() {
    override fun onHook() {
        //去更新
        findClass("com.vphonegaga.titan.beans.AppUpdateBean").hook {
            injectMember {
                method {
                    name = "isForceUpdate"
                }
                replaceToFalse()
            }
            injectMember {
                method {
                    name = "isLatest"
                }
                replaceToTrue()
            }
        }
        //VIP功能
        findClass("com.vphonegaga.titan.roles.ConfigMgr").hook {
            injectMember {
                method {
                    name = "getNeedClearAllAdsFiles"
                }
                replaceToTrue()
            }
            injectMember {
                method {
                    name = "getMagiskEnabled"
                }
                replaceToTrue()
            }
            injectMember {
                method {
                    name = "getAndroid10Enabled"
                }
                replaceToTrue()
            }
            injectMember {
                method {
                    name = "getVulkanEnabled"
                }
                replaceToTrue()
            }
        }
        findClass("com.vphonegaga.titan.personalcenter.beans.MaterialBean.Material").hook {
            injectMember {
                method {
                    name = "isFeature"
                }
                replaceToTrue()
            }
        }
        findClass("com.vphonegaga.titan.user.User").hook {
            injectMember {
                method {
                    name = "isVip"
                }
                replaceToTrue()
            }
            injectMember {
                method {
                    name = "isEnableAccAdvanceFeatures"
                }
                replaceToTrue()
            }
        }
        findClass("com.vphonegaga.titan.user.User.Builder").hook {
            injectMember {
                method {
                    name = "isVip"
                }
                beforeHook {
                    args(0).setTrue()
                }
            }
            injectMember {
                method {
                    name = "enableAccAdvanceFeatures"
                }
                beforeHook {
                    args(0).setTrue()
                }
            }
        }
    }
}