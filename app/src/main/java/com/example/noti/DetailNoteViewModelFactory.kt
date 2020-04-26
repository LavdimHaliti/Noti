package com.example.noti

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.noti.database.NoteEntity

/**
 * @DetailNoteNiewModelFactory is a class where we create new ViewModels by extending
 * @ViewModelProvider.Factory, which means that ViewModelProvider knows how to create a new ViewModel
 * Inside constructor we have
 * @param note and
 * @param application
 */

class DetailNoteViewModelFactory(private val note: NoteEntity, private val application: Application)  :
    ViewModelProvider.Factory{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailNoteViewModel::class.java)) {
            return DetailNoteViewModel(note, application) as T
        }
        throw IllegalArgumentException("I don't know how to create this viewModel")
    }
}