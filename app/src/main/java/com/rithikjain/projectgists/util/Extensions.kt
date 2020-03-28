package com.rithikjain.projectgists.util

import android.content.Context
import android.widget.Toast

fun Context.shortToast(string: String) {
    Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
}

fun Context.longToast(string: String) {
    Toast.makeText(this, string, Toast.LENGTH_LONG).show()
}