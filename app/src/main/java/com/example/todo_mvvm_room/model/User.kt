package com.example.todo_mvvm_room.model

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val lastName: String,
    val firstName: String,
    val age: Int,
    @Embedded
    val address: Address,

    val avatar: Bitmap,
) : Parcelable

@Parcelize
data class Address(
    val streetName: String,
    val streetNumber: Int,
) : Parcelable