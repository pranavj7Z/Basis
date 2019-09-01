package com.pranavjayaraj.basis;
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
/**
Adapter for loading the data into the swipecards
 */
class SwipeAdapter(
        private var contents: List<Content> = emptyList()
) : RecyclerView.Adapter<SwipeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.data, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val spot = contents[position]
        holder.id.text = "${spot.id_url}"
        holder.data.text = spot.data_url

    }

    override fun getItemCount(): Int {
        return contents.size
    }

    fun setSpots(contents: List<Content>) {
        this.contents = contents
    }

    fun getSpots(): List<Content> {
        return contents
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val id: TextView = view.findViewById(R.id.id)
        var data: TextView = view.findViewById(R.id.data)
    }

}
