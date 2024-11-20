package com.example.habitflow

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.habitflow.databinding.EditActivityBinding
import com.example.habitflow.db.ListItem
import com.example.habitflow.db.MyAdapter
import com.example.habitflow.db.MyDbManager
import com.example.habitflow.db.MyIntentConstants
import com.example.habitflow.ui.theme.DescriptionActivity

class EditActivity : AppCompatActivity() {
    var id = 0
    var isEditState = false
    val imageRequestCode = 10
    var tempImageUri = "empty"
    val myDbManager = MyDbManager(this)
    var activeIntent: String? = ""
    var list: ArrayList<ListItem>? = null
    private lateinit var binding: EditActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = EditActivityBinding.inflate(layoutInflater)
        val view: ConstraintLayout = binding.root
        setContentView(view)
        var intent = getIntent();
        activeIntent = intent?.getStringExtra("EditActivity")
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (activeIntent != null) {
            getMyIntent()
        }


    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
        list = myDbManager.readDbData()
    }


    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == imageRequestCode) {
            binding.imMainImage.setImageURI(data?.data)
            binding.fbAddImage2.visibility = View.GONE
            binding.textView.visibility = View.GONE
            tempImageUri = data?.data.toString()
            contentResolver.takePersistableUriPermission(
                data?.data!!,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }

    }

    fun onClickBack(view: View) {
        if (activeIntent == null) {
            var i = Intent(this, MainActivity::class.java)
            startActivity(i)
        } else {
            val posintent = intent
            var item: ListItem?= null
            var count: Int = list?.size!!-1
            for(i in 0 .. count)
            {
                if (posintent.getStringExtra(MyIntentConstants.I_HABIT_KEY) == list?.get(i)?.habit){
                    item = list?.get(i)
                }
            }
            val i = Intent(this,DescriptionActivity ::class.java).apply {
                putExtra(MyIntentConstants.I_HABIT_KEY,item?.habit)
                putExtra(MyIntentConstants.I_URI_KEY,item?.uri)
                putExtra(MyIntentConstants.I_REWARD_KEY,item?.reward)
                putExtra(MyIntentConstants.I_IMPORTANCE_KEY,item?.importance)
                putExtra(MyIntentConstants.I_ID_KEY,item?.id)
                putExtra(MyIntentConstants.I_CATEGORY_KEY,item?.category)
            }
            startActivity(i)
        }


    }

    fun getMyIntent() {
        val i = intent
        if (i != null) {
            if (i.getStringExtra(MyIntentConstants.I_HABIT_KEY) != null) {
                binding.edHabit.setText(i.getStringExtra(MyIntentConstants.I_HABIT_KEY))
                if (i.getStringExtra(MyIntentConstants.I_IMPORTANCE_KEY) != "") {
                    binding.edImportance.setText(i.getStringExtra(MyIntentConstants.I_IMPORTANCE_KEY))
                } else {
                    binding.edImportance.hint = ""
                }
                if (i.getStringExtra(MyIntentConstants.I_REWARD_KEY) != "") {
                    binding.edReward.setText(i.getStringExtra(MyIntentConstants.I_REWARD_KEY))
                } else {
                    binding.edReward.hint = ""
                }
                if (i.getStringExtra(MyIntentConstants.I_CATEGORY_KEY) != "") {
                    binding.edCategory.setText(i.getStringExtra(MyIntentConstants.I_CATEGORY_KEY))
                }
                else{
                    binding.edCategory.hint = ""
                }
                isEditState = true
                id = i.getIntExtra(MyIntentConstants.I_ID_KEY, 0)
                if (i.getStringExtra(MyIntentConstants.I_URI_KEY) != "empty") {
                    binding.imMainImage.setImageURI(Uri.parse(i.getStringExtra(MyIntentConstants.I_URI_KEY)))
                    binding.fbAddImage2.visibility = View.GONE
                }
            }
        }
    }

    fun onClickChooseImage(view: View) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startActivityForResult(intent, imageRequestCode)

    }

    fun showInfoToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()


    }

    fun onClickSave(view: View) {
        val habitText = binding.edHabit.text.toString()
        val rewardText = binding.edReward.text.toString()
        val importanceText = binding.edImportance.text.toString()
        val categoryText = binding.edCategory.text.toString()


        if (habitText != "") {
            if (isEditState) {
                myDbManager.updateItemInDb(
                    habitText,
                    rewardText,
                    importanceText,
                    categoryText,
                    tempImageUri,
                    id
                )
                var i = Intent(this, MainActivity::class.java)
                startActivity(i)
            } else {
                myDbManager.insertToDb(
                    habitText,
                    rewardText,
                    importanceText,
                    categoryText,
                    tempImageUri
                )
                var i = Intent(this, MainActivity::class.java)
                startActivity(i)
            }

        }
        else {
            showInfoToast("Please enter habit")
        }


    }
}

