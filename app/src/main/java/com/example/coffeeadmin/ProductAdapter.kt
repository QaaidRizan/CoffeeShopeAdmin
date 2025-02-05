package com.example.coffeeadmin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coffeeadmin.model.ItemsModel

class ProductAdapter(private val products: List<ItemsModel>,
                     private val removeProduct: (ItemsModel) -> Unit) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.productImage)
        val titleTextView: TextView = view.findViewById(R.id.productTitle)
        val descriptionTextView: TextView = view.findViewById(R.id.productDescription)
        val priceTextView: TextView = view.findViewById(R.id.productPrice)
        val ratingTextView: TextView = view.findViewById(R.id.productRating)
        val extraTextView: TextView = view.findViewById(R.id.productExtra)
        val removeButton: Button = view.findViewById(R.id.btnRemoveProduct)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.titleTextView.text = product.title
        holder.descriptionTextView.text = product.description
        holder.priceTextView.text = "$${product.price}"
        holder.ratingTextView.text = "Rating: ${product.rating}"
        holder.extraTextView.text = "Extra: ${product.extra}"

        // Load the first image URL if available
        if (product.picUrl.isNotEmpty()) {
            val imageUrl = product.picUrl[0]
            holder.imageView.visibility = View.VISIBLE
            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .into(holder.imageView)
        } else {
            holder.imageView.visibility = View.GONE // Hide the ImageView if no URL
        }

        // Set up the remove button click listener
        holder.removeButton.setOnClickListener {
            removeProduct(product)
        }
    }

    override fun getItemCount() = products.size
}