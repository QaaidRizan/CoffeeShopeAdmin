package com.example.coffeeadmin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.coffeeadmin.model.ItemsModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class AddProductActivity : AppCompatActivity() {

    private lateinit var selectImageBtn: Button
    private lateinit var uploadImageBtn: Button
    private lateinit var addProductBtn: Button
    private lateinit var productImageView: ImageView
    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var ratingEditText: EditText
    private lateinit var extraEditText: EditText

    private var imageUri: Uri? = null
    private var uploadedImageUrl: String? = null
    private val databaseRef = FirebaseDatabase.getInstance("https://coffeeshop1721-default-rtdb.firebaseio.com/").reference.child("Items")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        selectImageBtn = findViewById(R.id.selectImageButton)
        uploadImageBtn = findViewById(R.id.uploadImageButton)
        addProductBtn = findViewById(R.id.addProductButton)
        productImageView = findViewById(R.id.productImageView)
        titleEditText = findViewById(R.id.titleEditText)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        priceEditText = findViewById(R.id.priceEditText)
        ratingEditText = findViewById(R.id.ratingEditText)
        extraEditText = findViewById(R.id.extraEditText)

        selectImageBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 1)
        }

        uploadImageBtn.setOnClickListener {
            imageUri?.let { uri ->
                uploadImageToFirebase(uri)
            } ?: Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show()
        }

        addProductBtn.setOnClickListener {
            saveProductDetails()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data
            productImageView.setImageURI(imageUri)
        }
    }

    private fun uploadImageToFirebase(fileUri: Uri) {
        val storageRef = FirebaseStorage.getInstance().reference
        val fileName = "${UUID.randomUUID()}.jpg"
        val fileRef = storageRef.child(fileName)

        fileRef.putFile(fileUri)
            .addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener { uri ->
                    uploadedImageUrl = uri.toString()
                    Toast.makeText(this, "Image Uploaded Successfully!", Toast.LENGTH_LONG).show()
                    Log.d("FirebaseUpload", "Image URL: $uploadedImageUrl")
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Upload Failed: ${e.message}", Toast.LENGTH_LONG).show()
                Log.e("FirebaseUpload", "Upload Failed", e)
            }
    }

    private fun saveProductDetails() {
        val title = titleEditText.text.toString().trim()
        val description = descriptionEditText.text.toString().trim()
        val priceText = priceEditText.text.toString().trim()
        val ratingText = ratingEditText.text.toString().trim()
        val extraText = extraEditText.text.toString().trim()

        if (title.isEmpty() || description.isEmpty() || priceText.isEmpty() || uploadedImageUrl == null) {
            Toast.makeText(this, "Please fill all required fields and upload an image", Toast.LENGTH_SHORT).show()
            return
        }

        val price = priceText.toDoubleOrNull() ?: 0.0
        val rating = ratingText.toDoubleOrNull() ?: 0.0

        val productData = mapOf(
            "title" to title,
            "description" to description,
            "extra" to extraText,
            "picUrl" to listOf(uploadedImageUrl!!),  // Ensure it's an array with one item
            "price" to price,
            "rating" to rating
        )

        val productId = databaseRef.push().key ?: UUID.randomUUID().toString()

        databaseRef.child(productId)
            .setValue(productData)
            .addOnSuccessListener {
                Toast.makeText(this, "Product added successfully!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to add product: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
