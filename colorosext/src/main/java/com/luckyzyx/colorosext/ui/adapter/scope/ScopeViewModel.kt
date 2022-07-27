package com.luckyzyx.colorosext.ui.adapter.scope

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

class ScopeViewModel(application: Application) : AndroidViewModel(application) {

    val scopeList = MutableLiveData<List<Scope>>()

    //加载数据
    @OptIn(DelicateCoroutinesApi::class)
    fun loadData(data:ScopeWrapper){
        GlobalScope.launch(Dispatchers.Main){
            val scopes = withContext(Dispatchers.IO){

                val scopeMap = HashMap<Int, Scope>()
                //作用域
                //存储键值 作用域id -> 作用域组
                for (scope in data.scopes) {
                    scopeMap[scope.id] = scope
                }
                //遍历方法存储进
                //遍历方法存储进作用域组里的方法组
                for (func in data.funcs) {
                    scopeMap[func.father]?.funcs?.add(func)
                }
                //遍历包装器里的作用域组,获取作用域里的方法组,按照方法里的id排序
                for (scope in data.scopes) {
                    scope.funcs.sortBy { it.id }
                }
                //遍历包装器里的作用域组,获取作用域里的方法组,按照方法里的id排序
                data.scopes.sortedBy { it.id }
            }
            scopeList.value = scopes
        }
    }
}