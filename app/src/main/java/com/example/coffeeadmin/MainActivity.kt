package com.example.coffeeadmin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)

        val addProductButton: Button = findViewById(R.id.addProductButton)

        addProductButton.setOnClickListener {
            val intent = Intent(this, AddProductActivity::class.java)
            startActivity(intent)
        }
        val viewProductListButton: Button = findViewById(R.id.viewProductListButton)

        viewProductListButton.setOnClickListener {
            val intent = Intent(this, ViewProductsActivity::class.java)
            startActivity(intent)
        }
        val orderListButton: Button = findViewById(R.id.orderDetailsButton)

        orderListButton.setOnClickListener {
            val intent = Intent(this, AdminOrdersActivity::class.java)
            startActivity(intent)
        }
    }
}