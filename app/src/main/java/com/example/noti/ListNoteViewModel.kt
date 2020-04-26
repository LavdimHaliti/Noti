package com.example.noti

import android.app.Application
import androidx.lifecycle.*
import com.example.noti.database.NoteDatabase
import com.example.noti.database.NoteEntity
import kotlinx.coroutines.*

class ListNoteViewModel(app: Application) : AndroidViewModel(app) {

    //Get the noteDao from NoteDatabase
    private val noteDao = NoteDatabase.getInstance(app).noteDao

    private var thisNote = MutableLiveData<NoteEntity?>()

    val notes = noteDao.getAllNotes()

    private suspend fun clearNotes(){
        withContext(Dispatchers.IO){
            noteDao.deleteAllNotes()
        }
    }

    fun onClearClicked(){
        viewModelScope.launch {
            //clear the database table
            clearNotes()

            //clear this note since is no longer in the database
           thisNote.value = null
        }
    }

}