package com.example.trivia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.trivia.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var forgotBinding:ActivityForgotPasswordBinding
    val auth=FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forgotBinding=ActivityForgotPasswordBinding.inflate(layoutInflater)
        val view=forgotBinding.root
        setContentView(view)


        forgotBinding.buttonReset.setOnClickListener {

            val userEmail=forgotBinding.editTextForgotEmail.text.toString()

            auth.sendPasswordResetEmail(userEmail).addOnCompleteListener {task->
                if (task.isSuccessful){
                    Toast.makeText(applicationContext,"We sent a password reset mail to your Gmail",Toast.LENGTH_LONG).show()
                    finish()

                }else{
                    Toast.makeText(applicationContext,task.exception?.localizedMessage,Toast.LENGTH_LONG).show()

                }
            }

        }
    }
}