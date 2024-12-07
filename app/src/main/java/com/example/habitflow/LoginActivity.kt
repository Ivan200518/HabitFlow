package com.example.habitflow

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.habitflow.databinding.ActivityLoginBinding
import com.example.habitflow.db.MyAdapter
import com.example.habitflow.db.MyDbManager
import com.example.habitflow.db.User

class LoginActivity : ComponentActivity() {
    private lateinit var binding: ActivityLoginBinding
    val myManager = MyDbManager(this)
    val myAdapter = MyAdapter(ArrayList(), this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view: ConstraintLayout = binding.root
        setContentView(view)

    }

    override fun onResume() {
        super.onResume()
        myManager.openDb()
    }

    override fun onDestroy() {
        super.onDestroy()
        myManager.closeDb()
    }

    fun onClickLogin(view: View) {
        val email = binding.tvEmail.text.toString()
        val password = binding.tvPassword.text.toString()
        val userList = myManager.readFromUserDb()
        var truth = false
        userList.forEach { user ->
            if (user.email == email && user.password == password) {
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                truth = true
            }
        }
        if(!truth){
            Toast.makeText(this, "Not correct email or password", Toast.LENGTH_SHORT).show()
        }




    }

    fun onClickMovetoSing(view: View) {
        val i = Intent(this, SingUpActivity::class.java)
        startActivity(i)
    }


}