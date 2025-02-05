package com.example.coffeeadmin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeeadmin.model.Orders
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminOrdersActivity : AppCompatActivity() {

    private lateinit var ordersRecyclerView: RecyclerView
    private lateinit var orderAdapter: OrderAdapter
    private val orderList = mutableListOf<Orders>()
    private val databaseRef = FirebaseDatabase.getInstance("https://coffeeshop1721-default-rtdb.firebaseio.com/").reference.child("Orders")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_orders)

        ordersRecyclerView = findViewById(R.id.rvOrders)
        ordersRecyclerView.layoutManager = LinearLayoutManager(this)

        orderAdapter = OrderAdapter(orderList)
        ordersRecyclerView.adapter = orderAdapter

        fetchOrdersFromFirebase()
    }

    private fun fetchOrdersFromFirebase() {
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                orderList.clear()  // Clear the list before adding new items

                if (snapshot.exists()) {
                    for (orderSnapshot in snapshot.children) {
                        val order = orderSnapshot.getValue(Orders::class.java)
                        order?.let {
                            it.id = orderSnapshot.key ?: "" // Set the ID from the snapshot key
                            orderList.add(it)
                        }
                    }
                    orderAdapter.notifyDataSetChanged()  // Notify adapter about data change
                } else {
                    Toast.makeText(this@AdminOrdersActivity, "No orders found!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AdminOrdersActivity, "Failed to load orders: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}