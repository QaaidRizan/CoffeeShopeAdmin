package com.example.coffeeadmin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class AdminPanelActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)

        val addProductButton: Button = findViewById(R.id.addProductButton)
        val viewProductListButton: Button = findViewById(R.id.viewProductListButton)

        viewProductListButton.setOnClickListener {
            val intent = Intent(this, ViewProductsActivity::class.java)
            startActivity(intent)
        }

        // Add your button click listeners and other logic here
    }
}