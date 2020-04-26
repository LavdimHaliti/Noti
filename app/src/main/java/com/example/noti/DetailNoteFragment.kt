package com.example.noti

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.core.view.isInvisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.noti.database.NoteDatabase
import com.example.noti.database.NoteEntity
import com.example.noti.databinding.FragmentDetailNoteBinding

/**
 * A simple [Fragment] subclass.
 */
class DetailNoteFragment : Fragment() {

    /**
     * @param binding : FragmentDetailNoteBinding is used to connect this class with the xml layout file
     */
    private lateinit var binding: FragmentDetailNoteBinding

    /**
     * @param noteArgument get the arguments from ListNotesFragment that are taken from NoteEntity()
     * by lazy basically sais im not going to do anything until you call me
     */
    val noteArgument by lazy {
        DetailNoteFragmentArgs.fromBundle(arguments!!).note
    }

    /**
     * @DetailNoteViewModelFactory takes as arguments
     * @noteArguments and an application
     */
    val detailNoteViewModelFactory by lazy {
        DetailNoteViewModelFactory(noteArgument, requireActivity().application)
    }

    /**
     * Now ViewModelProvider takes ViewModelFactory and shows it how to create a new ViewModel, in this case DetailNoteViewModel
     */
    val detailNoteViewModel by lazy {
        ViewModelProvider(this, detailNoteViewModelFactory).get(DetailNoteViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailNoteBinding.inflate(inflater)

        //connect the viewmodel with the lyout using data binding
        binding.detailNoteViewModel = detailNoteViewModel

        //Navigate to ListNoteFragment
        detailNoteViewModel.navigateToListNoteFragment.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                detailNoteViewModel.doneNavigating()
                findNavController().navigateUp()
            }
        })

        //save the note by assigning the editText and multilineEditText to note.name and note.content
        fun saveNote() {
            val note = NoteEntity()
            note.name = binding.nameEditText.text.toString()
            note.content = binding.contentEditText.text.toString()
            detailNoteViewModel.saveNote(note)
        }

        //delete the current note
        fun deleteNote() {
            val note = NoteEntity()
            detailNoteViewModel.deleteNote(note)
        }

        //close the keyboard when pressing save item in the bottom navigation
        fun closeKeyboard(){
            val view = activity?.currentFocus
            if(view != null){
                val hideKeyboard = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                hideKeyboard.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        //Add or delete the current note using Bottom navigation
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.save_note -> if (binding.nameEditText.text.isEmpty()) {
                    binding.nameEditText.error = "Please enter your subject!"
                }else {
                    closeKeyboard()
                    saveNote()
                }
                R.id.delete_note -> deleteNote()
            }
            true
        }



        setHasOptionsMenu(true)

        return binding.root
    }

    private fun shareSuccess(){
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, noteArgument.content)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, "share via")
        startActivity(shareIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.share_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.share_menu -> shareSuccess()
        }
        return super.onOptionsItemSelected(item)
    }


}
