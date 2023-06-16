package com.example.capstone.adapter

import android.os.Parcel
import android.os.Parcelable

data class Food(
    val foodName: String?,
    val calories: Int?,
    val protein: Double?,
    val fat: Double?,
    val saturatedFat: Double?,
    val fiber: Double?,
    val carbs: Double?,
    val kelompok: String?,
    val imageUrl: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(foodName)
        parcel.writeValue(calories)
        parcel.writeValue(protein)
        parcel.writeValue(fat)
        parcel.writeValue(saturatedFat)
        parcel.writeValue(fiber)
        parcel.writeValue(carbs)
        parcel.writeString(kelompok)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Food> {
        override fun createFromParcel(parcel: Parcel): Food {
            return Food(parcel)
        }

        override fun newArray(size: Int): Array<Food?> {
            return arrayOfNulls(size)
        }
    }
}
