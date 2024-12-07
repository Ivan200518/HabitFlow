package com.example.habitflow.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns

class MyDbManager( context: Context) {
    val myDbHelper = MyDbHelper(context)
    var db: SQLiteDatabase? = null

    fun openDb() {
        db = myDbHelper.writableDatabase

    }
    fun insertToUserDb(  email:String,password:String){
        val values = ContentValues().apply {
            put(MyDbNameClass.COLUMN_NAME_EMAIL, email)
            put(MyDbNameClass.COLUMN_NAME_PASSWORD, password)
        }
        db?.insert(MyDbNameClass.USERTABLE_NAME,null,values)

    }
    fun readFromUserDb():ArrayList<User>{

        val usersList = ArrayList<User>();
        val cursor = db?.query(MyDbNameClass.USERTABLE_NAME,null,null,
            null,null,null,null)
         if (cursor != null){
             while (cursor?.moveToNext()!!){
                 val columnIndex = cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_EMAIL)
                 val columnIndex2 = cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_PASSWORD)
                 if (columnIndex!= -1 && columnIndex2!= -1){
                     val email = cursor.getString(columnIndex)
                     val password = cursor.getString(columnIndex2)
                     val item = User(email,password)
                     usersList.add(item)
                 }
             }
             cursor.close()

         }
        return usersList
    }
    fun insertToDb(
        habit: String,
        reward: String,
        importance: String,
        category: String,
        uri: String
    ) {
        val values = ContentValues().apply {
            put(MyDbNameClass.COLUMN_NAME_HABIT, habit)
            put(MyDbNameClass.COLUMN_NAME_REWARD, reward)
            put(MyDbNameClass.COLUMN_NAME_IMPORTANCE, importance)
            put(MyDbNameClass.COLUMN_NAME_CATEGORY, category)
            put(MyDbNameClass.COLUMN_NAME_IMAGE_URI, uri)

        }
        db?.insert(MyDbNameClass.TABLE_NAME, null, values)

    }

    fun readDbData(): ArrayList<ListItem> {
        val arrayList = ArrayList<ListItem>()
        val cursor = db?.query(
            MyDbNameClass.TABLE_NAME, null, null,
            null, null, null, null
        )
        if (cursor != null) {
            while (cursor?.moveToNext()!!) {
                val columnIndex = cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_HABIT)
                val columnIndex2 = cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_IMPORTANCE)
                val columnIndex3 = cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_REWARD)
                val columnIndex4 = cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_IMAGE_URI)
                val columnIndex5 = cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_CATEGORY)

                val columnIndex6 = cursor.getColumnIndex(BaseColumns._ID)

                if (columnIndex != -1 && columnIndex2 != -1 && columnIndex3 != -1 &&
                    columnIndex4 != -1 && columnIndex5 != -1
                ) {

                    val dataHabit = cursor?.getString(columnIndex)
                    val dataImportance = cursor?.getString(columnIndex2)

                    val dataReward = cursor?.getString(columnIndex3)
                    val dataUri = cursor?.getString(columnIndex4)
                    val dataCategory = cursor?.getString(columnIndex5)
                    val dataId = cursor?.getInt(columnIndex6)
                    var item = ListItem(dataHabit, dataImportance, dataReward,dataUri,dataCategory)
                    item.id = dataId
                    arrayList.add(item)
                }

            }
        }
        cursor?.close()
        return arrayList
    }

    fun closeDb() {
        myDbHelper.close()
    }
    fun removeItemFromDb(
        id: String
        
    ) {
        val selection = BaseColumns._ID + "=$id"
        db?.delete(MyDbNameClass.TABLE_NAME,selection, null)

    }
    fun updateItemInDb(
        habit: String,
        reward: String,
        importance: String,
        category: String,
        uri: String,
        id: Int
        )
    {
        val selection = BaseColumns._ID + "=$id"

        val values = ContentValues().apply {
            put(MyDbNameClass.COLUMN_NAME_HABIT, habit)
            put(MyDbNameClass.COLUMN_NAME_REWARD, reward)
            put(MyDbNameClass.COLUMN_NAME_IMPORTANCE, importance)
            put(MyDbNameClass.COLUMN_NAME_CATEGORY, category)
            put(MyDbNameClass.COLUMN_NAME_IMAGE_URI, uri)
        }
        db?.update(MyDbNameClass.TABLE_NAME, values,selection,null)

    }
}