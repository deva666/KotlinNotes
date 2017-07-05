package com.markodevcic.kotlinnotes.notes

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.markodevcic.kotlinnotes.data.Note
import org.jetbrains.anko.AnkoContext

class NotesAdapter (private var notes: List<Note>) : RecyclerView.Adapter<NotesViewHolder>() {

	override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
		val note = notes[position]
		holder.bind(note)
	}

	override fun getItemCount(): Int {
		return notes.size
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
		return NotesViewHolder(NotesUi().createView(AnkoContext.create(parent.context, parent)))
	}

	fun onNotesChanging(newNotes: List<Note>) {
		notes = newNotes
		notifyDataSetChanged()
	}
}