package com.markodevcic.kotlinnotes.notes

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.markodevcic.kotlinnotes.R
import com.markodevcic.kotlinnotes.data.Note
import io.realm.Realm
import io.realm.RealmResults
import org.jetbrains.anko.find
import org.jetbrains.anko.setContentView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

	private lateinit var realm: Realm
	private lateinit var mainUi: MainUi
	private lateinit var notes: RealmResults<Note>
	private lateinit var drawerLayout: DrawerLayout
	private lateinit var notesAdapter: NotesAdapter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		realm = Realm.getDefaultInstance()
		notes = realm.where(Note::class.java).findAllAsync()
		notesAdapter = NotesAdapter(notes, true)
		mainUi = MainUi(notesAdapter)
		mainUi.setContentView(this)

		drawerLayout = find<DrawerLayout>(R.id.drawer_layout)
		val toolbar = find<android.support.v7.widget.Toolbar>(R.id.main_toolbar)
		setSupportActionBar(toolbar)

		val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
				R.string.app_name, R.string.app_name)
		drawerLayout.addDrawerListener(toggle)
		toggle.syncState()

		val areNotesEmpty = realm.where(Note::class.java)
				.count() == 0L
		if (areNotesEmpty) {
			find<TextView>(R.id.no_items_text).visibility = View.VISIBLE
		}
	}

	override fun onDestroy() {
		super.onDestroy()
		realm.close()
	}

	override fun onBackPressed() {
		if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
			drawerLayout.closeDrawer(GravityCompat.START)
		} else {
			super.onBackPressed()
		}
	}

	override fun onNavigationItemSelected(item: MenuItem): Boolean {
		val results = when (item.itemId) {
			R.id.menu_filder_pending -> realm.where(Note::class.java).equalTo("isDone", false).findAllAsync()
			R.id.menu_filter_favorites -> realm.where(Note::class.java).equalTo("isDone", false).equalTo("isFavorite", true).findAllAsync()
			R.id.menu_filter_done -> realm.where(Note::class.java).equalTo("isDone", true).findAllAsync()
			R.id.menu_filter_all -> realm.where(Note::class.java).findAllAsync()
			else -> throw IllegalArgumentException("unknown ID")
		}
		supportActionBar?.title = item.title
		notesAdapter.onResultsChanged(results)
		drawerLayout.closeDrawer(GravityCompat.START)
		return true
	}
}
