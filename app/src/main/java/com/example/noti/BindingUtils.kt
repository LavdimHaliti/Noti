package com.example.noti

import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.noti.database.NoteEntity

/**
 * @BindingAdapter is used with data binding, in this case to link the database with the layout.
 * @param name  Take it from NoteEntity and using `nameText` implement it in xml layout in order to show it to the screen
 */
@BindingAdapter("nameText")
fun TextView.setNameText(item: NoteEntity?){
    item?.let {
        text = item.name
    }
}