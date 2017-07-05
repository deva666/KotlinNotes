package com.markodevcic.kotlinnotes.notes

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.markodevcic.kotlinnotes.R
import com.markodevcic.kotlinnotes.data.Note
import org.jetbrains.anko.*
import java.util.*

class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
	val titleText = itemView.find<TextView>(R.id.notes_item_title)
	val contentText = itemView.find<TextView>(R.id.notes_item_content)
	val dateText = itemView.find<TextView>(R.id.notes_item_date)
	val favorite = itemView.find<ImageView>(R.id.notes_item_favorite)

	fun bind(note: Note) {
		titleText.text = note.title
		contentText.text = note.note
		dateText.text = Date(note.createdAt).toString()
		if (note.isFavorite)
			favorite.visibility = View.VISIBLE
		else
			favorite.visibility = View.INVISIBLE
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
						textSize = 16f
						id = R.id.notes_item_title
						textColor = ContextCompat.getColor(ui.ctx, android.R.color.black)
					}.lparams(width = matchParent, height = wrapContent)

					imageView {
						id = R.id.notes_item_favorite
						imageResource = android.R.drawable.btn_star
					}.lparams {
						leftMargin = dip(12)
					}

					textView {
						id = R.id.notes_item_date
					}.lparams {
						leftMargin = dip(12)
					}
				}


				textView {
					textSize = 14f
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