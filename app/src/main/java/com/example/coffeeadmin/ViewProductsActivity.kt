package com.example.coffeeadmin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeeadmin.model.ItemsModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ViewProductsActivity : AppCompatActivity() {

    private lateinit var productsRecyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private val productList = mutableListOf<ItemsModel>()
    private val databaseRef = FirebaseDatabase.getInstance("https://coffeeshop1721-default-rtdb.firebaseio.com/").reference.child("Items")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_products)

        productsRecyclerView = findViewById(R.id.productsRecyclerView)
        productsRecyclerView.layoutManager = LinearLayoutManager(this)

        productAdapter = ProductAdapter(productList) { product ->
            removeProductFromFirebase(product.id)
        }
        productsRecyclerView.adapter = productAdapter

        fetchProductsFromFirebase()
    }

    private fun fetchProductsFromFirebase() {
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()  // Clear the list before adding new items

                if (snapshot.exists()) {
                    for (productSnapshot in snapshot.children) {
                        val product = productSnapshot.getValue(ItemsModel::class.java)
                        product?.id = productSnapshot.key ?: ""  // Set the ID from the snapshot key
                        product?.let { productList.add(it) }
                    }
                    productAdapter.notifyDataSetChanged()  // Notify adapter about data change
                } else {
                    Toast.makeText(this@ViewProductsActivity, "No products found!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ViewProductsActivity, "Failed to load products: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun removeProductFromFirebase(productId: String) {
        // Check if the product ID is valid
        if (productId.isNotEmpty()) {
            databaseRef.child(productId).removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Product removed successfully!", Toast.LENGTH_SHORT).show()
                    // Remove the product from the local list and notify the adapter
                    val productToRemove = productList.find { it.id == productId }
                    productToRemove?.let {
                        productList.remove(it)
                        productAdapter.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(this, "Failed to remove product: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Product ID is invalid", Toast.LENGTH_SHORT).show()
        }
    }
}