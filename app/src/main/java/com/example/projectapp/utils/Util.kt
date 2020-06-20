package com.example.projectapp.utils

import android.content.Context
import android.content.SharedPreferences

const val AUTHENTICATED_SUCCESSFULLY = "Authenticated Successfully"
const val CHECK_YOUR_INFORMATION = "Please check your information !"
const val SERVER_CONNECTION_ERROR = "Unable to connect to the server !"
const val VERIFIED_SUCCESSFULLY = "Verified Successfully"
const val INVALID_VERIFICATION = "Invalid verification token"
const val REGISTERED_SUCCESSFULLY = "Registered Successfully"

const val IS_INTRO_SCREEN_OPENED_BEFORE = "isIntroOpened"

const val USER_ID = "id"
const val USER_NAME = "name"
const val USER_EMAIL = "email"
const val REMEMBER_ME = "rememberMe"

const val LOGIN_TAG = "login"
const val REGISTER_TAG = "register"

/**
 * When the user check the remember_me CheckBox then we store this prefs .
 */
fun addRememberMePrefs(context: Context) {
    //if the rememberMe checked ,then don't show RegisterFragment OR LoginFragment again.
    val sp: SharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
    val editor: SharedPreferences.Editor = sp.edit()
    editor.putBoolean(REMEMBER_ME, true)
    editor.apply()
}

fun removeRememberMePrefs(context: Context) {
    //remove the data stored in prefs if rememberMe checkBox was checked.
    val sp: SharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
    val editor: SharedPreferences.Editor = sp.edit()
    editor.remove(REMEMBER_ME)
    editor.apply()
}

private val PUNCTUATION = listOf(", ", "; ", ": ", " ")

/**
 * Truncate long text with a preference for word boundaries and without trailing punctuation.
 */
fun String.smartTruncate(length: Int): String {
    val words = split(" ")
    var added = 0
    var hasMore = false
    val builder = StringBuilder()
    for (word in words) {
        if (builder.length > length) {
            hasMore = true
            break
        }
        builder.append(word)
        builder.append(" ")
        added += 1
    }

    PUNCTUATION.map {
        if (builder.endsWith(it)) {
            builder.replace(builder.length - it.length, builder.length, "")
        }
    }

    if (hasMore) {
        builder.append("...")
    }
    return builder.toString()
}