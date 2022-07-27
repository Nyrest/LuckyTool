package com.luckyzyx.colorosext.ui.adapter.scope

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class ScopeWrapper(val scopes: List<Scope>,val funcs:List<Func>) : Parcelable

@JsonClass(generateAdapter = true)
data class Scope(val id:Int,val title:String,val summer:String?) : Parcelable {
    @Transient
    val funcs: MutableList<Func> = ArrayList()

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()
    ){
        parcel.readTypedList(funcs,Func)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(summer)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Scope> {
        override fun createFromParcel(parcel: Parcel): Scope {
            return Scope(parcel)
        }

        override fun newArray(size: Int): Array<Scope?> {
            return arrayOfNulls(size)
        }
    }

}

@JsonClass(generateAdapter = true)
data class Func(val id:Int,val father:Int,val title:String,val summer:String?,val key:String,val type:String?) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString()!!,
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(father)
        parcel.writeString(title)
        parcel.writeString(summer)
        parcel.writeString(key)
        parcel.writeString(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Func> {
        override fun createFromParcel(parcel: Parcel): Func {
            return Func(parcel)
        }

        override fun newArray(size: Int): Array<Func?> {
            return arrayOfNulls(size)
        }
    }

}



