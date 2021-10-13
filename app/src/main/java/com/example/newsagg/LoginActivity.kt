package com.example.newsagg

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.*

class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()

        val emailTxt = findViewById<EditText>(R.id.inputEmail)
        val passTxt = findViewById<EditText>(R.id.inputPassword)

        //test
        println(emailTxt.text.toString())
        println(passTxt.text.toString())

        val loginBtn = findViewById<Button>(R.id.login)
        if (emailTxt.text.toString() != null
            || passTxt.text.toString() != null
            || passTxt.text.toString() != ""
            || emailTxt.text.toString() != "") {
            loginBtn.setOnClickListener {
                    view -> mAuth.signInWithEmailAndPassword(
                emailTxt.text.toString(),
                passTxt.text.toString()
            )
                .addOnCompleteListener(this) {
                        task ->
                    if(task.isSuccessful) {
                        val user = mAuth.currentUser
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("id", user?.email)
                        startActivity(intent)
                    }
                    else {
                        closeKeyBoard()
                        showMessage(view, "Sign in unsuccessful")
                    }
                }
            }
        }
        else {
            loginBtn.setOnClickListener {
                Toast.makeText(this, "Sign in unsuccessful", Toast.LENGTH_SHORT).show()
            }
        }

        val registerBtn = findViewById<Button>(R.id.register)
        registerBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        //check current auth state


        var currentUser : FirebaseUser? = mAuth.currentUser
        updateUI(currentUser)
    }

    fun updateUI(user : FirebaseUser?) {
        if (user != null) {

        }
    }

    fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun showMessage(view : View, msg : String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show()
    }
}