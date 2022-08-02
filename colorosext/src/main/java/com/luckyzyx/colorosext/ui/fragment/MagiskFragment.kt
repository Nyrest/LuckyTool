package com.luckyzyx.colorosext.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import com.luckyzyx.colorosext.R
import com.luckyzyx.colorosext.databinding.FragmentMagiskBinding
import com.luckyzyx.colorosext.utils.MoshiUtil
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter

class MagiskFragment : Fragment() {

    private lateinit var binding: FragmentMagiskBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMagiskBinding.inflate(inflater)
        initToolbar()
        return binding.root
    }

    fun initToolbar(){
        val toolbar = binding.toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.title = getString(R.string.nav_magisk)
        setHasOptionsMenu(true)
    }

    @JsonClass(generateAdapter = true)
    data class User(val id: Int, val name: String, val info: UserInfo)
    @JsonClass(generateAdapter = true)
    data class UserInfo(val age: Int, val sex: String)

    fun createUser(): User {
        return User(1, "Jack", UserInfo(25, "Sales"))
    }

    fun createMultiUser(): List<User> {
        return listOf(
            User(1, "Jack", UserInfo(10, "男")),
            User(2, "Pony", UserInfo(20, "男")),
            User(3, "Robin", UserInfo(30, "男"))
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val funs = createUser()

//        val moshi = Moshi.Builder().build()
//        val adapter = moshi.adapter(User::class.java).indent("  ")
//        val toJson = adapter.toJson(funs)

        val toJson = MoshiUtil.toJson(funs,"  ")
        val fromJson = MoshiUtil.fromJson<User>(toJson)

        binding.tv.text = toJson
        binding.tv2.text = fromJson.toString()
    }
}