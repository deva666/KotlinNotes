package com.markodevcic.kotlinnotes.viewnote

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.markodevcic.kotlinnotes.R

class ViewNoteActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_view_note)
		val toolbar = findViewById(R.id.toolbar) as Toolbar
		setSupportActionBar(toolbar)

		val noteId = intent.getStringExtra(KEY_NOTE_ID)
		if (noteId != null) {

		} else {
			throw IllegalStateException("note id expected in extras")
		}

		val fab = findViewById(R.id.fab) as FloatingActionButton
		fab.setOnClickListener { view ->
			Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
					.setAction("Action", null).show()
		}
		supportActionBar!!.setDisplayHomeAsUpEnabled(true)
	}

	companion object {
		const val KEY_NOTE_ID = "KEY_NOTE_ID"
	}
}
