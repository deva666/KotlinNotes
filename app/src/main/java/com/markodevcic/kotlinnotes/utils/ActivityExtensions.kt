@file:Suppress("UNCHECKED_CAST")

package com.markodevcic.kotlinnotes.utils

import android.app.Activity
import android.view.View

fun <T : View> Activity.find(id: Int): T = this.findViewById(id) as T