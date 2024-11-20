package com.example.habitflow.ui.theme

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.example.habitflow.EditActivity
import com.example.habitflow.MainActivity
import com.example.habitflow.databinding.DescriptionActivityBinding
import com.example.habitflow.db.ListItem
import com.example.habitflow.db.MainViewModel
import com.example.habitflow.db.MyAdapter
import com.example.habitflow.db.MyDbManager
import com.example.habitflow.db.MyIntentConstants


class DescriptionActivity : AppCompatActivity(){
    private lateinit var binding: DescriptionActivityBinding
    private var viewModel: MainViewModel? = null
    val myManager = MyDbManager(this)
    var list: ArrayList<ListItem>? = null
    val myAdapter = MyAdapter(ArrayList(),this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DescriptionActivityBinding.inflate(layoutInflater)
        val view: ConstraintLayout = binding.root
        setContentView(view)
        getMyIntent()



    }

    override fun onResume() {
        super.onResume()
        myManager.openDb()
        list = myManager.readDbData()
    }
    override fun onDestroy() {
        super.onDestroy()
        myManager.closeDb()
    }
    fun getMyIntent(){
        val i = intent
        if (i != null)
        {
            if(i.getStringExtra(MyIntentConstants.I_HABIT_KEY)!= null){
                binding.tvHabit.setText(i.getStringExtra(MyIntentConstants.I_HABIT_KEY))
                binding.tvImportance.setText(i.getStringExtra(MyIntentConstants.I_IMPORTANCE_KEY))
                binding.tvReward.setText(i.getStringExtra(MyIntentConstants.I_REWARD_KEY))
                binding.tvCategory.setText(i.getStringExtra(MyIntentConstants.I_CATEGORY_KEY))
                if(i.getStringExtra(MyIntentConstants.I_URI_KEY)!= "empty"){
                    binding.imMainImage.setImageURI(Uri.parse(i.getStringExtra(MyIntentConstants.I_URI_KEY)))
                    binding.fbAddImage2.visibility = View.GONE
                }
            }
        }
    }

     fun onClickBack(view: View) {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }



     fun onEditClick(view:View) {
         val posintent = intent
         var item: ListItem ?= null
         var count: Int = list?.size!!-1
         for(i in 0 .. count)
         {
             if (posintent.getStringExtra(MyIntentConstants.I_HABIT_KEY) == list?.get(i)?.habit){
                 item = list?.get(i)
             }
         }


        val i = Intent(this,EditActivity ::class.java).apply {
            putExtra(MyIntentConstants.I_HABIT_KEY,item?.habit)
            putExtra(MyIntentConstants.I_URI_KEY,item?.uri)
            putExtra(MyIntentConstants.I_REWARD_KEY,item?.reward)
            putExtra(MyIntentConstants.I_IMPORTANCE_KEY,item?.importance)
            putExtra(MyIntentConstants.I_ID_KEY,item?.id)
            putExtra(MyIntentConstants.I_CATEGORY_KEY,item?.category)
        }
        i.putExtra("EditActivity", "DescriptionActivity")
        startActivity(i)
    }





}