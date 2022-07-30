# 整理一下开发过程中遇到的问题

## 反射系统变量或方法为null

```xml

<application>
    android:name="ModuleApplication"
</application>
```

[Yuki -> ModuleApplication](https://fankes.github.io/YukiHookAPI/#/api/document?id=moduleapplication-class)

## 深色模式重启activity闪白屏

```kotlin
startActivity(Intent(Context, MainActivity::class.java))
Context.overridePendingTransition(R.anim.start_anim, R.anim.out_anim)
Context.finish()
```

## Key.BACK Fragment后台堆栈问题

Preference#app:fragment 默认入栈

```kotlin
//入栈
FragmentTransaction.addToBackStack(null).commit()
//不入栈
FragmentTransaction.commitNow()
```

## Fragment OnBackPressed

Fragment没有Activity里的OnBackPressed返回事件
```kotlin
//处理Fragment嵌套子Fragment返回问题
//1.定义扩展函数
typealias OnBackPressedTypeAlias = () -> Unit
/**
 * 解决 Fragment 中 OnBackPressed 事件, 默认结束当前Fragment依附的Activity
 * @param type true:结束当前Activity，false：响应callback回调
 */
fun Fragment.setOnHandleBackPressed(type: Boolean = true, callback: OnBackPressedTypeAlias? = null) {
    requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (type) {
                    requireActivity().finish()
                } else {
                    callback?.invoke()
                }
            }
        })
}

//2.在Fragment中的onCreate方法中使用
setOnHandleBackPressed()
//若需要在返回时做出相应处理
setOnHandleBackPressed(false) {
    Toast.makeText(context, "点击了返回键", Toast.LENGTH_LONG).show()
}
```