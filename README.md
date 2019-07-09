# recyclerview-autoscroller

recyclerview-autoscroll is a simple library to auto scroll RecyclerView.

<img src="./images/image_horizontal.gif" height="400" />

## Try it out:

```gradle
implementation "com.m4kvn:recyclerview-autoscroller:0.1.0"
```

### Get started

When use in activity and fragment, call `start` in onResume and call `stop` in onStop.

```kotlin
val initialPosition = adapter.itemCount / 2
val autoScroller = RecyclerViewAutoScroller(1000L, initialPosition)

override fun onResume() {
    super.onResume()
    autoScroller.start(recyclerView)
}

override fun onPause() {
    super.onPause()
    autoScroller.stop()
}
```
