package com.example.e_shop.ui.activity
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

 open  class BaseActivity : AppCompatActivity() {
    private  var doubleBackToExitPressedOnce = false
    fun doubleBackToExit(){
        if (doubleBackToExitPressedOnce){
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true

        Toast.makeText(this,"Please click back to exit",Toast.LENGTH_LONG).show()

         @Suppress("DEPRECATION")
         Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)

    }

    }

