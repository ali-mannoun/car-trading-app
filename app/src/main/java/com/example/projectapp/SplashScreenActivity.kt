package com.example.projectapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View

class SplashScreenActivity : AppCompatActivity() {
    val SPLASH_TIME: Long = 2000; //This is 3 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        //Code to start timer and take action after the timer ends
        Handler().postDelayed(Runnable {
            //Do any action here. Now we are moving to next page
            val mySuperIntent = Intent(this, MasterActivity::class.java)
            startActivity(mySuperIntent);

            //This 'finish()' is for exiting the app when back button pressed from Home page which is ActivityHome
            finish();
        }, SPLASH_TIME);
    }
}
