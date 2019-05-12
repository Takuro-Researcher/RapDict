package com.rapdict.takuro.rapdict;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
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
import static android.view.Gravity.TOP;
import static com.rapdict.takuro.rapdict.WidgetController.int_Dp2Px;

public class Rhyme_Return_Activity extends AppCompatActivity {
    private TextView timerText;
    private MyCountDownTimer cdt;
    private static TextView question_text;
    private static TextView word_text;
    private static TextView furigana_text;
    private static ArrayList<Word> question_list;
    private static ArrayList<AnswerData> answer_list;
    private Intent intent;
    private SimpleDateFormat dataFormat  =  new SimpleDateFormat("ss.SS", Locale.US);
    private DictOpenHelper helper;
    private static final String QUESTION = "question";
    private static final String TIME = "time";
    private static final String MIN = "min";
    private static final String MAX = "max";
    private static final String RET = "ret";
    private static final String ANSWER_LIST = "answer_list";
    int finish_q = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        /* データベースに関する記述　*/
        helper  =  new DictOpenHelper(getApplicationContext());
        SQLiteDatabase db  =  helper.getReadableDatabase();
        WordAccess wordAccess  =  new WordAccess();
        /* 出題されるワード */
        question_list  =  new ArrayList<Word>();
        question_list  =  wordAccess.getWords(db,intent.getIntExtra(MIN,0),intent.getIntExtra(MAX,0),intent.getIntExtra(QUESTION,0));
        /* ワードに対する答え */
        answer_list  =  new ArrayList<AnswerData>();
        /* 要素のデザインを決めるコントローラ */
        WidgetController widgetController  =  new WidgetController(getApplicationContext());

