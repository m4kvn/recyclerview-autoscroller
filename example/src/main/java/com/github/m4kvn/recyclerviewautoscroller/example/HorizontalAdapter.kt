package com.github.m4kvn.recyclerviewautoscroller.example

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HorizontalAdapter(
    private val type: Type
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    enum class Type { FILL, BANNER, WRAP }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ImageViewHolder(
            LayoutInflater.from(parent.context).inflate(
                when (type) {
                    Type.FILL -> R.layout.view_image_full
                    Type.BANNER -> R.layout.view_image_banner
                    Type.WRAP -> R.layout.view_image_wrap
                }, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? ImageViewHolder)?.apply {
            positionText.text = "$position"
            image.setBackgroundColor(color)
        }
    }

    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val positionText: TextView = view.findViewById(R.id.position)
        val image: ImageView = view.findViewById(R.id.image)
        val color = Util.getRandomColor()
    }
}