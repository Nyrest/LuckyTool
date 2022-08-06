package com.luckyzyx.luckytool.hook.apps.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class GlobalFillet : YukiBaseHooker() {
    override fun onHook() {
        resources().hook {
            injectResource {
                conditions {
                    name = "system_app_widget_inner_radius"
                    dimen()
                }
                replaceTo(22)
            }
            injectResource {
                conditions {
                    name = "car_button_radius"
                    dimen()
                }
                replaceTo(22)
            }
            injectResource {
                conditions {
                    name = "car_radius_1"
                    dimen()
                }
                replaceTo(22)
            }
            injectResource {
                conditions {
                    name = "car_radius_2"
                    dimen()
                }
                replaceTo(22)
            }
            injectResource {
                conditions {
                    name = "car_radius_3"
                    dimen()
                }
                replaceTo(22)
            }
            injectResource {
                conditions {
                    name = "chooser_corner_radius"
                    dimen()
                }
                replaceTo(22)
            }
            injectResource {
                conditions {
                    name = "config_bottomDialogCornerRadius"
                    dimen()
                }
                replaceTo(22)
            }
            injectResource {
                conditions {
                    name = "config_buttonCornerRadius"
                    dimen()
                }
                replaceTo(22)
            }
            injectResource {
                conditions {
                    name = "config_dialogCornerRadius"
                    dimen()
                }
                replaceTo(22)
            }
            injectResource {
                conditions {
                    name = "config_progressBarCornerRadius"
                    dimen()
                }
                replaceTo(22)
            }
            injectResource {
                conditions {
                    name = "date_picker_day_selector_radius"
                    dimen()
                }
                replaceTo(22)
            }
            injectResource {
                conditions {
                    name = "default_magnifier_corner_radius"
                    dimen()
                }
                replaceTo(22)
            }
            injectResource {
                conditions {
                    name = "dialog_corner_radius"
                    dimen()
                }
                replaceTo(22)
            }
            injectResource {
                conditions {
                    name = "keyguard_avatar_frame_shadow_radius"
                    dimen()
                }
                replaceTo(22)
            }
            injectResource {
                conditions {
                    name = "leanback_dialog_corner_radius"
                    dimen()
                }
                replaceTo(22)
            }
            injectResource {
                conditions {
                    name = "notification_action_button_radius"
                    dimen()
                }
                replaceTo(22)
            }
            injectResource {
                conditions {
                    name = "rounded_corner_radius"
                    dimen()
                }
                replaceTo(22)
            }
            injectResource {
                conditions {
                    name = "rounded_corner_radius_bottom"
                    dimen()
                }
                replaceTo(22)
            }
            injectResource {
                conditions {
                    name = "rounded_corner_radius_top"
                    dimen()
                }
                replaceTo(22)
            }
            injectResource {
                conditions {
                    name = "subtitle_corner_radius"
                    dimen()
                }
                replaceTo(22)
            }
            injectResource {
                conditions {
                    name = "subtitle_shadow_radius"
                    dimen()
                }
                replaceTo(22)
            }
            injectResource {
                conditions {
                    name = "timepicker_center_dot_radius"
                    dimen()
                }
                replaceTo(22)
            }
            injectResource {
                conditions {
                    name = "timepicker_selector_dot_radius"
                    dimen()
                }
                replaceTo(22)
            }
            injectResource {
                conditions {
                    name = "timepicker_selector_radius"
                    dimen()
                }
                replaceTo(22)
            }
            injectResource {
                conditions {
                    name = "tooltip_corner_radius"
                    dimen()
                }
                replaceTo(22)
            }
            injectResource {
                conditions {
                    name = "messaging_image_rounding"
                    dimen()
                }
                replaceTo(22)
            }
        }
    }
}