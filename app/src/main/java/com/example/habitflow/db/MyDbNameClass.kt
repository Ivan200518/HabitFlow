package com.example.habitflow.db

import android.provider.BaseColumns

object MyDbNameClass: BaseColumns {
    const val TABLE_NAME = "my_table"
    const val COLUMN_NAME_HABIT = "habit"
    const val COLUMN_NAME_REWARD = "reward"
    const val COLUMN_NAME_IMPORTANCE = "importance"
    const val COLUMN_NAME_CATEGORY = "category"
    const val COLUMN_NAME_IMAGE_URI = "uri"

    const val DATABASE_VERSION = 3
    const val DATABASE_NAME = "Database.db "

    const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT" +
            "$COLUMN_NAME_HABIT TEXT NOT NULL," +
            "$COLUMN_NAME_REWARD TEXT NOT NULL," + "$COLUMN_NAME_IMPORTANCE TEXT NOT NULL," +
            "$COLUMN_NAME_CATEGORY TEXT NOT NULL,"+
            "$COLUMN_NAME_IMAGE_URI TEXT NOT NULL)"
    const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
}