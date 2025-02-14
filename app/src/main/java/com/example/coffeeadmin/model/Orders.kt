package com.example.coffeeadmin.model

import android.os.Parcel
import android.os.Parcelable
import java.text.SimpleDateFormat
import java.util.*

data class Orders(
    var id: String = "",
    val customerName: String = "",
    val customerPhone: String = "",
    val address: String = "",
    val totalPrice: Double = 0.0,
    val productCount: Int = 0,
    val productNames: List<String?> = emptyList(),
    val orderDate: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()),
    var status: String = "Pending" // Default status
)

data class CartItem(
    val productName: String?,
    val quantity: Int
) : Parcelable {
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(productName)
        parcel.writeInt(quantity)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<CartItem> {
        override fun createFromParcel(parcel: Parcel): CartItem {
            return CartItem(
                parcel.readString(),
                parcel.readInt()
            )
        }

        override fun newArray(size: Int): Array<CartItem?> {
            return arrayOfNulls(size)
        }
    }
}