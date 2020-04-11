package com.example.projectapp.domain

/**
 * Domain objects are plain Kotlin data classes that represent the things in our app. These are the
 * objects that should be displayed on screen, or manipulated by the app.
 *
 * @see database for objects that are mapped to the database
 * @see network for objects that parse or prepare network calls
 */

data class User(
        val id: String,
        val name: String,
        val email: String,
        //val password: String,
        val verificationToken: String?,
        val userVerification: String,
        val accountType: String,
        val userType: String
)

enum class UserType {
    REGULAR, ADMIN
}

enum class UserVerification {
    VERIFIED, UNVERIFIED
}

enum class AccountType {
    FACEBOOK_ACCOUNT, GOOGLE_ACCOUNT, REGULAR_ACCOUNT
}