package com.example.habitflow

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habitflow.db.MyAdapter
import com.example.habitflow.db.MyDbManager
import com.example.habitflow.ui.theme.HabitFlowTheme

import com.example.habitflow.databinding.ActivityMainBinding

class MainActivity : ComponentActivity() {

    val myManager = MyDbManager(this)
    val myAdapter = MyAdapter(ArrayList(),this)
//    private lateinit var binding: MainActivityBinding
    var rcView: RecyclerView ?= null


    private lateinit var binding: ActivityMainBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view: ConstraintLayout = binding.root
        setContentView(view)
        rcView = findViewById(R.id.rcView)
        init()
    }


    override fun onResume() {
        super.onResume()
        myManager.openDb()
        fillAdapter()
    }


    override fun onDestroy() {
        super.onDestroy()
        myManager.closeDb()
    }

    fun onClickNew(view: View) {
        var i = Intent(this, EditActivity::class.java)
        startActivity(i)
    }
    fun init(){
        rcView?.layoutManager = LinearLayoutManager(this )
        val swapHelper = getSwapMg()
        swapHelper.attachToRecyclerView(rcView)
        rcView?.adapter  = myAdapter
    }

    fun showInfoAlert(viewHolder: RecyclerView.ViewHolder) {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("")
            .setMessage(R.string.sure)
            .setPositiveButton(R.string.yes) { dialog, which ->
                myAdapter.removeItem(viewHolder.adapterPosition,myManager)
            }
            .setNegativeButton(R.string.cancel) { dialog, which ->
                myAdapter.updateAdapter(myManager.readDbData())
                dialog.dismiss()

            }
        var dialog = builder.create();
        dialog.show()
    }
    fun fillAdapter(){
        val list = myManager.readDbData()
        myAdapter.updateAdapter(list)
        if(list.size != 0)
        {
            binding.tvNoElements.visibility = View.GONE
        }else{
            binding.tvNoElements.visibility = View.VISIBLE
        }
    }

    private fun getSwapMg(): ItemTouchHelper{
        return ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT
                or ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                showInfoAlert(viewHolder)
            }

        })
    }




}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HabitFlowTheme {
        Greeting("Android")
    }
}