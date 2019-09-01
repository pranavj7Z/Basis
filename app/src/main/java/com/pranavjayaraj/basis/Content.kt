package com.pranavjayaraj.basis;

data class Content(
        val id: Long = counter++,/** auto increments for each item in the list used by the indicator */
        val id_url: String,/** indicates the id data from the url */
        val data_url: String /** indicates the text data from the url */
        ) {
        companion object {
private var counter = 0L
        }
        }