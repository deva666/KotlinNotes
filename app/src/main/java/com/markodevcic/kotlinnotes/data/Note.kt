package com.markodevcic.kotlinnotes.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Note : RealmObject() {

	@PrimaryKey
	var id: String = ""

	var note: String = ""

	var title: String = ""

	var createdAt: Long = 0L

	var isFavorite: Boolean = false

	companion object {
		fun newNote(): Note =  Note().apply {
			id = UUID.randomUUID().toString()
			createdAt = Date().time
		}
	}
}