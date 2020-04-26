package com.example.noti.database

import android.os.Parcelable
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import kotlinx.android.parcel.Parcelize
/**

* NoteEntity is a data class that creates a table from the database to use by including it's content
* @param id: String
* @param name: String
* @param content: String
* In order for the database to be able to use this table we need to  specify it as an @Entity(tablename = "note_table")
* and we extend the Parcelable Interface in order to pass data between destination. Also e need to annotate it as @Parcelize.
 */
@Parcelize
@Entity(tableName = "note_table")
data class NoteEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "content")
    var content: String = ""
) : Parcelable