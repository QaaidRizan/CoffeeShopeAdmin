package com.example.coffeeadmin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeeadmin.model.Orders
import com.google.firebase.database.FirebaseDatabase

class OrderAdapter(private val orders: List<Orders>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val orderDateTextView: TextView = view.findViewById(R.id.tvOrderDate)
        val customerNameTextView: TextView = view.findViewById(R.id.tvCustomerName)
        val customerPhoneTextView: TextView = view.findViewById(R.id.tvCustomerPhone)
        val addressTextView: TextView = view.findViewById(R.id.tvAddress)
        val totalPriceTextView: TextView = view.findViewById(R.id.tvTotalPrice)
        val productCountTextView: TextView = view.findViewById(R.id.tvProductCount)
        val productNamesTextView: TextView = view.findViewById(R.id.tvProductNames)
        val orderStatusSpinner: Spinner = view.findViewById(R.id.spinnerOrderStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]

        // Display date
        holder.orderDateTextView.text = "Order Date: ${order.orderDate}"

        // Display other details
        holder.customerNameTextView.text = order.customerName
        holder.customerPhoneTextView.text = order.customerPhone
        holder.addressTextView.text = order.address
        holder.totalPriceTextView.text = "$${String.format("%.2f", order.totalPrice)}"
        holder.productCountTextView.text = "Items: ${order.productCount}"
        holder.productNamesTextView.text = "Products: ${order.productNames.joinToString(", ")}"

        // Set Spinner value
        val statusArray = holder.itemView.context.resources.getStringArray(R.array.order_status_array)
        val statusPosition = statusArray.indexOf(order.status)
        holder.orderStatusSpinner.setSelection(statusPosition)

        // Update Firebase on Spinner value change
        holder.orderStatusSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val newStatus = statusArray[position]
                if (newStatus != order.status) {
                    updateOrderStatusInFirebase(order.id, newStatus)
                    order.status = newStatus
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    override fun getItemCount() = orders.size

    private fun updateOrderStatusInFirebase(orderId: String, newStatus: String) {
        val databaseRef = FirebaseDatabase.getInstance("https://coffeeshop1721-default-rtdb.firebaseio.com/").reference.child("Orders")
        databaseRef.child(orderId).child("status").setValue(newStatus)
            .addOnSuccessListener {
                // Handle success
            }
            .addOnFailureListener {
                // Handle failure
            }
    }
}