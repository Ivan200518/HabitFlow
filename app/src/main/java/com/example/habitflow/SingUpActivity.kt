package com.example.habitflow

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.habitflow.databinding.ActivityLoginBinding
import com.example.habitflow.databinding.ActivitySignupBinding
import com.example.habitflow.db.MyDbManager

class SingUpActivity:ComponentActivity() {
    private lateinit var binding: ActivitySignupBinding
    val myDbManager = MyDbManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignupBinding.inflate(layoutInflater)
        val view: ConstraintLayout = binding.root
        setContentView(view)

    }
    override fun onResume(){
        super.onResume()
        myDbManager.openDb()
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }



    fun onClickAddToDb(view: View) {
        var IsSignUp:Boolean = false
        val email = binding.tvEmail.text.toString()
        val password = binding.tvPassword.text.toString()
        val confirmPassword = binding.confirmPassword.text.toString()
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
        else if (password != confirmPassword)
        {
            Toast.makeText(this, "The passwords do not match", Toast.LENGTH_SHORT).show()
        }
        else{
            myDbManager.insertToUserDb(email, password)
            IsSignUp = true
        }

        if (IsSignUp)
        {
            Toast.makeText(this, "Sign up successfully", Toast.LENGTH_SHORT).show()
            val i = Intent(this,LoginActivity::class.java)
            startActivity(i)
        }


    }

    fun onClickLog(view: View) {
        val i = Intent(this, LoginActivity::class.java)
        startActivity(i)
    }
}