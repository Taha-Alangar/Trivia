package com.example.trivia

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.trivia.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    lateinit var loginBinding: ActivityLoginBinding

    val auth=FirebaseAuth.getInstance()

    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        loginBinding=ActivityLoginBinding.inflate(layoutInflater)
        val view=loginBinding.root

        setContentView(view)
        val textOfGoogleButton=loginBinding.buttonGoogleSignin.getChildAt(0) as TextView
            textOfGoogleButton.text="Continue with Google"
            textOfGoogleButton.setTextColor(Color.BLACK)
        textOfGoogleButton.textSize=18F

        registerActivityForGoogleSignIn()

        loginBinding.buttonLoginin.setOnClickListener {

            val userEmail=loginBinding.editTextLoginEmail.text.toString()
            val userPassword=loginBinding.editTextLoginPassword.text.toString()
            signInUser(userEmail,userPassword)

        }
        loginBinding.buttonGoogleSignin.setOnClickListener {

            signinGoogle()



        }
        loginBinding.textViewSignup.setOnClickListener {


            val intent= Intent(this@LoginActivity,SignupActivity::class.java)
            startActivity(intent)

        }
        loginBinding.textViewForgotPassword.setOnClickListener {

            val intent=Intent(this@LoginActivity,ForgotPasswordActivity::class.java)
            startActivity(intent)

        }

    }
    fun signInUser(userEmail:String,userPassword:String){

        val isValidated = validateData(userEmail, userPassword)
        if (!isValidated) {
            return
        }

        auth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener {task->

            if (task.isSuccessful){


                val  intent=Intent(this@LoginActivity,MainActivity::class.java)
                startActivity(intent)
                finish()

            }else{
                Toast.makeText(applicationContext,"Invalid Credentials",Toast.LENGTH_SHORT).show()

            }

        }
    }

    override fun onStart() {
        super.onStart()

        val user=auth.currentUser
        if (user!=null){

            val  intent=Intent(this@LoginActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private  fun signinGoogle(){
        val gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("712855438530-5g0elc2kjgel4u17j76cja4h082n65il.apps.googleusercontent.com")
            .requestEmail().build()

        googleSignInClient=GoogleSignIn.getClient(this,gso)
        signIn()
    }
    private fun signIn(){

        val  signInIntent:Intent=googleSignInClient.signInIntent
        activityResultLauncher.launch(signInIntent)
    }
    private fun registerActivityForGoogleSignIn(){
        activityResultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {result->
                val resultCode=result.resultCode
                val data=result.data

                if (resultCode== RESULT_OK && data!=null){
                    val task:Task<GoogleSignInAccount> =GoogleSignIn.getSignedInAccountFromIntent(data)
                    firebaseSignInWithGoogle(task)
                }
            })
    }
    private fun firebaseSignInWithGoogle(task:Task<GoogleSignInAccount>){
            try {
                val account:GoogleSignInAccount =task.getResult(ApiException::class.java)
                Toast.makeText(applicationContext,"Welcome to Quiz Game",Toast.LENGTH_SHORT).show()
                val intent=Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
                firebaseGoogleAccount(account)
            } catch (e:ApiException){
                Toast.makeText(applicationContext,e.localizedMessage,Toast.LENGTH_SHORT).show()
            }
    }
    private fun firebaseGoogleAccount(account: GoogleSignInAccount){
        val authCredential=GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(authCredential).addOnCompleteListener {task->
            if (task.isSuccessful){

            }
        }
    }



    fun validateData(email: String?, password: String): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginBinding.editTextLoginEmail.setError("Email is Invalid")
            return false
        }
        if (password.length < 6) {
            loginBinding.editTextLoginPassword.setError("Atleast 6 character")
            return false
        }
        return true
    }

}