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
```kotlin
//入栈
FragmentTransaction.addToBackStack(null).commit()
//不入栈
FragmentTransaction.commitNow()
```

