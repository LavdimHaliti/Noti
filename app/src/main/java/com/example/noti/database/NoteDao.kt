package com.example.noti.database

import androidx.lifecycle.LiveData
import androidx.room.*
/**
 * NoteDao (Data Access Object) has the job to access the data from the database and do work with that data by
 * implementing necessary annotations, @Insert, @Update, @Delete, @Query and @Dao.
 * Dao it has to be an interface or an abstract class
 */
@Dao
interface NoteDao {

    //Take the NoteEntity table and insert data in
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: NoteEntity)

    //Take the NoteEntity table and update the existing data
    @Update
    fun update(note: NoteEntity)

    //Delete a data drom the NoteEntity table
    @Delete
    fun delete(note: NoteEntity)

    //Delete everything from NoteEntity table
    @Query("DELETE FROM note_table")
    fun deleteAllNotes()

    //Get the id of a single row in the NoteEntity table
    @Query("SELECT * FROM note_table WHERE id = :noteId")
    fun get(noteId: Long): NoteEntity

    //Get all data from the NoteEntity table by putting in a List and observe any changes using LiveData
    @Query("SELECT * FROM note_table")
    fun getAllNotes(): LiveData<List<NoteEntity>>

    //Get a single note from NoteEntity table
    @Query("SELECT * FROM note_table LIMIT 1")
    fun getNote(): NoteEntity


}