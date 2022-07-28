package com.luckyzyx.colorosext.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.luckyzyx.colorosext.databinding.FragmentMagiskBinding

class MagiskFragment : Fragment() {

    private lateinit var binding: FragmentMagiskBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMagiskBinding.inflate(inflater)
        return binding.root
    }

    data class User(val id:Int,val name:String,val info:UserInfo)
    data class UserInfo(val age:Int, val sex:String)

    data class School(val id: Int,val name: String,val classlist:List<Class>)
    data class Class(val id:Int,val name:String,val studentlist:List<Student>)
    data class Student(val id:Int,val name: String)

    fun createUser():User{
        return User(1,"Jack",UserInfo(25,"Sales"))
    }

    fun createMultiUser(): List<User> {
        return listOf(
            User(1,"Jack",UserInfo(25,"Sales")),
            User(2,"Pony",UserInfo(30,"Marketing")),
            User(3,"Robin",UserInfo(31,"Engineer"))
        )
    }

//    private fun createData(): ScopeWrapper {
//        return ScopeWrapper(
//            listOf(
//                Scope(1, "作用域1", ""),
//                Scope(2, "作用域2", ""),
//                Scope(3, "作用域3", ""),
//            ),
//            listOf(
//                Func(1,1, "方法1", null, "key1", null),
//                Func(2,1, "方法2", null, "key2", null),
//                Func(3,1, "方法3", null, "key3", null),
//            )
//        )
//    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val funs = createData()
//
//        val toJson = MoshiUtil.toJson(funs,"  ")
//        val fromJson = MoshiUtil.fromJson<ScopeWrapper>(toJson)
//
//        binding.tv.text = toJson
//        binding.tv2.text = fromJson.toString()

    }
}