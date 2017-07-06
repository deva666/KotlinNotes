package com.markodevcic.kotlinnotes.notes

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.RadioGroup
import android.widget.Toolbar
import com.markodevcic.kotlinnotes.R
import com.markodevcic.kotlinnotes.data.Note
import io.realm.Realm
import io.realm.RealmResults
import org.jetbrains.anko.*

class MainActivity : AppCompatActivity() {

	private lateinit var realm: Realm
	private lateinit var mainUi: MainUi
	private lateinit var notes: RealmResults<Note>

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		mainUi = MainUi(NotesAdapter(listOf()))
		mainUi.setContentView(this)

		val toolbar = find<Toolbar>(R.id.main_toolbar)
		setActionBar(toolbar)

		realm = Realm.getDefaultInstance()
		notes = realm.where(Note::class.java).findAllAsync()
		notes.addChangeListener { newNotes, _ ->
			mainUi.onNotesChanging(newNotes)
		}
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.menu_main, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		val id = item.itemId

		if (id == R.id.action_filter) {
			alert {
				customView {
					radioGroup {
						orientation = RadioGroup.VERTICAL
						radioButton {
							text = "Favorites"
							setOnClickListener {
								notes.removeAllChangeListeners()
								notes = realm.where(Note::class.java)
										.equalTo("isFavorite", true)
										.findAllAsync()
								notes.addChangeListener { newNotes, _ ->
									mainUi.onNotesChanging(newNotes)
								}
							}
						}
						radioButton {
							text = "All"
							setOnClickListener {
								notes.removeAllChangeListeners()
								notes = realm.where(Note::class.java).findAllAsync()
								notes.addChangeListener { newNotes, _ ->
									mainUi.onNotesChanging(newNotes)
								}
							}
						}
					}
				}
			}.show()
			return true
		}

		return super.onOptionsItemSelected(item)
	}

	override fun onDestroy() {
		super.onDestroy()
		notes.removeAllChangeListeners()
		realm.close()
	}
}
