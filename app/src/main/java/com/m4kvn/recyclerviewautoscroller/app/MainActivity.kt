package com.m4kvn.recyclerviewautoscroller.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.m4kvn.recyclerviewautoscroller.RecyclerViewAutoScroller
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val adapter = MainAdapter()
    private val autoScroller =
        RecyclerViewAutoScroller(1000L, adapter.itemCount / 2)

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