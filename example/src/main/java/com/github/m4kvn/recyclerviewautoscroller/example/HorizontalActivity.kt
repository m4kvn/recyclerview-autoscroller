package com.github.m4kvn.recyclerviewautoscroller.example

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.m4kvn.recyclerviewautoscroller.RecyclerViewAutoScroller
import kotlinx.android.synthetic.main.activity_horizontal.*

class HorizontalActivity : AppCompatActivity() {
    private val adapters: Array<RecyclerView.Adapter<*>> = arrayOf(
        HorizontalAdapter(HorizontalAdapter.Type.WRAP),
        HorizontalAdapter(HorizontalAdapter.Type.BANNER),
        HorizontalAdapter(HorizontalAdapter.Type.FILL)
    )
    private val autoScroller: Array<RecyclerViewAutoScroller> = arrayOf(
        RecyclerViewAutoScroller(2000L, 0),
        RecyclerViewAutoScroller(3000L, adapters[1].itemCount / 2),
        RecyclerViewAutoScroller(5000L, 0)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horizontal)

        wrapRecycler.apply {
            adapter = adapters[0]
        }

        bannerRecycler.apply {
            adapter = adapters[1]
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    val x1 = resources.getDimensionPixelOffset(R.dimen.x1)
                    outRect.set(x1 / 2, 0, x1 / 2, 0)
                }
            })
        }

        fillRecycler.apply {
            adapter = adapters[2]
        }
    }

    override fun onResume() {
        super.onResume()
        autoScroller[0].start(wrapRecycler)
        autoScroller[1].start(bannerRecycler)
        autoScroller[2].start(fillRecycler)
    }

    override fun onPause() {
        super.onPause()
        autoScroller.forEach { it.stop() }
    }

    companion object {

        fun start(activity: AppCompatActivity) {
            activity.startActivity(Intent(activity, HorizontalActivity::class.java))
        }
    }
}