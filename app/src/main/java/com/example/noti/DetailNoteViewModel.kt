package com.example.noti

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.noti.database.NoteDatabase
import com.example.noti.database.NoteEntity
import kotlinx.coroutines.*

/**
 * ViewModel for DetailNoteFragment
 *
 * @param note : NoteEntity. Take a note from NoteEntity
 * @param app : Application.
 *
 *
 */

class DetailNoteViewModel(val note: NoteEntity, app: Application) : AndroidViewModel(app) {

    //Get the noteDao from NoteDatabase
    val noteDao = NoteDatabase.getInstance(app).noteDao

    //Get the current note and assign it to a variable
    private val currentNote = MutableLiveData<NoteEntity?>()

    //Assign the note id to a boolean where it will specify if a new note is created or not
    private val isNewNote: Boolean = note.id == 0L

    /**
     * @param _navigateToListNoteFragment
     * @param navigateToListNoteFragment
     *
     * Navigate to another fragment using LiveData as an Observer
     */
    private val _navigateToListNoteFragment = MutableLiveData<Boolean>()

    val navigateToListNoteFragment: LiveData<Boolean>
        get() = _navigateToListNoteFragment

    fun doneNavigating() {
        _navigateToListNoteFragment.value = false
    }

    /**As soon as the application starts take the existing note/s get it from the database.
     * @viewModelScope is the same as declaring `private val viewModelJob = Job()` and
     * `private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)` but simplified.
     */


    init {
        initializeNote()
    }

    private fun initializeNote() {
        viewModelScope.launch {
            currentNote.value = getNoteFromDataBase()
        }
    }

    /**
     * All the long running work needs to happen in different threads not in the main thread.
     * This is the case also with the database, so
     * @getNoteFromDataBase()
     * @insertNote()
     * @updateNote()
     * @delete() methods need to be declared as suspend functions in order to work inside of a Coroutine.
     * @withContext(Dispatchers.IO) inside of this method we implement the methods that will be working in different threads.
     */

    private suspend fun getNoteFromDataBase(): NoteEntity? {
        return withContext(Dispatchers.IO) {
            var note = noteDao.getNote()

            note
        }

    }

    private suspend fun insertNote(note: NoteEntity) {
        withContext(Dispatchers.IO) {
            noteDao.insert(note)
        }
    }

    private suspend fun updateNote(note: NoteEntity) {
        withContext(Dispatchers.IO) {
            noteDao.update(note)
        }
    }

    private suspend fun delete(note: NoteEntity) {
        withContext(Dispatchers.IO) {
            noteDao.delete(note)
        }
    }

    //if we want to create a new note then we inserNote(), else if it's an existing note updateNote()
    fun saveNote(note: NoteEntity) {
        viewModelScope.launch {
            if (isNewNote) {
                insertNote(note)
            }else{
                note.id = this@DetailNoteViewModel.note.id
                updateNote(note)
            }
        }
    }

    //If there is any existing note and we want to delete it then deleteNote()
    fun deleteNote(note: NoteEntity){
        viewModelScope.launch {
                note.id = this@DetailNoteViewModel.note.id
                delete(note)
                _navigateToListNoteFragment.value= true
        }
    }
}

