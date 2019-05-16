package com.rapdict.takuro.rapdict;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.ArrayList;

import static android.view.Gravity.CENTER;
import static android.view.Gravity.TOP;
import static com.rapdict.takuro.rapdict.WidgetController.int_Dp2Px;

public class Result_Activity extends AppCompatActivity {
    private Intent intent;
    private static final String ANSWER_LIST = "answer_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        ArrayList<sample.intent.AnswerData> answer_list = (ArrayList<sample.intent.AnswerData>) getIntent().getSerializableExtra(ANSWER_LIST);
        WidgetController widgetController = new WidgetController(this);

        final TableRow tableRow[] = new TableRow[120];
        final CheckBox checkBox[] = new CheckBox[120];
        final TextView question_view[] = new TextView[120];
        final TextView answer_view[] = new TextView[120];
        TableRow buttonRow = new TableRow(this);

        TableLayout varLayout  =  new TableLayout(this);
        varLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        final TableRow.LayoutParams layoutParams = new TableRow.LayoutParams();
        layoutParams.weight = 1;
        final TableRow.LayoutParams layoutParams4 = new TableRow.LayoutParams();
        layoutParams4.weight = 1;
        layoutParams4.span = 3;
        layoutParams4.setMargins(int_Dp2Px(15,getApplicationContext()),int_Dp2Px(15,getApplicationContext()),int_Dp2Px(15,getApplicationContext()),int_Dp2Px(15,getApplicationContext()));
        //left top right bottomの順番でマージンを設定する
        layoutParams.setMargins(int_Dp2Px(0,getApplicationContext()),int_Dp2Px(10,getApplicationContext()),int_Dp2Px(0,getApplicationContext()),int_Dp2Px(10,getApplicationContext()));

        ScrollView scrollView = new ScrollView(this);
        scrollView.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        //   答えの数だけ初期化を行う。
        for(int i=0;i<answer_list.size();i++){
            tableRow[i] = new TableRow(this);
            tableRow[i].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            answer_view[i] = new TextView(this);
            question_view[i] = new TextView(this);
            checkBox[i] = new CheckBox(this);
        }
        Button record_button = new Button(this);
        record_button = widgetController.settings(record_button,10,10,10,8,8,"ワードを保存する！",CENTER,0);
        buttonRow.addView(record_button,layoutParams4);

        if (answer_list != null || answer_list.size() != 0) {
            int i = 0;
            for (sample.intent.AnswerData answer : answer_list) {
                question_view[i] = widgetController.settings(question_view[i], 10, 10, 10, 10, 8, answer.getQuestion(), TOP, 1);
                answer_view[i] = widgetController.settings(answer_view[i], 10, 10, 10, 10, 8, answer.getAnswer(), TOP, 1);
                i++;
            }
        }

        for (int i=0;i < answer_list.size(); i++) {
            tableRow[i].addView(question_view[i], layoutParams);
            tableRow[i].addView(answer_view[i], layoutParams);
            tableRow[i].addView(checkBox[i], layoutParams);
            varLayout.addView(tableRow[i]);
        }
        varLayout.addView(buttonRow);
        scrollView.addView(varLayout);
        setContentView(scrollView);

    }
}
