package com.m4kvn.recyclerviewautoscroller.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.m4kvn.recyclerviewautoscroller.RecyclerViewAutoScroller

class MainActivity : AppCompatActivity() {
    private val adapter = MainAdapter()
    private val autoScroller = RecyclerViewAutoScroller(1000L, adapter.itemCount / 2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recycler = findViewById<RecyclerView>(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler.adapter = MainAdapter()
    }

    override fun onResume() {
        super.onResume()
        val recycler = findViewById<RecyclerView>(R.id.recycler)
        autoScroller.start(recycler)
    }

    override fun onPause() {
        super.onPause()
        autoScroller.stop()
    }
}