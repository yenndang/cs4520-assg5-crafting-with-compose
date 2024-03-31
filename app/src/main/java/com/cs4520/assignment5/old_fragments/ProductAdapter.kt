package com.cs4520.assignment5.old_fragments

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cs4520.assignment5.R
import com.cs4520.assignment5.databinding.ItemProductBinding
import com.cs4520.assignment5.models.Product

class ProductAdapter(private var products: MutableList<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun getItemCount(): Int = products.size
    private val productNames = mutableSetOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        // Using ViewBinding for inflation
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    fun updateProducts(newProducts: List<Product>) {
        // Update the method to avoid adding duplicates
        newProducts.forEach { newProduct ->
            if (!products.any { existingProduct -> existingProduct.name == newProduct.name }) {
                products.add(newProduct)
            }
        }
        notifyDataSetChanged()
    }

    class ProductViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            with(binding) {
                textViewName.text = product.name
                textViewPrice.text = "$${product.price}"

                textViewExpiryDate.apply {
                    if (product.expiryDate != null) {
                        visibility = View.VISIBLE
                        text = product.expiryDate
                    } else {
                        visibility = View.GONE
                    }
                }

                // Update this block to handle product type using `is` checks
                when (product) {
                    is Product.Food -> {
                        root.setBackgroundColor(Color.parseColor("#FFD965")) // Light Yellow
                        imageViewProductIcon.setImageResource(R.drawable.food)
                    }
                    is Product.Equipment -> {
                        root.setBackgroundColor(Color.parseColor("#E06666")) // Light Red
                        imageViewProductIcon.setImageResource(R.drawable.equipment)
                    }
                    // Add more cases for other product types if necessary
                }

                // Setting text color for all TextViews inside the item
                listOf(textViewName, textViewPrice, textViewExpiryDate).forEach { textView ->
                    textView.setTextColor(Color.parseColor("#000000"))
                }
            }
        }
    }
}
