package com.rapdict.takuro.rapdict;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class Rhyme_Return_Setting_Activity extends MainActivity {
    private static final String QUESTION="question";
    private static final String TIME="time";
    private static final String MIN="min";
    private static final String MAX="max";
    private static final String RET="ret";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rhyme_return_setting);

        //Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ArrayAdapter question=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item);
        ArrayAdapter time=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item);
        ArrayAdapter min=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item);
        final ArrayAdapter max=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item);
        ArrayAdapter ret=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item);

        //問題数10~30
        for(int i=10;i<31;i+=10){
            question.add(i);
        }
        //制限時間1~8
        for(int i=3;i<30;i+=3){
            time.add(i);
        }
        //最小文字2~7
        for(int i=3;i<8;i++){
            min.add(i);
        }
        //最大文字3~10
        for(int i=4;i<11;i++){
            max.add(i);
        }
        //
        for(int i=1;i<5;i++){
            ret.add(i);
        }

        Spinner s1=(Spinner)findViewById(R.id.question_spinner);
        s1.setAdapter(question);

        Spinner s2=(Spinner)findViewById(R.id.time_spinner);
        s2.setAdapter(time);


        Spinner s3=(Spinner)findViewById(R.id.min_spinner);
        s3.setAdapter(min);
        //最小文字>最大文字とならないように、スピナーの値を順次変更する
        s3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            int min_value;
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Spinner spinner=(Spinner) adapterView;
                min_value= (int) spinner.getSelectedItem();
                max.clear();
                for(int f=min_value+1;f<11;f++){
                    max.add(f);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        Spinner s4=(Spinner)findViewById(R.id.max_spinner);
        s4.setAdapter(max);

        Spinner s5=(Spinner)findViewById(R.id.return_spinner);
        s5.setAdapter(ret);

        //ボタンをおされた時の処理
        Button start_button=(Button)findViewById(R.id.start_button);
        start_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Rhyme_Return_Activity.class);

                intent.putExtra(QUESTION,(int) ((Spinner)findViewById(R.id.question_spinner)) .getSelectedItem());
                intent.putExtra(MIN,(int) ((Spinner)findViewById(R.id.min_spinner)) .getSelectedItem());
                intent.putExtra(MAX,(int) ((Spinner)findViewById(R.id.max_spinner)) .getSelectedItem());
                intent.putExtra(TIME,(int) ((Spinner)findViewById(R.id.time_spinner)) .getSelectedItem());
                intent.putExtra(RET,(int) ((Spinner)findViewById(R.id.return_spinner)) .getSelectedItem());
                startActivity(intent);
            }
        });


        //Toolbarのトップページに戻るボタンを出現させる処理
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


    }


}



