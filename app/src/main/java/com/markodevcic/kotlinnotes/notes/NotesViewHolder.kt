package com.markodevcic.kotlinnotes.notes

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.Spanned
import android.text.TextUtils
import android.text.style.StrikethroughSpan
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.markodevcic.kotlinnotes.R
import com.markodevcic.kotlinnotes.data.Note
import io.realm.Realm
import org.jetbrains.anko.*
import java.text.DateFormat
import java.util.*

class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
	private val titleText = itemView.find<TextView>(R.id.notes_item_title)
	private val contentText = itemView.find<TextView>(R.id.notes_item_content)
	private val dateText = itemView.find<TextView>(R.id.notes_item_date)
	private val favorite = itemView.find<ImageView>(R.id.notes_item_favorite)
	private val menuButton = itemView.find<ImageButton>(R.id.notes_item_menu)
	private var isExpanded = false

	fun bind(note: Note) {
		contentText.setOnClickListener {
			if (isExpanded) {
				contentText.maxLines = 1
			} else {
				contentText.maxLines = Int.MAX_VALUE
			}
			isExpanded = !isExpanded
		}
		menuButton.setOnClickListener { view ->
			val popupMenu = PopupMenu(view.context, view)
			popupMenu.setOnMenuItemClickListener { item ->
				when (item.itemId) {
					R.id.action_favorite -> {
						toggleIsFavorite(note.id)
						true
					}
					R.id.action_done -> {
						toggleIsDone(note.id)
						true
					}
					else -> false
				}
			}
			popupMenu.inflate(R.menu.menu_item_note)
			popupMenu.show()
		}

		if (note.isDone) {
			setSpan(titleText, note.title)
			setSpan(contentText, note.note)
		} else {
			titleText.text = note.title
			contentText.text = note.note
		}
		val formatter = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault())
		dateText.text = formatter.format(Date(note.createdAt))
		if (note.isFavorite) {
			favorite.visibility = View.VISIBLE
		} else {
			favorite.visibility = View.INVISIBLE
		}
	}

	private fun toggleIsFavorite(id: String) {
		Realm.getDefaultInstance().use { realm ->
			realm.executeTransactionAsync {
				val realmNote = it.where(Note::class.java)
						.equalTo("id", id)
						.findFirst()
				realmNote.isFavorite = !realmNote.isFavorite
			}
		}
	}

	private fun toggleIsDone(id: String) {
		Realm.getDefaultInstance().use { realm ->
			realm.executeTransactionAsync {
				val realmNote = it.where(Note::class.java)
						.equalTo("id", id)
						.findFirst()
				realmNote.isDone = !realmNote.isDone
			}
		}
	}

	private fun setSpan(textView: TextView, text: String) {
		textView.setText(text, TextView.BufferType.SPANNABLE)
		val spannable = textView.text as Spannable
		spannable.setSpan(StrikethroughSpan(), 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
	}
}

class NotesUi : AnkoComponent<ViewGroup> {
	override fun createView(ui: AnkoContext<ViewGroup>): View {
		return with(ui) {
			linearLayout {
				lparams(width = matchParent, height = wrapContent)
				orientation = LinearLayout.VERTICAL
				padding = dip(12)
				linearLayout {
					lparams(width = matchParent, height = wrapContent)
					orientation = LinearLayout.HORIZONTAL
					textView {
						textSize = 20f
						id = R.id.notes_item_title
						textColor = ContextCompat.getColor(ui.ctx, android.R.color.black)
					}.lparams(width = wrapContent, height = wrapContent)

					imageView {
						id = R.id.notes_item_favorite
						imageResource = R.drawable.ic_star_black
					}.lparams {
						leftMargin = dip(12)
						width = 35
						height = 35
					}

					textView {
						id = R.id.notes_item_date
					}.lparams {
						leftMargin = dip(12)
					}

					linearLayout {
						gravity = Gravity.END

						imageButton {
							id = R.id.notes_item_menu
							imageResource = R.drawable.ic_action_more_vert
							backgroundColor = ContextCompat.getColor(ui.ctx, android.R.color.transparent)
						}.lparams(width = dip(24), height = dip(24)) {
							margin = 0
							padding = 0
						}
					}.lparams(width = matchParent) {
						margin = 0
						padding = 0
					}
				}

				textView {
					textSize = 16f
					id = R.id.notes_item_content
					maxLines = 1
					ellipsize = TextUtils.TruncateAt.END
				}.lparams(width = wrapContent) {
					topMargin = dip(6)
				}

			}
		}
	}

}