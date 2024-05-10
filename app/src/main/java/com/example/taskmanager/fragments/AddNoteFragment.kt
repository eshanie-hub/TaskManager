package com.example.taskmanager.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import com.example.taskmanager.MainActivity
import com.example.taskmanager.R
import com.example.taskmanager.databinding.FragmentAddNoteBinding
import com.example.taskmanager.model.Note
import com.example.taskmanager.viewmodel.NoteViewModel
import android.widget.RadioButton
import android.widget.RadioGroup


class AddNoteFragment : Fragment(R.layout.fragment_add_note), MenuProvider {
    private lateinit var selectedPriority: String
    private var addNoteBinding: FragmentAddNoteBinding? = null
    private val binding get() = addNoteBinding!!

    private lateinit var notesViewModel: NoteViewModel
    private lateinit var addNoteView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        addNoteBinding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        notesViewModel = (activity as MainActivity).noteViewModel
        addNoteView = view

        val radioLow = view.findViewById<RadioButton>(R.id.radioLow)
        val radioMedium = view.findViewById<RadioButton>(R.id.radioMedium)
        val radioHigh = view.findViewById<RadioButton>(R.id.radioHigh)

        radioLow.setOnClickListener {
            selectedPriority = "Low"
        }

        radioMedium.setOnClickListener {
            selectedPriority = "Medium"
        }

        radioHigh.setOnClickListener {
            selectedPriority = "High"
        }

    }

    private fun saveNote() {
        val noteTitle = binding.addNoteTitle.text.toString().trim()
        val noteDesc = binding.addNoteDesc.text.toString().trim()
        val noteDate = binding.addNoteDate.text.toString().trim()
        val priority = selectedPriority


        if (noteTitle.isNotEmpty()) {
            val note = Note(0, noteTitle, noteDesc, noteDate, priority)
            notesViewModel.addNote(note)

            Toast.makeText(addNoteView.context, "Note Saved", Toast.LENGTH_SHORT).show()
            addNoteView.findNavController().popBackStack(R.id.homeFragment, false)

        } else {
            Toast.makeText(addNoteView.context, "Please enter note title", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_add_note, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.saveMenu -> {
                saveNote()
                true
            }
            else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        addNoteBinding = null
    }
}
