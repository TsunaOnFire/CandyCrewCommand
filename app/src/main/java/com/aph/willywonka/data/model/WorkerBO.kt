package com.aph.willywonka.data.model

import android.os.Parcel
import android.os.Parcelable
import com.aph.willywonka.data.database.entities.WorkerEntity
import com.aph.willywonka.data.network.model.WorkerDTO
import com.google.gson.annotations.SerializedName

const val GENDER_MALE = "M"
const val GENDER_FEMALE = "F"

data class WorkerBO(
    @SerializedName("id") val id: Int?,
    @SerializedName("first_name") val first_name: String?,
    @SerializedName("last_name") val last_name: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("profession") val profession: String?,
    @SerializedName("quota") val quota: String?,
    @SerializedName("height") val height: Int?,
    @SerializedName("country") val country: String?,
    @SerializedName("age") val age: Int?,
    @SerializedName("gender") val gender: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("favorite") val favorite: FavouriteModel?,
) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        id = parcel.readValue(Int::class.java.classLoader) as? Int,
        first_name = parcel.readString(),
        last_name = parcel.readString(),
        description = parcel.readString(),
        image = parcel.readString(),
        profession = parcel.readString(),
        quota = parcel.readString(),
        height = parcel.readValue(Int::class.java.classLoader) as? Int,
        country = parcel.readString(),
        age = parcel.readValue(Int::class.java.classLoader) as? Int,
        gender = parcel.readString(),
        email = parcel.readString(),
        parcel.readParcelable(FavouriteModel::class.java.classLoader)
        ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(first_name)
        parcel.writeString(last_name)
        parcel.writeString(description)
        parcel.writeString(image)
        parcel.writeString(profession)
        parcel.writeString(quota)
        parcel.writeValue(height)
        parcel.writeString(country)
        parcel.writeValue(age)
        parcel.writeString(gender)
        parcel.writeString(email)
        parcel.writeParcelable(favorite, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WorkerBO> {
        override fun createFromParcel(parcel: Parcel): WorkerBO {
            return WorkerBO(parcel)
        }

        override fun newArray(size: Int): Array<WorkerBO?> {
            return arrayOfNulls(size)
        }
    }
}

data class FavouriteModel(
    @SerializedName("color") val color: String?,
    @SerializedName("food") val food: String?,
    @SerializedName("random_string") val random_string: String?,
    @SerializedName("song") val song: String?
) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(color)
        parcel.writeString(food)
        parcel.writeString(random_string)
        parcel.writeString(song)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FavouriteModel> {
        override fun createFromParcel(parcel: Parcel): FavouriteModel {
            return FavouriteModel(parcel)
        }

        override fun newArray(size: Int): Array<FavouriteModel?> {
            return arrayOfNulls(size)
        }
    }
}

fun WorkerDTO?.toBO() = WorkerBO(
    id = this?.id,
    first_name = this?.first_name,
    last_name = this?.last_name,
    description = this?.description,
    image = this?.image,
    profession = this?.profession,
    quota = this?.quota,
    height = this?.height,
    country = this?.country,
    age = this?.age,
    gender = this?.gender,
    email = this?.email,
    favorite = this?.favorite,
)

fun WorkerEntity.toBO() = WorkerBO(
    id = id,
    first_name = first_name,
    last_name = last_name,
    description = description,
    image = image,
    profession = profession,
    quota = quota,
    height = height,
    country = country,
    age = age,
    gender = gender,
    email = email,
    favorite = FavouriteModel(
        color,
        food,
        random_string,
        song,
    )
)