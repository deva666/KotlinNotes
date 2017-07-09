package com.markodevcic.kotlinnotes.viewnote

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.TextView
import com.markodevcic.kotlinnotes.R
import com.markodevcic.kotlinnotes.data.Note
import com.markodevcic.kotlinnotes.utils.find
import io.realm.Realm
import java.text.DateFormat
import java.util.*

class ViewNoteActivity : AppCompatActivity() {

	private lateinit var realm: Realm
	private lateinit var noteTitle: TextView
	private lateinit var noteContent: TextView
	private lateinit var noteResult: Note

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_view_note)
		val toolbar = find<Toolbar>(R.id.toolbar)
		setSupportActionBar(toolbar)

		noteTitle = find<TextView>(R.id.activity_view_note_title)
		noteContent = find<TextView>(R.id.activity_view_note_content)

		realm = Realm.getDefaultInstance()

		val fab = find<FloatingActionButton>(R.id.fab)
		fab.setOnClickListener {
			if (noteResult.isLoaded) {
				realm.executeTransaction { r ->
					noteResult.isFavorite = !noteResult.isFavorite
				}
			}
		}

		val noteId = intent.getStringExtra(KEY_NOTE_ID)
		if (noteId != null) {
			noteResult = realm.where(Note::class.java)
					.equalTo("id", noteId)
					.findFirstAsync()

			noteResult.addChangeListener<Note> { note, _ ->
				if (note == null) {
					throw IllegalStateException("note with ID: $noteId not found")
				}
				val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault())
				noteTitle.text = "Taken at ${dateFormat.format(Date(note.createdAt))}"
				noteContent.text = note.note
				toolbar.title = note.title
				val drawable = if (note.isFavorite) R.drawable.ic_favorite_white
				else R.drawable.ic_favorite_border_white
				fab.setImageDrawable(ContextCompat.getDrawable(this, drawable))
			}
		} else {
			throw IllegalStateException("note id expected in extras")
		}


		supportActionBar?.setDisplayHomeAsUpEnabled(true)
	}

	override fun onDestroy() {
		super.onDestroy()
		noteResult.removeAllChangeListeners()
		realm.close()
	}

	companion object {
		const val KEY_NOTE_ID = "KEY_NOTE_ID"
	}
}
