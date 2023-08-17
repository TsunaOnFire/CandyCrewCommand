package com.aph.willywonka.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aph.willywonka.data.model.WorkerBO

const val workerTableName = "worker_table"

@Entity(tableName = workerTableName)
data class WorkerEntity (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")val id: Int? =0,
    @ColumnInfo(name = "first_name")val first_name: String?,
    @ColumnInfo(name = "last_name")val last_name: String?,
    @ColumnInfo(name = "profession")val profession: String?,
    @ColumnInfo(name = "email")val email: String?,
    @ColumnInfo(name = "country")val country: String?,
    @ColumnInfo(name = "gender")val gender: String?,
    @ColumnInfo(name = "age")val age: Int?,
    @ColumnInfo(name = "height")val height: Int?,
    @ColumnInfo(name = "quota")val quota: String?,
    @ColumnInfo(name = "description")val description: String?,
    @ColumnInfo(name = "image")val image: String?,
    //favorite
    @ColumnInfo(name = "color")val color:String?,
    @ColumnInfo(name = "food")val food:String?,
    @ColumnInfo(name = "song")val song:String?,
    @ColumnInfo(name = "random_string")val random_string:String?,


)

fun WorkerBO.toBDO() = WorkerEntity(
    id = id,
    first_name = first_name,
    last_name = last_name,
    profession = profession,
    email = email,
    country = country,
    gender = gender,
    age = age,
    height = height,
    quota= quota,
    description= description,
    image = image,

    color = favorite?.color,
    food = favorite?.food,
    song = favorite?.song,
    random_string = favorite?.random_string,
)