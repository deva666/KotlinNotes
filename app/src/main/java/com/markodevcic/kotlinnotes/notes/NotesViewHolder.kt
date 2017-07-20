package com.markodevcic.kotlinnotes.notes

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.Spanned
import android.text.TextUtils
import android.text.style.StrikethroughSpan
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
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
	private val buttonHost = itemView.find<LinearLayout>(R.id.button_host)
	private val btnFavorite = itemView.find<ImageButton>(R.id.notes_item_btn_favorite)
	private val btnDone = itemView.find<ImageButton>(R.id.notes_item_btn_done)
	private var isExpanded = false

	fun bind(note: Note) {
		itemView.setOnClickListener {
			if (isExpanded) {
				contentText.maxLines = 1
				buttonHost.visibility = View.GONE
			} else {
				contentText.maxLines = Int.MAX_VALUE
				buttonHost.visibility = View.VISIBLE
			}
			isExpanded = !isExpanded
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
		if (note.isFavorite)
			favorite.visibility = View.VISIBLE
		else
			favorite.visibility = View.INVISIBLE
		buttonHost.visibility = if (isExpanded) View.VISIBLE else View.GONE

		btnFavorite.setOnClickListener {
			val realm = Realm.getDefaultInstance()
			val id = note.id
			realm.executeTransactionAsync {
				val realmNote = it.where(Note::class.java)
						.equalTo("id", id)
						.findFirst()
				realmNote.isFavorite = !realmNote.isFavorite
			}
			realm.close()
		}

		btnDone.setOnClickListener {
			Realm.getDefaultInstance().use { r ->
				val id = note.id
				r.executeTransactionAsync {
					val realmNote = it.where(Note::class.java)
							.equalTo("id", id)
							.findFirst()

					realmNote.isDone = !realmNote.isDone
				}
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
				}


				textView {
					textSize = 16f
					id = R.id.notes_item_content
					maxLines = 1
					ellipsize = TextUtils.TruncateAt.END
				}.lparams(width = wrapContent) {
					topMargin = dip(6)
				}

				linearLayout {
					id = R.id.button_host
					lparams {
						topMargin = dip(18)
						leftMargin = dip(18)
					}
					orientation = LinearLayout.HORIZONTAL

					imageButton {
						id = R.id.notes_item_btn_favorite
						imageResource = R.drawable.ic_star_white
						backgroundColor = ContextCompat.getColor(ui.ctx, R.color.colorAccent)
					}.lparams(width = dip(42), height = dip(42)) {
						bottomMargin = dip(6)
					}

					imageButton {
						id = R.id.notes_item_btn_done
						imageResource = R.drawable.ic_done_white
						backgroundColor = ContextCompat.getColor(ui.ctx, R.color.colorAccent)
					}.lparams(width = dip(42), height = dip(42)) {
						leftMargin = dip(24)
						bottomMargin = dip(6)
					}
				}
			}
		}
	}

}