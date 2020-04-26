package com.example.noti

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.noti.database.NoteEntity
import com.example.noti.databinding.FragmentListNotesBinding
import com.example.noti.ListNotesFragmentDirections.Companion.actionListNotesFragmentToDetailNoteFragment2 as toDetailNoteFragment

/**
 * A simple [Fragment] subclass.
 */
class ListNotesFragment : Fragment() {
    //bind the fragment with the layout using data binding
    private lateinit var binding: FragmentListNotesBinding

    private val viewModel by lazy {
        ViewModelProvider(this).get(ListNoteViewModel::class.java)
    }

    // adapter used to attach recyclerview to the fragment and show it on screen
    //
    private val adapter by lazy {
        ListNoteAdapter { note: NoteEntity ->
            findNavController().navigate(toDetailNoteFragment(note))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentListNotesBinding.inflate(inflater)

        binding.listNoteViewModel = viewModel

        binding.lifecycleOwner = this

        //connect the recyclerview to the adapter
        binding.noteRecyclerView.adapter = adapter

        //viewModel.notes basically sais take all notes from the database and attach them to the adapter
        viewModel.notes.observe(viewLifecycleOwner, Observer(adapter::submitList))

        binding.addButton.setOnClickListener {

            findNavController().navigate(toDetailNoteFragment(NoteEntity()))
        }

        //Hide the FAB when scrolling down
        binding.noteRecyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if(dy > 0){
                    binding.addButton.hide()
                }else if(dy < 0){
                    binding.addButton.show()
                }
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    private fun deleteNotes() {
        viewModel.onClearClicked()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.del_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.delete_menu -> deleteNotes()
        }
        return super.onOptionsItemSelected(item)
    }
}
