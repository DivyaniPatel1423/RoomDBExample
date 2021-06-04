package com.testexample.practicemaker.model.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_dishes_table")
data class FavDish(
        @ColumnInfo val image: String?,
        @ColumnInfo(name = "image_source") val imageSource: String?,
        @ColumnInfo val title: String?,
        @ColumnInfo val type: String?,
        @ColumnInfo val category: String?,
        @ColumnInfo val ingredients: String?,

        @ColumnInfo(name = "cooking_time") val cookingTime: String?,
        @ColumnInfo(name = "instructions") val directionToCook: String?,
        @ColumnInfo(name = "favorite_dish") val favoriteDish: Boolean,

        @PrimaryKey(autoGenerate = true) val id:Int =0

) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(image)
        parcel.writeString(imageSource)
        parcel.writeString(title)
        parcel.writeString(type)
        parcel.writeString(category)
        parcel.writeString(ingredients)
        parcel.writeString(cookingTime)
        parcel.writeString(directionToCook)
        parcel.writeByte(if (favoriteDish) 1 else 0)
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FavDish> {
        override fun createFromParcel(parcel: Parcel): FavDish {
            return FavDish(parcel)
        }

        override fun newArray(size: Int): Array<FavDish?> {
            return arrayOfNulls(size)
        }
    }
}