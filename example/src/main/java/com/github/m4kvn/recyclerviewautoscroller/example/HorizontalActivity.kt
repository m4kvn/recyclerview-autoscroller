package com.github.m4kvn.recyclerviewautoscroller.example

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.m4kvn.recyclerviewautoscroller.RecyclerViewAutoScroller

class HorizontalActivity : AppCompatActivity() {
    private val horizontalAdapter = HorizontalAdapter()
    private val autoScroller = RecyclerViewAutoScroller(2000L, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horizontal)

        findViewById<RecyclerView>(R.id.recycler).apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = horizontalAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        autoScroller.start(findViewById(R.id.recycler))
    }

    override fun onPause() {
        super.onPause()
        autoScroller.stop()
    }

    companion object {

        fun start(activity: AppCompatActivity) {
            activity.startActivity(Intent(activity, HorizontalActivity::class.java))
        }
    }
}