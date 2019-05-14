package com.rapdict.takuro.rapdict;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.ArrayList;

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

        TableLayout varLayout  =  new TableLayout(this);
        varLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        final TableRow.LayoutParams layoutParams = new TableRow.LayoutParams();
        layoutParams.weight = 1;
        layoutParams.setMargins(int_Dp2Px(30,getApplicationContext()),int_Dp2Px(50,getApplicationContext()),int_Dp2Px(30,getApplicationContext()),int_Dp2Px(30,getApplicationContext()));

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
        if (answer_list != null || answer_list.size() != 0) {
            int i = 0;
            for (sample.intent.AnswerData answer : answer_list) {
                answer_view[i] = widgetController.settings(answer_view[i], 10, 10, 10, 10, 10, answer.getAnswer(), TOP, 1);
                question_view[i] = widgetController.settings(question_view[i], 10, 10, 10, 10, 10, answer.getQuestion(), TOP, 1);
                i++;
            }
        }


        for (int i=0;i < answer_list.size(); i++) {
            tableRow[i].addView(answer_view[i],layoutParams);
            tableRow[i].addView(question_view[i],layoutParams);
            varLayout.addView(tableRow[i]);
        }
        scrollView.addView(varLayout);

        setContentView(scrollView);

    }
}
