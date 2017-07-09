package com.markodevcic.kotlinnotes.createnote

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.EditText
import com.markodevcic.kotlinnotes.R
import com.markodevcic.kotlinnotes.data.Note
import com.markodevcic.kotlinnotes.utils.find
import io.realm.Realm

class CreateNoteActivity : AppCompatActivity() {

	private lateinit var titleText: EditText
	private lateinit var contentText: EditText

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_create_note)
		val toolbar = find<Toolbar>(R.id.toolbar)
		setSupportActionBar(toolbar)
		titleText = find<EditText>(R.id.create_note_title_text)
		contentText = find<EditText>(R.id.create_note_content_text)

		val fab = find<FloatingActionButton>(R.id.fab)
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
