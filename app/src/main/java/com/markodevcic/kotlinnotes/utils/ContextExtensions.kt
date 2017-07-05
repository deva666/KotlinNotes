package com.markodevcic.kotlinnotes.utils

import android.app.Activity
import android.content.Context
import android.content.Intent

inline fun <reified T : Activity> Context.launchActivity() {
	val intent = Intent(this, T::class.java)
	this.startActivity(intent)
}

inline fun <reified T : Activity> Activity.launchActivityForResult(requestCode:Int) {
	val intent = Intent(this, T::class.java)
	this.startActivityForResult(intent, requestCode)
}