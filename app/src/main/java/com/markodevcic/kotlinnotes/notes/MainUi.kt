package com.markodevcic.kotlinnotes.notes

import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import com.markodevcic.kotlinnotes.R
import com.markodevcic.kotlinnotes.createnote.CreateNoteActivity
import com.markodevcic.kotlinnotes.data.Note
import com.markodevcic.kotlinnotes.utils.launchActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.floatingActionButton
import org.jetbrains.anko.recyclerview.v7.recyclerView

class MainUi(private val notesAdapter: NotesAdapter) : AnkoComponent<MainActivity> {

	override fun createView(ui: AnkoContext<MainActivity>): View {
		return with(ui) {
			coordinatorLayout {
				fitsSystemWindows = true
				appBarLayout {
					toolbar {
						popupTheme = R.style.AppTheme_PopupOverlay
						id = R.id.main_toolbar
						backgroundResource = R.color.colorPrimary
						setTitleTextColor(ContextCompat.getColor(ui.ctx, R.color.primary_text_default_material_dark))
						title = "Notes"
					}.lparams(width = matchParent) {
						val tv = TypedValue()
						if (ui.owner.theme.resolveAttribute(R.attr.actionBarSize, tv, true)) {
							height = TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics);
						}
					}
				}.lparams(width = matchParent, height = wrapContent)

				scrollView {
					recyclerView {
						layoutManager = LinearLayoutManager(ui.owner)
						adapter = notesAdapter
					}
				}.lparams(width = matchParent, height = matchParent) {
					behavior = AppBarLayout.ScrollingViewBehavior()
				}

				floatingActionButton {
					imageResource = android.R.drawable.ic_input_add
					backgroundColor = ContextCompat.getColor(ui.owner, R.color.colorAccent)
					setOnClickListener {
						ui.owner.launchActivity<CreateNoteActivity>()
					}
				}.lparams {
					margin = resources.getDimensionPixelSize(R.dimen.fab_margin)
					gravity = Gravity.BOTTOM or GravityCompat.END
				}
			}
		}
	}

	fun onNotesChanging(notes: List<Note>) {
		notesAdapter.onNotesChanging(notes)
	}
}
