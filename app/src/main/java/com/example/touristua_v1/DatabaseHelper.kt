package com.example.touristua

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "tourist.db", null, 3) {

    private val TABLE = "Location"
    private val ID = "id"
    private val NAME = "name"
    private val REGION = "region"
    private val TYPE = "type"
    private val YEAR = "year"
    private val DESC = "description"
    private val IMAGE_URL = "imageUrl"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE $TABLE (
                $ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $NAME TEXT NOT NULL,
                $REGION TEXT,
                $TYPE TEXT,
                $YEAR INTEGER,
                $DESC TEXT,
                $IMAGE_URL TEXT
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE")
        onCreate(db)
    }

    fun addLocation(loc: Location): Long {
        val values = ContentValues()
        values.put(NAME, loc.name)
        values.put(REGION, loc.region)
        values.put(TYPE, loc.type)
        values.put(YEAR, loc.year)
        values.put(DESC, loc.description)
        values.put(IMAGE_URL, loc.imageUrl)
        return writableDatabase.insert(TABLE, null, values)
    }

    fun getAllLocations(): ArrayList<Location> {
        val list = ArrayList<Location>()
        val cursor: Cursor = readableDatabase.rawQuery(
            "SELECT * FROM $TABLE ORDER BY $NAME ASC", null
        )
        while (cursor.moveToNext()) {
            list.add(cursorToLocation(cursor))
        }
        cursor.close()
        return list
    }

    fun searchLocations(query: String): ArrayList<Location> {
        val list = ArrayList<Location>()
        val cursor: Cursor = readableDatabase.rawQuery("""
            SELECT * FROM $TABLE 
            WHERE $NAME LIKE ? OR $REGION LIKE ? OR $DESC LIKE ?
            ORDER BY $NAME ASC
        """.trimIndent(), arrayOf("%$query%", "%$query%", "%$query%"))
        while (cursor.moveToNext()) {
            list.add(cursorToLocation(cursor))
        }
        cursor.close()
        return list
    }

    fun deleteLocation(id: Int): Int {
        return writableDatabase.delete(TABLE, "$ID = ?", arrayOf(id.toString()))
    }

    private fun cursorToLocation(cursor: Cursor): Location {
        return Location(
            id          = cursor.getInt(cursor.getColumnIndexOrThrow(ID)),
            name        = cursor.getString(cursor.getColumnIndexOrThrow(NAME)),
            region      = cursor.getString(cursor.getColumnIndexOrThrow(REGION)) ?: "",
            type        = cursor.getString(cursor.getColumnIndexOrThrow(TYPE)) ?: "",
            year        = cursor.getInt(cursor.getColumnIndexOrThrow(YEAR)),
            description = cursor.getString(cursor.getColumnIndexOrThrow(DESC)) ?: "",
            imageUrl    = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_URL)) ?: ""
        )
    }
}