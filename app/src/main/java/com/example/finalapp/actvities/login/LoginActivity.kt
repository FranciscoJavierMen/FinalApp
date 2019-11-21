package com.example.finalapp.actvities.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finalapp.R
import com.example.finalapp.actvities.SignUpActivity
import com.example.finalapp.isValidEmail
import com.example.finalapp.isValidPassword
import com.example.finalapp.validate
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val mGoogleApiClient: GoogleApiClient by lazy { getGoogleApiClient() }
    private val RC_GOOGLE_SIGN_IN = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.finalapp.R.layout.activity_login)

        initComponents()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        if (currentUser == null){
            Toast.makeText(this, "The user isnt logded in", Toast.LENGTH_SHORT).show()
            val myUser = mAuth.currentUser
            myUser?.displayName
            myUser?.email
            myUser?.phoneNumber
            myUser?.photoUrl
            myUser?.isEmailVerified
        } else {
            Toast.makeText(this, "The user is logded in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getGoogleApiClient(): GoogleApiClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleApiClient.Builder(this)
            .enableAutoManage(this, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT).show()
    }

    private fun initComponents() {
        btnSignIn.setOnClickListener{
            val email = edtLayoutEmail.editText?.text.toString()
            val password = edtLayoutPassword.editText?.text.toString()

            if (isValidEmail(email) && isValidPassword(password)){
                signInWithEmailAndPassword(email, password)
            } else {
                Toast.makeText(this, "Wrong user or password", Toast.LENGTH_SHORT).show()
            }
        }

        txtForgotPassword.setOnClickListener {
            //val intent = Intent(this, ForgotPasswordActivity::class.java)
            //startActivity(intent)
            mAuth.signOut()
        }

        btnNewAccount.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        edtLayoutEmail.validate{
            edtLayoutEmail.editText?.error = if(isValidEmail(it)) null else "Invalid email"
        }

        edtLayoutPassword.validate{
            edtLayoutPassword.editText?.error = if(isValidPassword(it)) null else "Invalid password"
        }

        btnSignInGoogle.setOnClickListener {
            val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
            startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_GOOGLE_SIGN_IN){
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess){
                val account = result.signInAccount
                logingByGoogleAccountIntoFirebase(account!!)
            }
        }
    }

    private fun logingByGoogleAccountIntoFirebase(googleAccount: GoogleSignInAccount){
        val credentials = GoogleAuthProvider.getCredential(googleAccount.idToken, null)
        mAuth.signInWithCredential(credentials).addOnCompleteListener(this){
            if (mGoogleApiClient.isConnected){
                Auth.GoogleSignInApi.signOut(mGoogleApiClient)
            }

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    //Acceder con un usuario creado
    private fun signInWithEmailAndPassword(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    if(mAuth.currentUser!!.isEmailVerified){
                        Toast.makeText(this, "The user ${mAuth.currentUser?.displayName} has been logged in", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "First confirm the mail we sent you", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "An unexpected error occurred, please try again later", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
