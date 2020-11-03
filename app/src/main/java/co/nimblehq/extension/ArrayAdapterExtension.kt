package co.nimblehq.extension

import android.widget.ArrayAdapter

fun <T> ArrayAdapter<T>.refreshWithData(list: List<T>) {
    clear()
    addAll(list)
    notifyDataSetChanged()
}
