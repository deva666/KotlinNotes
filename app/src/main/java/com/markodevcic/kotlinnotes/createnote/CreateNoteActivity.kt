package com.markodevcic.kotlinnotes.createnote

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.EditText
import com.markodevcic.kotlinnotes.R
import com.markodevcic.kotlinnotes.data.Note
import io.realm.Realm

class CreateNoteActivity : AppCompatActivity() {

	private lateinit var titleText: EditText
	private lateinit var contentText: EditText

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_create_note)
		val toolbar = findViewById(R.id.toolbar) as Toolbar
		setSupportActionBar(toolbar)
		titleText = findViewById(R.id.create_note_title_text) as EditText
		contentText = findViewById(R.id.create_note_content_text) as EditText

		val fab = findViewById(R.id.fab) as FloatingActionButton
		fab.setOnClickListener {
			saveNote()
			this.finish()
		}
		supportActionBar!!.setDisplayHomeAsUpEnabled(true)
	}

	private fun saveNote() {
		val realm = Realm.getDefaultInstance()
		val note = Note.newNote()
		note.title = titleText.text.toString()
		note.note = contentText.text.toString()
		realm.executeTransactionAsync { r ->
			r.copyToRealmOrUpdate(note)
		}
		realm.close()
	}
}
