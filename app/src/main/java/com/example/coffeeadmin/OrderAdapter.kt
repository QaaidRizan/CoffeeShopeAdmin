package com.example.coffeeadmin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeeadmin.model.Orders

class OrderAdapter(private val orders: List<Orders>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val customerNameTextView: TextView = view.findViewById(R.id.tvCustomerName)
        val customerPhoneTextView: TextView = view.findViewById(R.id.tvCustomerPhone)
        val addressTextView: TextView = view.findViewById(R.id.tvAddress)
        val totalPriceTextView: TextView = view.findViewById(R.id.tvTotalPrice)
        val productCountTextView: TextView = view.findViewById(R.id.tvProductCount)
        val productNamesTextView: TextView = view.findViewById(R.id.tvProductNames)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.customerNameTextView.text = order.customerName
        holder.customerPhoneTextView.text = order.customerPhone
        holder.addressTextView.text = order.address
        holder.totalPriceTextView.text = "$${order.totalPrice}"
        holder.productCountTextView.text = "Product Count: ${order.productCount}"
        holder.productNamesTextView.text = "Products: ${order.productNames.joinToString(", ")}"
    }

    override fun getItemCount() = orders.size
}