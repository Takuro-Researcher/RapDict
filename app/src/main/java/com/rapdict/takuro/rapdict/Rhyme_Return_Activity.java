package com.rapdict.takuro.rapdict;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import sample.intent.AnswerData;

import static android.text.TextUtils.isEmpty;
import static android.view.Gravity.CENTER;

public class Rhyme_Return_Activity extends AppCompatActivity{
    private TextView timerText;
    private MyCountDownTimer cdt;
    private static TextView question_text;
    private static TextView word_text;
    private static TextView furigana_text;
    private static ArrayList<Word> question_list;
    private static ArrayList<AnswerData> answer_list;
    private Intent intent;


    private SimpleDateFormat dataFormat =
            new SimpleDateFormat("ss.SS", Locale.US);
    private DictOpenHelper helper;

    private static final String QUESTION="question";
    private static final String TIME="time";
    private static final String MIN="min";
    private static final String MAX="max";
    private static final String RET="ret";
    private static final String ANSWER_LIST="answer_list";
    int finish_q=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent=getIntent();
        /* データベースに関する記述　*/
        helper=new DictOpenHelper(getApplicationContext());
        SQLiteDatabase db=helper.getReadableDatabase();
        WordAccess wordAccess=new WordAccess();
        question_list =new ArrayList<Word>();
        answer_list=new ArrayList<AnswerData>();


