package com.example.facebook.models

data class Post (
    val text: String = "",
    val createdBy: User = User(),
    val createdAt: Long = 0L,
    val uploadImage:String="",
    val likedBy: ArrayList<String> = ArrayList())