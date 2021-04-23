package com.example.e_shop

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class forgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        val submit = findViewById<Button>(R.id.submit)
        val et_email = findViewById<TextInputLayout>(R.id.emailfp)
        val backfp = findViewById<ImageView>(R.id.backfp)

        backfp.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        submit.setOnClickListener {
            val email: String = et_email.editText!!.text.toString().trim { it <= ' ' }

            if (email.isEmpty()){
                val snackbar = Snackbar.make(it,"Please fill Email-id", Snackbar.LENGTH_LONG)
                val sbView: View = snackbar.view
                sbView.setBackgroundColor(resources.getColor(R.color.design_default_color_error))
                snackbar.show()
            }else{
                // This piece of code is used to send the reset password link to the user's email id if the user is registered.
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Show the toast message and finish the forgot password activity to go back to the login screen.
                            Toast.makeText(
                                this,"Email sent successfully to reset your password", Toast.LENGTH_LONG
                            ).show()

                            finish()
                        } else {
                               Toast.makeText(this,"Operation Failed either you have enter wrong email-id or the user has been deleted from the server",Toast.LENGTH_LONG).show()
                                  finish()
                        }
                    }
            }


            }

        }

    }
