package com.cs4520.assignment5.models

// Sealed classes for product list (from dataset) in the ProductAdapter

sealed class Product {
    abstract val name: String
    abstract val price: String
    abstract val expiryDate: String?

    data class Food(
        override val name: String,
        override val expiryDate: String?,
        override val price: String
    ) : Product()

    data class Equipment(
        override val name: String,
        override val expiryDate: String?,
        override val price: String
    ) : Product()
}

