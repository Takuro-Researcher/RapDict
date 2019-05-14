package com.rapdict.takuro.rapdict;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class Result_Activity extends AppCompatActivity {
    private Intent intent;
    private static final String ANSWER_LIST = "answer_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        ArrayList<sample.intent.AnswerData> answer_list = (ArrayList<sample.intent.AnswerData>) getIntent().getSerializableExtra(ANSWER_LIST);

        final TableRow tableRow[] = new TableRow[120];
        final CheckBox checkBox[] = new CheckBox[120];
        ScrollView scrollView = new ScrollView(this);
        scrollView.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        for(int i=0;i<answer_list.size();i++){
            tableRow[i] = new TableRow(this);
            tableRow[i].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams();
        layoutParams.weight = 1;

        setContentView(scrollView);

    }
}
