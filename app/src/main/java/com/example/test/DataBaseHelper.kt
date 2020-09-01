package com.example.test

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

object TableInfo: BaseColumns{
    const val TABLE_NAME = "reminder"
    const val TABLE_COLUMN_TOPIC = "topic"
    const val TABLE_COLUMN_TEXT = "text"
    const val TABLE_COLUMN_TIME = "time"
    const val TABLE_COLUMN_REMINDER_ID = "reminderId"
}

object BasicCommand {
    const val SQL_CREATE_TABLE =
        "CREATE TABLE ${TableInfo.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${TableInfo.TABLE_COLUMN_TOPIC} TEXT NOT NULL," +
                "${TableInfo.TABLE_COLUMN_TEXT} TEXT NOT NULL," +
                "${TableInfo.TABLE_COLUMN_TIME} TEXT NOT NULL," +
                "${TableInfo.TABLE_COLUMN_REMINDER_ID} INTEGER NOT NULL)"

    const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS ${TableInfo.TABLE_NAME}"
}

class DataBaseHelper(context: Context): SQLiteOpenHelper(context, TableInfo.TABLE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(BasicCommand.SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}