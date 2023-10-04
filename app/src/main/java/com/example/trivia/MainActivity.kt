package com.example.trivia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.trivia.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding=ActivityMainBinding.inflate(layoutInflater)
        val view=mainBinding.root
        setContentView(view)



        mainBinding.buttonSignout.setOnClickListener {


            FirebaseAuth.getInstance().signOut()

            val gao=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build()
            val googleSignInClient=GoogleSignIn.getClient(this,gao)
            googleSignInClient.signOut().addOnCompleteListener {task->
            if (task.isSuccessful){
                Toast.makeText(applicationContext,"Sign Out",Toast.LENGTH_LONG).show()
            }

            }

            val intent= Intent(this@MainActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()


        }
        mainBinding.buttonStartQuiz.setOnClickListener {



            val intent=Intent(this,QuizActivity::class.java)

            startActivity(intent);
        }

    }
}