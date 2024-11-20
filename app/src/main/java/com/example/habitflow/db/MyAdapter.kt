package com.example.habitflow.db

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.MutableInt
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.habitflow.EditActivity
import com.example.habitflow.MainActivity
import com.example.habitflow.Position
import com.example.habitflow.R
import com.example.habitflow.ui.theme.DescriptionActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MyAdapter(listMain:ArrayList<ListItem>, contextM:Context): RecyclerView.Adapter<MyAdapter.MyViewHolder>(){
    var listArray = listMain
    var context = contextM
    private val originalPositions:MutableList<Int> = mutableListOf(1,2,3) // Список для хранения оригинальных позиций

    init {
        // Инициализируем оригинальные позиции
        originalPositions.addAll(listArray.indices)
    }
    inner class MyViewHolder(itemView: View,contextV:Context):RecyclerView.ViewHolder(itemView){
        val context = contextV
        var animbutton = itemView.findViewById<ImageButton>(R.id.buttonAnim)
        val edHabit = itemView.findViewById<TextView>(R.id.edText)
        private var originalColor:Int = ContextCompat.getColor(context, R.color.orange_use)
        private var isButtonPressed = false
        init {
            animbutton.setOnClickListener {

                moveButtonToEnd(adapterPosition)
            }
        }
        fun setData(item:ListItem) {
            edHabit?.text = item.habit
            itemView.setOnClickListener {
                val intent = Intent(context,DescriptionActivity ::class.java).apply {
                    putExtra(MyIntentConstants.I_HABIT_KEY,item.habit)
                    putExtra(MyIntentConstants.I_URI_KEY,item.uri)
                    putExtra(MyIntentConstants.I_REWARD_KEY,item.reward)
                    putExtra(MyIntentConstants.I_IMPORTANCE_KEY,item.importance)
                    putExtra(MyIntentConstants.I_ID_KEY,item.id)
                    putExtra(MyIntentConstants.I_CATEGORY_KEY,item.category)
                }
                context.startActivity(intent)

            }

        }

        private fun moveButtonToEnd(position:Int){

            if(isButtonPressed){
                var originalPosition =originalPositions[0]
                val item = listArray.removeAt(listArray.size - 1) // Удаляем элемент из конца
                listArray.add(originalPosition, item) // Возвращаем элемент на оригинальную позицию
                notifyItemMoved(listArray.size - 1, originalPosition) // Уведомляем адаптер о перемещении
                edHabit.backgroundTintList =  ColorStateList.valueOf(originalColor)

                animbutton.setImageResource(R.drawable.ic_circle)
                isButtonPressed = false
            }
            else{
                edHabit.backgroundTintList =  ColorStateList.valueOf(Color.GRAY)
                val item = listArray[position]
                listArray.removeAt(position)
                listArray.add(item)
                notifyItemMoved(position,listArray.size-1)
                animbutton.setImageResource(R.drawable.ic_check_mark)
                isButtonPressed = true
            }
            originalPositions[0] = position

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyViewHolder(inflater.inflate(R.layout.rc_item,parent,false),context)
    }

    override fun getItemCount(): Int {
        return listArray.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setData(listArray.get(position))
    }

    fun updateAdapter(listItems: List<ListItem>){
        listArray.clear()
        listArray.addAll(listItems)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int,dbManager: MyDbManager){

        dbManager.removeItemFromDb(listArray[position].id.toString())
        listArray.removeAt(position)
        notifyItemRangeChanged(0,listArray.size)
        notifyItemRemoved(position)
    }



}