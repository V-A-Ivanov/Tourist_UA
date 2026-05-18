package com.example.touristua

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class SimpleLocationAdapter(
    private val list: ArrayList<Location>,
    private val isCompact: Boolean = false,
    private val onClick: (Location) -> Unit
) : RecyclerView.Adapter<SimpleLocationAdapter.ViewHolder>() {

    class ViewHolder(view: View, compact: Boolean) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvCardName)
        val city: TextView = view.findViewById(R.id.tvCardCity)
        val description: TextView = view.findViewById(R.id.tvCardDescription)
        val image: ImageView? = if (!compact) view.findViewById(R.id.ivCardImage) else null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = if (isCompact) R.layout.item_location_compact else R.layout.item_location
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ViewHolder(view, isCompact)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val loc = list[position]

        holder.name.text = loc.name
        holder.city.text = "📍 ${loc.region}"
        holder.description.text = loc.description

        holder.image?.let { imageView ->
            if (loc.imageUrl.isNotEmpty()) {
                Glide.with(holder.itemView.context)
                    .load(loc.imageUrl)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.stat_notify_error)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .into(imageView)
            } else {
                imageView.setImageResource(android.R.drawable.ic_menu_gallery)
            }
        }

        holder.itemView.setOnClickListener { onClick(loc) }
    }

    override fun getItemCount() = list.size
}