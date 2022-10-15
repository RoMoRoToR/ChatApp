package com.example.chatapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.DatabaseRegistrar
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference


    private var mAuth = FirebaseAuth.getInstance()
    private lateinit var refUsers: DatabaseRegistrar
    private var firebaseUserID: String = ""
    val register_button = findViewById(R.id.registration_button) as Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val toolbar: Toolbar = findViewById(R.id.toolbar_register)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Registration"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        mAuth = FirebaseAuth.getInstance()

        register_button.setOnClickListener{
            registerUser()
        }

    }

    private fun registerUser() {
        val username: String = (R.id.username_registration).javaClass.toString()
        val email: String = (R.id.email_registration).javaClass.toString()
        val password: String = (R.id.password_registration).javaClass.toString()

        if (username == " "){
            Toast.makeText(this@RegisterActivity, "Enter you name", Toast.LENGTH_LONG).show()
        }
        if (email == " "){
            Toast.makeText(this@RegisterActivity, "Enter you email", Toast.LENGTH_LONG).show()
        }
        if (password == " "){
            Toast.makeText(this@RegisterActivity, "Enter you password", Toast.LENGTH_LONG).show()
        }
        else{
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    task ->
                    if (task.isSuccessful)
                    {
                        firebaseUserID = mAuth.currentUser!!.uid
                        refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserID)

                        val userHashMap = HashMap<String, Any>()
                        userHashMap["uid"]=firebaseUserID
                        userHashMap["email"]=email
                        userHashMap["username"]=username

                        refUsers.updateChildren(userHashMap)
                            .addOnCompliteListener {task ->
                                if (task.isSuccessful)
                                {
                                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                    }
                    else
                    {
                        Toast.makeText(this@RegisterActivity, "Error", Toast.LENGTH_LONG).show()
                    }
                }
        }

    }
}