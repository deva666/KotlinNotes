package com.markodevcic.kotlinnotes.notes

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
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

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		realm = Realm.getDefaultInstance()
		notes = realm.where(Note::class.java).findAllAsync()
		mainUi = MainUi(NotesAdapter(notes, true))
		mainUi.setContentView(this)

		drawerLayout = find<DrawerLayout>(R.id.drawer_layout)
		val toolbar = find<android.support.v7.widget.Toolbar>(R.id.main_toolbar)
		setSupportActionBar(toolbar)

		val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
				R.string.app_name, R.string.app_name)
		drawerLayout.addDrawerListener(toggle)
		toggle.syncState()
	}

	override fun onDestroy() {
		super.onDestroy()
		notes.removeAllChangeListeners()
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

		drawerLayout.closeDrawer(GravityCompat.START)
		return true
	}
}
