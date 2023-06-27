package com.example.storagemediaupload

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.storagemediaupload.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    lateinit var auth:FirebaseAuth
    lateinit var bind : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        auth = FirebaseAuth.getInstance()
        bind.signup.setOnClickListener {
            val em = bind.editTextTextEmailAddress.text.toString()
            val pas = bind.editTextNumberPassword.text.toString()
            if(em.isNotEmpty() && pas.isNotEmpty()){
                auth.createUserWithEmailAndPassword(em,pas).addOnCompleteListener {
                    startActivity(Intent(this,upload::class.java))
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this,"Failed to signup",Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this,"Please fill completly",Toast.LENGTH_SHORT).show()
            }
        }
    }
}