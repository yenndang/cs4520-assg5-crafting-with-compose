package com.cs4520.assignment5.api

import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.cs4520.assignment5.models.Product
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import java.lang.reflect.Type

class ProductDeserializer : JsonDeserializer<Product> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Product {
        val jsonObject = json.asJsonObject

        val type = jsonObject.get("type").asString
        return when (type) {
            "Food" -> Gson().fromJson(json, Product.Food::class.java)
            "Equipment" -> Gson().fromJson(json, Product.Equipment::class.java)
            else -> throw IllegalArgumentException("Unknown type: $type")
        }
    }
}
