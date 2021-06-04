package com.testexample.practicemaker.utils

object Constants {

    const val DISH_TYPE = "DishType"
    const val DISH_CATEGORY = "DishCategory"
    const val DISH_COOKING_TIME = "DishCookingTime"
    const val DISH_IMAGE_SOURCE_LOCAL = "Local"
    const val DISH_IMAGE_SOURCE_OLINE = "Online"
    fun dishType() : ArrayList<String>{
        val list = ArrayList<String>()
        list.add("breakfast")
        list.add("lunch")
        list.add("snack")
        list.add("dinner")
        list.add("desert")
        list.add("salad")
        list.add("side dish")
        list.add("other")
        return list
    }

    fun dishCategories() : ArrayList<String>{
        val list = ArrayList<String>()
        list.add("pizza")
        list.add("BBQ")
        list.add("Bakery")
        list.add("dinner")
        list.add("Cafe")
        list.add("Chiken")
        list.add("Dessert")
        list.add("Drink")
        list.add("Hot Dogs")
        list.add("Juice")
        list.add("Sandwich")
        list.add("Tea and Coffee")
        list.add("Wraps")
        list.add("Other")
        return list
    }

    fun dishCookeTime() : ArrayList<String>{
        val list = ArrayList<String>()
        list.add("10")
        list.add("15")
        list.add("20")
        list.add("30")
        list.add("40")
        list.add("50")
        list.add("60")
        list.add("90")
        list.add("120")
        list.add("150")
        list.add("160")
        return list
    }
}