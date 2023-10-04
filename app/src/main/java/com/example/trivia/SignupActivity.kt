package com.example.trivia

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trivia.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.sign

class SignupActivity : AppCompatActivity() {

    lateinit var signupBinding: ActivitySignupBinding

    val auth:FirebaseAuth=FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signupBinding=ActivitySignupBinding.inflate(layoutInflater)
        val view=signupBinding.root
        setContentView(view)

        signupBinding.buttonSignup.setOnClickListener {

           createAccount()
        }
        signupBinding.textViewLoginup.setOnClickListener {

            val intent= Intent(this@SignupActivity,LoginActivity::class.java)
            startActivity(intent)
        }
    }

    fun signupWithFirebase(email:String,password:String){

        signupBinding.progressBarSignup.visibility= View.VISIBLE
        signupBinding.buttonSignup.isClickable=false

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {task->
        if (task.isSuccessful){

            Toast.makeText(applicationContext,"Your account has been created",Toast.LENGTH_SHORT).show()
            finish()
            signupBinding.progressBarSignup.visibility=View.INVISIBLE
            signupBinding.buttonSignup.isClickable=true
        }else{
            Toast.makeText(applicationContext,task.exception?.localizedMessage,Toast.LENGTH_SHORT).show()


        }


        }
    }
    fun validateData( email: String?, password: String): Boolean {

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signupBinding.editTextSignupEmail.setError("Email is Invalid")
            return false
        }
        if (password.length < 6) {
            signupBinding.editTextSignupPassword.setError("Atleast 6 character")
            return false
        }
        return true
    }

    fun createAccount() {
        val email: String = signupBinding.editTextSignupEmail.getText().toString()
        val password: String = signupBinding.editTextSignupPassword.getText().toString()
        val isValidated = validateData( email, password)
        if (!isValidated) {
            return
        }
        signupWithFirebase(email, password)
    }
}