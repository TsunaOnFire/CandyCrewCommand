package com.aph.willywonka.utils

import android.content.SharedPreferences
import android.os.Parcelable
import android.util.Log
import androidx.annotation.DrawableRes
import com.aph.willywonka.R
import com.aph.willywonka.data.model.FavouriteModel
import com.aph.willywonka.data.model.WorkerBO
import java.io.InvalidClassException
import java.text.SimpleDateFormat

class AphToolbox {
    companion object{
        fun FixDateCustom(date_and_hour: String, InputFormat: String, OutputFormat: String):String{
            var result = date_and_hour
            try {
                val dateFormatInput = SimpleDateFormat(InputFormat) //this format changeable according to your choice
                val dateFormatOutput = SimpleDateFormat(OutputFormat) //this format changeable according to your choice
                if(date_and_hour==""||date_and_hour=="-"){
                    return ""
                }
                val data=dateFormatInput.parse(date_and_hour)

                result=""
                if(data!=null){
                    result=dateFormatOutput.format(data)
                }
            }
            catch(e:Exception){
                Log.e("FixDateCustom",e.message.toString())

            }


            return result

        }

        fun ToParcelable(worker:WorkerBO): Parcelable {
            return worker
        }

        fun DummyWorker(id: Int = 1) = WorkerBO(
            id = id,
            first_name = "$id F.Name",
            last_name = "Last Name",
            description = "Example Description",
            image = "https://picsum.photos/200/300",
            profession = "Profession",
            quota = "Example Quota",
            height = 180,
            country = "Example Country",
            age = 100,
            gender = "M",
            email = "example@example.com",
            FavouriteModel(
                color = "Example Color",
                food = "Example Food",
                random_string = "Example Random STRING",
                song = "Random Song"
            )


        )

        @DrawableRes
        fun GetGenderIcon(gender: String?) = when(gender) {
            "M"-> R.mipmap.ic_male
            "F"-> R.mipmap.ic_female
            else-> R.mipmap.ic_female
        }
    }
}


inline fun <reified T> SharedPreferences.get(key: String, defaultValue: T): T {
    return when(T::class) {
        Boolean::class -> this.getBoolean(key, defaultValue as Boolean) as T
        Float::class -> this.getFloat(key, defaultValue as Float) as T
        Int::class -> this.getInt(key, defaultValue as Int) as T
        Long::class -> this.getLong(key, defaultValue as Long) as T
        String::class -> this.getString(key, defaultValue as String) as T
        else -> throw InvalidClassException("Only Boolean, Float, Int, Long or String are valid")
    }
}

inline fun <reified T> SharedPreferences.put(key: String, value: T) {
    val editor = this.edit()
    when(T::class) {
        Boolean::class -> editor.putBoolean(key, value as Boolean)
        Float::class -> editor.putFloat(key, value as Float)
        Int::class -> editor.putInt(key, value as Int)
        Long::class -> editor.putLong(key, value as Long)
        String::class -> editor.putString(key, value as String)
        else -> throw InvalidClassException("Only Boolean, Float, Int, Long or String are valid")
    }
    editor.apply()
}

fun WorkerBO.isEmpty(): Boolean {
    return id == null
            || first_name.isNullOrEmpty()
            || last_name.isNullOrEmpty()
            || age == null
            || profession.isNullOrEmpty()
            || email.isNullOrEmpty()
            || image.isNullOrEmpty()
}

fun WorkerBO.isFullDownload(): Boolean {
    return !isEmpty()
            && !description.isNullOrEmpty()
            && !quota.isNullOrEmpty()

}
