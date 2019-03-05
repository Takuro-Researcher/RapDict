package com.rapdict.takuro.rapdict;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Rhyme_Return_Activity extends AppCompatActivity {
    private TextView timerText;
    private SimpleDateFormat dataFormat =
            new SimpleDateFormat("ss.SS", Locale.US);
    private DictOpenHelper helper;

    private static final String QUESTION="question";
    private static final String TIME="time";
    private static final String MIN="min";
    private static final String MAX="max";
    private static final String RET="ret";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        /* データベースに関する記述　*/
        helper=new DictOpenHelper(getApplicationContext());
        SQLiteDatabase db=helper.getReadableDatabase();
        WordAccess wordAccess=new WordAccess();
        List<String> result=new ArrayList<>();

        result=wordAccess.getWords(db,intent.getIntExtra(MIN,0),intent.getIntExtra(MAX,0),intent.getIntExtra(QUESTION,0));
        for(String s : result){
            System.out.println(s);
        }






        /*デザインに関する記述*/
        TableLayout varLayout= new TableLayout(this);
        varLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        );

        //1列目　問題数を表示する部分
        TableRow tableRow1=new TableRow(this);
        tableRow1.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        );
        TextView question_text=new TextView(this);
        TextView question_num=new TextView(this);
        int question_number=intent.getIntExtra(QUESTION,0);

        tableRow1.addView(settings(question_text,false,20,0,0 ,0,0,20,"問題数"));
        tableRow1.addView(settings(question_num,true,0,0,40,20,20,20,Integer.toString(question_number)));
        varLayout.addView(tableRow1);

        //2列目 制限時間を表示する部分
        TableRow tableRow2=new TableRow(this);
        tableRow2.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        );
        timerText=new TextView(this);//ここでインスタンス生成を行った。onCreate外でインスタンス作成を行ったところエラーが出る。クラスのメソッドでウィジェットの値を変更する際は要注意。
        settings(timerText,true,0,20,20,20,0,20,dataFormat.format(0));
        int time_num=intent.getIntExtra(TIME,0);
        final MyCountDownTimer cdt=new MyCountDownTimer(time_num*1000,10);
        cdt.start();

        tableRow2.addView(timerText);
        //2列をひとまとめにするレイアウトを指定。
        TableRow.LayoutParams layoutParams=new TableRow.LayoutParams();
        layoutParams.span=2;
        //第2引数にレイアウト情報を書く
        varLayout.addView(tableRow2,layoutParams);

        //3列目 文字数
        TableRow tableRow3=new TableRow(this);
        tableRow3.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        );
        TextView word_text=new TextView(this);
        tableRow3.addView(settings(word_text,true,0,20,20,20,0,12,""));
//        for(int i=0;i<question_number;i++){
//            result.get(i)
//        }

        setContentView(varLayout);


    }

    //Dpをピクセルに変換する関数
    public static float Dp2Px(float dp, Context context){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return dp * metrics.density;
    }
    //Dpをピクセルに変換、int型に変換する。
    public static int int_Dp2Px(float dp, Context context){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float n=dp * metrics.density;
        return (int)n;
    }
    //TextViewのデザインを決める
    public TextView settings(TextView textView, boolean padd_mode,float padding,  float padding_right,float padding_left,float padding_bottom,float padding_top,float textsize,String text){
        if(padd_mode==false) {
            textView.setPadding(int_Dp2Px(padding, getApplicationContext()), int_Dp2Px(padding, getApplicationContext()), int_Dp2Px(padding, getApplicationContext()), int_Dp2Px(padding, getApplicationContext()));
        }else{
            textView.setPadding(int_Dp2Px(padding_left, getApplicationContext()), int_Dp2Px(padding_top, getApplicationContext()), int_Dp2Px(padding_right, getApplicationContext()), int_Dp2Px(padding_bottom, getApplicationContext()));
        }
        textView.setTextSize(Dp2Px(textsize,getApplicationContext()));
        textView.setText(text);
        return textView;
    }
    //カウントダウンタイマー,
    public class MyCountDownTimer extends CountDownTimer{

        MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            // 完了
            timerText.setText(dataFormat.format(0));
        }

        // インターバルで呼ばれる
        @Override
        public void onTick(long millisUntilFinished) {
            // 残り時間を分、秒、ミリ秒に分割
            //long mm = millisUntilFinished / 1000 / 60;
            //long ss = millisUntilFinished / 1000 % 60;
            //long ms = millisUntilFinished - ss * 1000 - mm * 1000 * 60;
            //timerText.setText(String.format("%1$02d:%2$02d.%3$03d", mm, ss, ms));

            timerText.setText(dataFormat.format(millisUntilFinished));

        }
    }

    public class WordAccess{
        public ArrayList<String>  getWords(SQLiteDatabase database, int min_word, int max_word, int question){
            int nums[]={min_word,max_word};

            Cursor cursor = database.query(
                    "wordtable", // DB名
                    new String[] { "furigana", "word" }, // 取得するカラム名
                    "word_len>=? AND word_len<=?", // WHERE句の列名
                    new String[]{Integer.toString(min_word),Integer.toString(max_word)}, // WHERE句の値
                    null, // GROUP BY句の値
                    null, // HAVING句の値
                    "RANDOM()", // ORDER BY句の値
                    Integer.toString(question)
            );
            ArrayList<String> result = new ArrayList<String>();
            int i=0;
            while (cursor.moveToNext()){
                Word word1=new Word();
                int furigana_id=cursor.getColumnIndex("furigana");
                int word_id=cursor.getColumnIndex("word");
                String furigana=cursor.getString(furigana_id);
                String word=cursor.getString(word_id);
                String resu=word+","+furigana;
                result.add(resu);
                i++;
            }
            return result;
        }
    }

    class Word{
        String furigana;
        String word;
        int word_len;

        public String getFurigana() {
            return furigana;
        }

        public void setFurigana(String furigana) {
            this.furigana = furigana;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public int getWord_len() {
            return word_len;
        }

        public void setWord_len(int word_len) {
            this.word_len = word_len;
        }
    }


}

