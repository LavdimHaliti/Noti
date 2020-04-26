package com.example.noti

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.noti.database.NoteEntity
import com.example.noti.databinding.NoteItemBinding

/**
 * @ListNoteAdapter is the adapter class used as a bridge between the UI and the recyclerView
 * @param onItemClicked is used to enable the items in the recyclerview to be clickable
 * extends ListAdapter.
 */
class ListNoteAdapter(private val onItemClicked: (NoteEntity) -> Unit) : androidx.recyclerview.widget.ListAdapter<NoteEntity , ListNoteAdapter.ViewHolder>(NoteDiffCallBack()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClicked)

    }

    class ViewHolder private constructor(private val binding: NoteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(note: NoteEntity, onItemClicked: ((NoteEntity) -> Unit)){
            binding.note = note
            binding.root.setOnClickListener {
                onItemClicked(note)
            }


        }

        companion object{
            fun from(parent: ViewGroup): ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = NoteItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }


}

class NoteDiffCallBack : DiffUtil.ItemCallback<NoteEntity>(){
    override fun areItemsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
        return oldItem == newItem
    }

}