        question_list =wordAccess.getWords(db,intent.getIntExtra(MIN,0),intent.getIntExtra(MAX,0),intent.getIntExtra(QUESTION,0));
        /*デザインに関する記述*/
        final TableLayout varLayout= new TableLayout(this);
        varLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        );
        //2列をひとまとめにするレイアウトを指定。
        final TableRow.LayoutParams layoutParams=new TableRow.LayoutParams();
        layoutParams.weight=1;

        //1列目　問題数を表示する部分
        final TableRow tableRow1=new TableRow(this);
        tableRow1.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        question_text=new TextView(this);
        Button back_button=new Button(this);
        back_button.setText("設定へ戻る");


        final int question_number=intent.getIntExtra(QUESTION,0);
        //第2引数にレイアウト情報を書く
        tableRow1.addView(settings(question_text,20,20,20,20 ,15,"問題数:  "+Integer.toString(question_number)),layoutParams);
        tableRow1.addView(back_button);
        varLayout.addView(tableRow1);

        //2列目 制限時間を表示する部分
        TableRow tableRow2=new TableRow(this);
        tableRow2.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        timerText=new TextView(this);//ここでインスタンス生成を行った。onCreate外でインスタンス作成を行ったところエラーが出る。クラスのメソッドでウィジェットの値を変更する際は要注意。
        layoutParams.span=2;
        settings(timerText,20,20,20,0,15,dataFormat.format(0));
        int time_num=intent.getIntExtra(TIME,0);
        cdt=new MyCountDownTimer(time_num*1000,10);
        cdt.start();
        tableRow2.addView(timerText,layoutParams);
        varLayout.addView(tableRow2);

        //3列目 文字列
        TableRow tableRow3=new TableRow(this);
        tableRow3.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        word_text=new TextView(this);
        word_text=settings(word_text,20,20,20,15,17,giveword(question_number, question_list));
        word_text.setGravity(CENTER);
        word_text.setWidth(0);
        tableRow3.addView(word_text,layoutParams);
        varLayout.addView(tableRow3);

        //4列目 フリガナ
        TableRow tableRow4=new TableRow(this);
        tableRow4.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        furigana_text=new TextView(this);
        furigana_text=settings(furigana_text,20,20,20,0,8,givefurigana(question_number, question_list));
        furigana_text.setGravity(CENTER);
        furigana_text.setWidth(0);
        tableRow4.addView(furigana_text,layoutParams);
        varLayout.addView(tableRow4);

        //5列目 入力
        final TableRow tableRow5=new TableRow(this);
        tableRow5.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        final Button add_button= new Button(this);
        final Button next_button=new Button(this);

        add_button.setPadding(int_Dp2Px(10,getApplicationContext()),int_Dp2Px(10,getApplicationContext()),int_Dp2Px(10,getApplicationContext()),int_Dp2Px(10,getApplicationContext()));
        add_button.setTextSize(Dp2Px(8,getApplicationContext()));
        add_button.setText("思いついた！");
        add_button.setGravity(CENTER);
        add_button.setWidth(0);


        next_button.setPadding(int_Dp2Px(10,getApplicationContext()),int_Dp2Px(10,getApplicationContext()),int_Dp2Px(10,getApplicationContext()),int_Dp2Px(10,getApplicationContext()));
        next_button.setTextSize(Dp2Px(8,getApplicationContext()));
        next_button.setText("次の問題へ!");
        next_button.setGravity(CENTER);
        next_button.setWidth(0);

        //2列利用
        final TableRow.LayoutParams layoutParams2=new TableRow.LayoutParams();
        layoutParams2.weight=1;
        layoutParams2.setMargins(int_Dp2Px(30,getApplicationContext()),int_Dp2Px(50,getApplicationContext()),int_Dp2Px(30,getApplicationContext()),int_Dp2Px(30,getApplicationContext()));
        tableRow5.addView(add_button,layoutParams2);
        tableRow5.addView(next_button,layoutParams2);
        varLayout.addView(tableRow5);


        //ボタン押下後の、処理を記述。また状況によっては（踏み返し3個以上の場合）6列目に韻を入力するテキストボックスを用意する。
        final TableRow tableRow6=new TableRow(this);
        tableRow6.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        varLayout.addView(tableRow6);

        //押韻送信ボタンを7列目に用意
        final TableRow tableRow7=new TableRow(this);
        tableRow7.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        varLayout.addView(tableRow7);

        //カラムの表記を定義
        final TableRow.LayoutParams layoutParams3=new TableRow.LayoutParams();
        layoutParams3.weight=1;
        layoutParams3.setMargins(int_Dp2Px(15,getApplicationContext()),int_Dp2Px(25,getApplicationContext()),int_Dp2Px(15,getApplicationContext()),int_Dp2Px(15,getApplicationContext()));
        final TableRow.LayoutParams layoutParams4=new TableRow.LayoutParams();
        layoutParams4.weight=1;
        layoutParams4.span=2;
        layoutParams4.setMargins(int_Dp2Px(30,getApplicationContext()),int_Dp2Px(40,getApplicationContext()),int_Dp2Px(30,getApplicationContext()),int_Dp2Px(15,getApplicationContext()));

        final Button record_button=new Button(this);
        record_button.setPadding(int_Dp2Px(10,getApplicationContext()),int_Dp2Px(10,getApplicationContext()),int_Dp2Px(10,getApplicationContext()),int_Dp2Px(10,getApplicationContext()));
        record_button.setTextSize(Dp2Px(8,getApplicationContext()));
        record_button.setText("次の問題へ!");
        record_button.setGravity(CENTER);
        record_button.setWidth(0);

        final EditText insert_text1=new EditText(this);
        insert_text1.setWidth(0);
        insert_text1.setHint("ライムを入力");
        final EditText insert_text2=new EditText(this);
        insert_text2.setWidth(0);
        insert_text2.setHint("ライムを入力");
        final EditText insert_text3=new EditText(this);
        insert_text3.setWidth(0);
        insert_text3.setHint("ライムを入力");
        final EditText insert_text4=new EditText(this);
        insert_text4.setWidth(0);
        insert_text4.setHint("ライムを入力");



        add_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                tableRow5.removeView(add_button);
                tableRow5.removeView(next_button);
                cdt.cancel();
                switch (intent.getIntExtra(RET,0)){
                    case 1:
                        tableRow5.addView(insert_text1,layoutParams3);
                        break;
                    case 2:
                        tableRow5.addView(insert_text1,layoutParams3);
                        tableRow5.addView(insert_text2,layoutParams3);

                        break;
                    case 3:
                        tableRow5.addView(insert_text1,layoutParams3);
                        tableRow5.addView(insert_text2,layoutParams3);
                        tableRow6.addView(insert_text3,layoutParams3);
                        break;
                    case 4:
                        tableRow5.addView(insert_text1,layoutParams3);
                        tableRow5.addView(insert_text2,layoutParams3);
                        tableRow6.addView(insert_text3,layoutParams3);
                        tableRow6.addView(insert_text4,layoutParams3);
                        break;
                }
                tableRow7.addView(record_button,layoutParams4);
            }
        });

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish_q++;
                if(finish_q< intent.getIntExtra(QUESTION,0)) {
                    next_question_change(finish_q, intent.getIntExtra(QUESTION, 0));
                    cdt.start();
                }else{
                    Intent intent2 = new Intent(getApplicationContext(),Rhyme_Return_Setting_Activity.class);
                    intent2.putExtra(ANSWER_LIST,answer_list);
                    startActivity(intent2);
                }
                System.out.println(finish_q);
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        record_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnswerData answer1=new AnswerData();
                AnswerData answer2=new AnswerData();
                AnswerData answer3=new AnswerData();
                AnswerData answer4=new AnswerData();

                switch (intent.getIntExtra(RET,0)) {
                    case 1:
                        if (!isEmpty(insert_text1.getText())) {
                            answer1.answerSet(giveword_id(question_number-finish_q, question_list), String.valueOf(insert_text1.getText()), giveword(question_number-finish_q, question_list));
                            answer_list.add(answer1);
                        }
                        insert_text1.setText("");
                        tableRow5.removeAllViews();
                        break;
                    case 2:
                        if (!isEmpty(insert_text1.getText())) {
                            answer1.answerSet(giveword_id(question_number-finish_q, question_list), String.valueOf(insert_text1.getText()), giveword(question_number-finish_q, question_list));
                            answer_list.add(answer1);
                        }
                        if (!isEmpty(insert_text2.getText())) {
                            answer2.answerSet(giveword_id(question_number-finish_q, question_list), String.valueOf(insert_text2.getText()), giveword(question_number-finish_q, question_list));
                            answer_list.add(answer2);
                        }
                        insert_text1.setText("");
                        insert_text2.setText("");
                        tableRow5.removeAllViews();
                        break;
                    case 3:
                        if (!isEmpty(insert_text1.getText())) {
                            answer1.answerSet(giveword_id(question_number-finish_q, question_list), String.valueOf(insert_text1.getText()), giveword(question_number-finish_q, question_list));
                            answer_list.add(answer1);
                        }
                        if (!isEmpty(insert_text2.getText())) {
                            answer2.answerSet(giveword_id(question_number-finish_q, question_list), String.valueOf(insert_text2.getText()), giveword(question_number-finish_q, question_list));
                            answer_list.add(answer2);
                        }
                        if (!isEmpty(insert_text3.getText())) {
                            answer3.answerSet(giveword_id(question_number-finish_q, question_list), String.valueOf(insert_text3.getText()),giveword(question_number-finish_q, question_list));
                            answer_list.add(answer3);
                        }
                        insert_text1.setText("");
                        insert_text2.setText("");
                        insert_text3.setText("");
                        tableRow5.removeAllViews();
                        tableRow6.removeAllViews();
                        break;
                    case 4:
                        if (!isEmpty(insert_text1.getText())) {
                            answer1.answerSet(giveword_id(question_number-finish_q, question_list), String.valueOf(insert_text1.getText()), giveword(question_number-finish_q, question_list));
                            answer_list.add(answer1);
                        }
                        if (!isEmpty(insert_text2.getText())) {
                            answer2.answerSet(giveword_id(question_number-finish_q, question_list), String.valueOf(insert_text2.getText()), giveword(question_number-finish_q, question_list));
                            answer_list.add(answer2);
                        }
                        if (!isEmpty(insert_text3.getText())) {
                            answer3.answerSet(giveword_id(question_number-finish_q, question_list), String.valueOf(insert_text3.getText()),giveword(question_number-finish_q, question_list));
                            answer_list.add(answer3);
                        }
                        if (!isEmpty(insert_text4.getText())) {
                            answer4.answerSet(giveword_id(question_number-finish_q, question_list), String.valueOf(insert_text4.getText()), giveword(question_number-finish_q, question_list));
                            answer_list.add(answer4);
                        }
                        insert_text1.setText("");
                        insert_text2.setText("");
                        insert_text3.setText("");
                        insert_text4.setText("");
                        tableRow5.removeAllViews();
                        tableRow6.removeAllViews();
                        break;
                }
                
                for(AnswerData s : answer_list){
                    System.out.println(s.getAnswer());
                    System.out.println(s.getQuestion());
                    System.out.println(s.getQuestion_id());
                }

                tableRow7.removeAllViews();
                tableRow5.addView(add_button);
                tableRow5.addView(next_button);
                finish_q++;
                System.out.println(finish_q);
                if(finish_q<10) {
                    next_question_change(finish_q, intent.getIntExtra(QUESTION, 0));
                    cdt.start();
                }else{
                    Intent intent=new Intent(getApplicationContext(),Rhyme_Return_Setting_Activity.class);
                    startActivity(intent);
                }
            }
        });


        //レイアウトをビューに適用
        setContentView(varLayout);
    }

    /*画面を写った瞬間に起動される。カウントダウンタイマーを停止させれば、後ろ側で問題が進むことはなく、
    　次に訪れた時はonCreateによる問題設定やレイアウトは再定義される*/
    @Override
    public void onStop(){
        super.onStop();
        cdt.cancel();
    }

    //残り問題数を更新し、問題文字とフリガナを更新する
    public static void next_question_change(int finish_q,int question){
        int i=question-finish_q;
        question_text.setText("問題数:  "+Integer.toString(i));
        furigana_text.setText(givefurigana(i, question_list));
        word_text.setText(giveword(i, question_list));
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
    public TextView settings(TextView textView, float padding_right,float padding_left,float padding_bottom,float padding_top,float textsize,String text){

        textView.setPadding(int_Dp2Px(padding_left, getApplicationContext()), int_Dp2Px(padding_top, getApplicationContext()), int_Dp2Px(padding_right, getApplicationContext()), int_Dp2Px(padding_bottom, getApplicationContext()));

        textView.setTextSize(Dp2Px(textsize,getApplicationContext()));
        textView.setText(text);
        return textView;
    }
    //EditTextのデザインを決める。
    public EditText settings(EditText editText,float padding_right,float padding_left,float padding_bottom,float padding_top,float textsize,int gravity,int width){
        editText.setPadding(int_Dp2Px(padding_left, getApplicationContext()), int_Dp2Px(padding_top, getApplicationContext()), int_Dp2Px(padding_right, getApplicationContext()), int_Dp2Px(padding_bottom, getApplicationContext()));
        editText.setTextSize(Dp2Px(textsize,getApplicationContext()));
        editText.setGravity(gravity);
        editText.setWidth(width);
        return editText;
    }

    //問題番号に対する、文字列を与える
    public static String giveword(int question_num, List<Word> result){
        Word word1;
        word1=result.get(question_num-1);
        return word1.getWord();
    }
    //問題番号に対する、フリガナを与える。
    public static String givefurigana(int question_num, List<Word> result){
        Word word1;
        word1=result.get(question_num-1);
        return word1.getFurigana();
    }
    public static int giveword_id(int question_num, List<Word> result){
        Word word1;
        word1=result.get(question_num-1);
        return word1.getWord_id();
    }

    //これより下はクラス

    //カウントダウンタイマー,
    public class MyCountDownTimer extends CountDownTimer{

        MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onFinish() {
            // 完了
            finish_q++;
            next_question_change(finish_q,intent.getIntExtra(QUESTION,0));
            start();
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

    //SQLを通す＆Wordのクラス配列に変換する
    public class WordAccess{
        public ArrayList<Word>  getWords(SQLiteDatabase database, int min_word, int max_word, int question){

            Cursor cursor = database.query(
                    "wordtable", // DB名
                    new String[] {"_id", "furigana", "word" }, // 取得するカラム名
                    "word_len>=? AND word_len<=?", // WHERE句の列名
                    new String[]{Integer.toString(min_word),Integer.toString(max_word)}, // WHERE句の値
                    null, // GROUP BY句の値
                    null, // HAVING句の値
                    "RANDOM()", // ORDER BY句の値
                    Integer.toString(question)
            );
            ArrayList<Word> result = new ArrayList<Word>();

            while (cursor.moveToNext()){
                Word word1=new Word();
                int furigana_id=cursor.getColumnIndex("furigana");
                int word_id=cursor.getColumnIndex("word");
                int _id_id=cursor.getColumnIndex("_id");
                String furigana=cursor.getString(furigana_id);
                String word=cursor.getString(word_id);
                int id =cursor.getInt(_id_id);
                word1.setWord(word);
                word1.setFurigana(furigana);
                word1.setWord_id(id);
                result.add(word1);
            }
            return result;
        }
    }

}

