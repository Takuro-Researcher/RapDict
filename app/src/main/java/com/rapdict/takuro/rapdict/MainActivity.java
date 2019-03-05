package com.rapdict.takuro.rapdict;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_option, menu);
        return super.onCreateOptionsMenu(menu);
    }


    // メニューが選択されたときの処理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.rhyme_return:
                Intent intent=new Intent(this, Rhyme_Return_Setting_Activity.class);
                startActivity(intent);
            case R.id.rhyme_dict:
                return true;

            case R.id.settings:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
