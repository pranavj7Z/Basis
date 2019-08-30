package com.pranavjayaraj.basis;

data class Content(
        val id: Long = counter++,
        val name: String,
        val city: String
        ) {
        companion object {
private var counter = 0L
        }
        }