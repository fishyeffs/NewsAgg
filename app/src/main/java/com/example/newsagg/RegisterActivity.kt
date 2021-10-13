package com.example.newsagg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val email  = findViewById<EditText>(R.id.inputNewEmail)
        val pass1  = findViewById<EditText>(R.id.inputNewPass)
        val pass2  = findViewById<EditText>(R.id.confirmPassword)
        mAuth = FirebaseAuth.getInstance()

        val registerBtn = findViewById<Button>(R.id.registerBtn)
        registerBtn.setOnClickListener {
            if (true) { // pass1 == pass2
                Log.d("DEBUG", email.text.toString())
                mAuth.createUserWithEmailAndPassword(email.text.toString(), pass1.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Log.d("DEBUG", "Create user is successful")
                            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                            val user = mAuth.currentUser
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("id", user?.email)
                            startActivity(intent)
                        }
                        else {
                            Toast.makeText(this, "An error occurred :(", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }

    }
}