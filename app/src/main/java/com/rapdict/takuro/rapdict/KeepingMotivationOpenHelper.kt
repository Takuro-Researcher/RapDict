package com.rapdict.takuro.rapdict

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*
import kotlin.collections.ArrayList

class KeepingMotivationOpenHelper internal constructor(private val mContext: Context) : android.database.sqlite.SQLiteOpenHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_HITOKOTO_TABLE)
        db.execSQL(SQL_CREATE_SHOUGOU_TABLE)

        //CSVファイルを使用
        val assetManager = mContext.resources.assets
        var br_H: BufferedReader?=null
        var br_S: BufferedReader?=null
        //クラス配列
        var hitokotoList = ArrayList<Hitokoto>()
        var shougouList = ArrayList<Shougou>()
        //CSVファイルから、クラス配列wordlist[]にデータを全て入れる。
        try {
            val inputStream_hitokoto = assetManager.open("hitokoto.csv")
            val inputStream_shougou = assetManager.open("shougou.csv")
            val inputStream_hitokotoReader = InputStreamReader(inputStream_hitokoto)
            val inputStream_shougouReader = InputStreamReader(inputStream_shougou)
            br_H = BufferedReader(inputStream_hitokotoReader!!)
            br_S = BufferedReader(inputStream_shougouReader!!)
            val hitokoto_array = arrayOf("hitokoto", "to_shougou")
            val shougou_array =arrayOf("shougou","achieve_exp")
            hitokotoList = csv_saveHitokotoTable(br_H,hitokoto_array)
            shougouList = csv_saveShougouTable(br_S,shougou_array)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                br_H!!.close()
                br_S!!.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        //実際に保存する
        motivation_saveData(db,shougouList,hitokotoList)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_HITOKOTO_DELETE)
        db.execSQL(SQL_SHOUGOU_DELETE)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
    //CSVを分解し、一言リストに変換する
    fun csv_saveHitokotoTable(br: BufferedReader,table_column:Array<String>): ArrayList<Hitokoto> {
        val hitokotoList = ArrayList<Hitokoto>()
        var line:String?
        do{
            line =br.readLine()
            if(line==null){
                break
            }
            val data = line.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            var colno = 0
            val hitokoto =Hitokoto()
            for (column in table_column) {
                when (colno) {
                    0 -> hitokoto.hitokoto = data[colno]
                    1 -> hitokoto.to_shougou = Integer.parseInt(data[colno])
                }
                colno++
            }
            hitokotoList.add(hitokoto)
        }while(true)
        return hitokotoList
    }
    //CSVを分解し、称号リストに変換する
    fun csv_saveShougouTable(br: BufferedReader,table_column:Array<String>): ArrayList<Shougou> {
        val shougouList = ArrayList<Shougou>()
        var line:String?
        do{
            line =br.readLine()
            if(line==null){
                break
            }
            val data = line.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            var colno = 0
            val shougou =Shougou()
            for (column in table_column) {
                when (colno) {
                    0 -> shougou.shougou = data[colno]
                    1 -> shougou.achieve_exp = Integer.parseInt(data[colno])
                }
                colno++
            }
            shougouList.add(shougou)
        }while(true)
        return shougouList
    }

    //実データにセーブする
    fun motivation_saveData(db: SQLiteDatabase, shougouList: ArrayList<Shougou>, hitokotoList: ArrayList<Hitokoto>) {
        val shougou_values = ContentValues()
        val hitokoto_values = ContentValues()
        for(i in shougouList.indices){
            System.out.println(shougouList[i].shougou)
            System.out.println(shougouList[i].achieve_exp)
            shougou_values.put("shougou",shougouList[i].shougou)
            shougou_values.put("achieve_exp",shougouList[i].achieve_exp)

            db.insert(SHOUGOU_TABLE_NAME,null,shougou_values)
        }
        for(i in hitokotoList.indices){
            System.out.println(hitokotoList[i].hitokoto)
            System.out.println(hitokotoList[i].to_shougou)
            hitokoto_values.put("hitokoto",hitokotoList[i].hitokoto)
            hitokoto_values.put("to_shougou",hitokotoList[i].to_shougou)
            db.insert(HITOKOTO_TABLE_NAME,null,hitokoto_values)
        }
        System.out.println("セーブ完了だち！")
    }

    companion object {
        private val DATABASE_VERSION = 4

        // データーベース情報を変数に格納
        private val DATABASE_NAME = "KeepingMotivation.db"

        //称号テーブルカラム
        private val SHOUGOU_TABLE_NAME = "shougoutable"
        private val COLUMN_NAME_SHOUGOU_ID ="shougou_id"
        private val COLUMN_NAME_SHOUGOU ="shougou"
        private val COLUMN_NAME_ACHIEVE_EXP ="achieve_exp"

        //一言テーブルカラム
        private val HITOKOTO_TABLE_NAME = "hitokototable"
        private val COLUMN_NAME_HITOKOTO_ID = "hitokoto_id"
        private val COLUMN_NAME_HITOKOTO = "hitokoto"
        private val COLUMN_NAME_TO_SHOUGOU = "to_shougou"
        //スキーマ定義
        private val SQL_CREATE_SHOUGOU_TABLE = "CREATE TABLE " + SHOUGOU_TABLE_NAME + " (" +
                COLUMN_NAME_SHOUGOU_ID + " INTEGER PRIMARY KEY autoincrement," +
                COLUMN_NAME_SHOUGOU + " TEXT," +
                COLUMN_NAME_ACHIEVE_EXP + " INTEGER)"
        private val SQL_CREATE_HITOKOTO_TABLE = "CREATE TABLE " + HITOKOTO_TABLE_NAME + " (" +
                COLUMN_NAME_HITOKOTO_ID + " INTEGER PRIMARY KEY autoincrement," +
                COLUMN_NAME_HITOKOTO + " TEXT," +
                COLUMN_NAME_TO_SHOUGOU + " INTEGER)"

        private val SQL_HITOKOTO_DELETE = "DROP TABLE IF EXISTS $SHOUGOU_TABLE_NAME"
        private val SQL_SHOUGOU_DELETE = "DROP TABLE IF EXISTS $HITOKOTO_TABLE_NAME"
    }
}