package com.github.m4kvn.recyclerviewautoscroller

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.m4kvn.recyclerviewautoscroller.library.RecyclerViewAutoScroller
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_image.view.*

class MainActivity : AppCompatActivity() {
    private val adapter = MainAdapter()
    private val autoScroller = RecyclerViewAutoScroller(1000L, adapter.itemCount / 2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler.adapter = MainAdapter()
    }

    override fun onResume() {
        super.onResume()
        autoScroller.start(recycler)
    }

    override fun onPause() {
        super.onPause()
        autoScroller.stop()
    }
}

class MainAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ImageViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_image, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return 100
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? ImageViewHolder)?.apply {
            image.setBackgroundColor(color)
        }
    }

    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.image
        val color: Int = Color.parseColor(mutableListOf<String>()
            .apply { repeat(6) { add(patterns.random()) } }
            .joinToString(separator = "", prefix = "#"))

        companion object {
            private val patterns = arrayOf(
                "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"
            )
        }
    }
}
