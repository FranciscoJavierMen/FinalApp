package com.example.finalapp.actvities.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.finalapp.R
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth.signOut()
    }
}