        final TableRow tableRow[] = new TableRow[7];
        for(int i=0; i<tableRow.length; i++){
            tableRow[i] = new TableRow(this);
            tableRow[i].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        /*デザインに関する記述*/
        TableLayout varLayout  =  new TableLayout(this);
        varLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //2列をひとまとめにするレイアウトを指定。
        final TableRow.LayoutParams layoutParams = new TableRow.LayoutParams();
        layoutParams.weight = 1;

        //1列目　問題数を表示する部分
        question_text = new TextView(this);
        Button back_button = new Button(this);
        back_button.setText("設定へ戻る");
        final int question_number = intent.getIntExtra(QUESTION,0);
        //第2引数にレイアウト情報を書く
        tableRow[0].addView(widgetController.settings(question_text,20,20,20,20 ,15,"問題数:  "+Integer.toString(question_number),TOP,1),layoutParams);
        tableRow[0].addView(back_button);

        //2列目 制限時間を表示する部分
        timerText = new TextView(this);
        layoutParams.span = 2;
        widgetController.settings(timerText,20,20,20,0,15,dataFormat.format(0),TOP, 1);
        int time_num = intent.getIntExtra(TIME,0);
        cdt = new MyCountDownTimer(time_num*1000,10);
        cdt.start();
        tableRow[1].addView(timerText,layoutParams);

        //3列目 文字列
        word_text = new TextView(this);
        word_text = widgetController.settings(word_text,20,20,20,15,17,giveword(question_number, question_list),CENTER,0);
        tableRow[2].addView(word_text,layoutParams);

        //4列目 フリガナ
        furigana_text = new TextView(this);
        furigana_text = widgetController.settings(furigana_text,20,20,20,0,8,givefurigana(question_number, question_list),CENTER,0);
        tableRow[3].addView(furigana_text,layoutParams);

        //5列目 入力
        Button add_button =  new Button(this);
        add_button  =  widgetController.settings(add_button,10,10,10,10,8, "思いついた!", CENTER,0);
        Button next_button = new Button(this);
        next_button  =  widgetController.settings(next_button,10,10,10,10,8,"次の問題へ",CENTER,0);

        //2列利用
        final TableRow.LayoutParams layoutParams2 = new TableRow.LayoutParams();
        layoutParams2.weight = 1;
        layoutParams2.setMargins(int_Dp2Px(30,getApplicationContext()),int_Dp2Px(50,getApplicationContext()),int_Dp2Px(30,getApplicationContext()),int_Dp2Px(30,getApplicationContext()));
        tableRow[4].addView(add_button,layoutParams2);
        tableRow[4].addView(next_button,layoutParams2);

        //カラムの表記を定義
        final TableRow.LayoutParams layoutParams3 = new TableRow.LayoutParams();
        layoutParams3.weight = 1;
        layoutParams3.setMargins(int_Dp2Px(15,getApplicationContext()),int_Dp2Px(25,getApplicationContext()),int_Dp2Px(15,getApplicationContext()),int_Dp2Px(15,getApplicationContext()));
        final TableRow.LayoutParams layoutParams4 = new TableRow.LayoutParams();
        layoutParams4.weight = 1;
        layoutParams4.span = 2;
        layoutParams4.setMargins(int_Dp2Px(30,getApplicationContext()),int_Dp2Px(40,getApplicationContext()),int_Dp2Px(30,getApplicationContext()),int_Dp2Px(15,getApplicationContext()));

        final Button record_button = new Button(this);
        widgetController.settings(record_button,10,10,10,10,8,"次の問題へ!",CENTER,0);

        final EditText editText[]  =  new EditText[4];
        for(int i = 0; i<editText.length; i++){
            editText[i]  =  new EditText(this);
            editText[i].setWidth(0);
            editText[i].setHint("ライムを入力");
        }

        final Button finalAdd_button  =  add_button;
        final Button finalNext_button  =  next_button;
        add_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                tableRow[4].removeView(finalAdd_button);
                tableRow[4].removeView(finalNext_button);
                cdt.cancel();
                for(int i = 0; i<intent.getIntExtra(RET,0);i++){
                    if(i == 0 || i == 1){
                        tableRow[4].addView(editText[i],layoutParams3);
                    }else if(i == 2 || i == 3){
                        tableRow[5].addView(editText[i],layoutParams3);
                    }
                }
                tableRow[6].addView(record_button,layoutParams4);
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
                    Intent intent2  =  new Intent(getApplicationContext(),Rhyme_Return_Setting_Activity.class);
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

        final Button finalAdd_button1 = add_button;
        final Button finalNext_button1 = next_button;

        record_button.setOnClickListener(new View.OnClickListener() {
            AnswerData answer[] = new AnswerData[4];
            @Override
            public void onClick(View view) {
                for(int i = 0; i<intent.getIntExtra(RET,0); i++){
                    answer[i]  =  new AnswerData();
                    if(!isEmpty(editText[i].getText())) {
                        answer[i].answerSet(giveword_id(question_number - finish_q, question_list), String.valueOf(editText[i].getText()), giveword(question_number - finish_q, question_list));
                        editText[i].setText("");
                        answer_list.add(answer[i]);
                    }
                }
                tableRow[4].removeAllViews();
                if(intent.getIntExtra(RET,0)>2){
                    tableRow[5].removeAllViews();
                }
                tableRow[6].removeAllViews();
                tableRow[4].addView(finalAdd_button1);
                tableRow[4].addView(finalNext_button1);
                finish_q++;
                if(finish_q<10) {
                    next_question_change(finish_q, intent.getIntExtra(QUESTION, 0));
                    cdt.start();
                }else{
                    Intent intent = new Intent(getApplicationContext(),Rhyme_Return_Setting_Activity.class);
                    startActivity(intent);
                }
            }
        });
        //レイアウトをビューに適用
        for(int i=0; i<tableRow.length; i++){
            varLayout.addView(tableRow[i]);
        }
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
        int i = question-finish_q;
        question_text.setText("問題数:  "+Integer.toString(i));
        furigana_text.setText(givefurigana(i, question_list));
        word_text.setText(giveword(i, question_list));
    }

    //問題番号に対する、文字列を与える
    public static String giveword(int question_num, List<Word> result){
        Word word1;
        word1 = result.get(question_num-1);
        return word1.getWord();
    }
    //問題番号に対する、フリガナを与える。
    public static String givefurigana(int question_num, List<Word> result){
        Word word1;
        word1 = result.get(question_num-1);
        return word1.getFurigana();
    }
    //問題番号に対する、Word_idを与える。
    public static int giveword_id(int question_num, List<Word> result){
        Word word1;
        word1 = result.get(question_num-1);
        return word1.getWord_id();
    }


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
            timerText.setText(dataFormat.format(millisUntilFinished));
        }
    }
}