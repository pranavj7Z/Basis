package com.pranavjayaraj.basis


import androidx.recyclerview.widget.DiffUtil

class ContentDiffCallback(
        private val old: List<Content>,
        private val aNew: List<Content>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return old.size
    }

    override fun getNewListSize(): Int {
        return aNew.size
    }

    override fun areItemsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return old[oldPosition].id == aNew[newPosition].id
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return old[oldPosition] == aNew[newPosition]
    }

}
