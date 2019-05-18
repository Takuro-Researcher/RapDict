package com.rapdict.takuro.rapdict;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SQLiteOpenHelper extends android.database.sqlite.SQLiteOpenHelper {
    // データーベースのバージョン
    private static final int DATABASE_VERSION = 8;

    // データーベース情報を変数に格納
    private static final String DATABASE_NAME = "WordDb.db";
    private static final String QUESTION_TABLE_NAME = "wordtable";
    private static final String ANSWER_TABLE_NAME = "answertable";
    private static final String _ID = "_id";
    private static final String COLUMN_NAME_FURIGANA = "furigana";
    private static final String COLUMN_NAME_WORD = "word";
    private static final String COLUMN_NAME_WORD_LEN="word_len";
    private static final String COLUMN_NAME_WORD_ID="word_id";
    private static final String COLUMN_NAME_ANSWER="answer";

    private final Context mContext;
    //スキーマ定義
    private static final String SQL_CREATE_QUESTION_TABLE =
            "CREATE TABLE " + QUESTION_TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY autoincrement," +
                    COLUMN_NAME_FURIGANA + " TEXT," +
                    COLUMN_NAME_WORD +" TEXT,"+
                    COLUMN_NAME_WORD_LEN + " INTEGER)";
    private static final String SQL_CREATE_ANSWER_TABLE = "CREATE TABLE " + ANSWER_TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY autoincrement," +
                    COLUMN_NAME_ANSWER +" TEXT,"+
                    COLUMN_NAME_WORD_ID + " INTEGER)";

    private static final String SQL_QUESTION_DELETE = "DROP TABLE IF EXISTS " + QUESTION_TABLE_NAME;
    private static final String SQL_ANSWER_DELETE = "DROP TABLE IF EXISTS " + ANSWER_TABLE_NAME;

    SQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_QUESTION_TABLE);
        db.execSQL(SQL_CREATE_ANSWER_TABLE);
        //CSVファイルを使用
        AssetManager assetManager=mContext.getResources().getAssets();
        BufferedReader br=null;
        //クラス配列
        ArrayList<Word> wordList =new ArrayList<Word>();
        //CSVファイルから、クラス配列wordlist[]にデータを全て入れる。
        try{
            InputStream inputStream=assetManager.open("wordlist.csv");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            br = new BufferedReader(inputStreamReader);
            String[] arr ={"furigana","word","word_len"};
            String line;

            while((line=br.readLine())!=null){
                String[] data =line.split(",");
                int colno = 0;
                Word word=new Word();
                for (String column : arr) {
                    switch (colno){
                        case 0:
                            word.setFurigana(data[colno]);
                            break;
                        case 1:
                            word.setWord(data[colno]);
                            break;
                        case 2:
                            word.setWord_len(Integer.parseInt(data[colno]) );
                            break;
                    }
                    colno++;
                }
                wordList.add(word);
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //wordList[]から、実際にSQLiteによる登録
        for(int i=0;i<wordList.size();i++) {
            question_saveData(db, wordList.get(i).getFurigana(),wordList.get(i).getWord(),wordList.get(i).getWord_len());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_QUESTION_DELETE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void question_saveData(SQLiteDatabase db, String furigana, String word, int word_len){
        ContentValues values = new ContentValues();
        values.put("furigana", furigana);
        values.put("word", word);
        values.put("word_len",word_len);
        db.insert("wordtable", null, values);
    }

    public void answer_saveData(SQLiteDatabase db, String answer, int word_id){
        ContentValues values = new ContentValues();
        values.put("word_id",word_id);
        values.put("answer",answer);
        db.insert("answertable", null, values);
    }
}

