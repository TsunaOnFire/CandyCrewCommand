package com.aph.willywonka.utils

import android.content.SharedPreferences
import android.os.Parcelable
import android.util.Log
import com.aph.willywonka.data.model.WorkerBO
import java.io.InvalidClassException
import java.text.SimpleDateFormat

class APH_toolbox {
    companion object{
        fun Fix_Date_Cutom(date_and_hour: String, InputFormat: String, OutputFormat: String):String{
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
                Log.e("Fix_Date_Cutom",e.message.toString())

            }


            return result

        }

        fun ToParcelable(worker:WorkerBO): Parcelable {
            return worker
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
